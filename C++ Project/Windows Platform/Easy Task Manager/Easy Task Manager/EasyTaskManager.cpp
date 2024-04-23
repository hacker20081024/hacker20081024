#include "EasyTaskManager.h"
#include <QtWidgets/QApplication>

HANDLE filter_semaphore = CreateSemaphore(NULL, 1, 1, L"Filter");
vector<QLineEdit*> ProcessInfoWidgetList = {};
vector<int> ProcessPIDList = {};
vector<QLineEdit*> form_line_edit_list = {};
vector<void*> memory_list = {};


void customMessageHandler(QtMsgType type, const QMessageLogContext& context, const QString& msg) {
	QByteArray localMsg = msg.toLocal8Bit();
	switch (type) {
	case QtDebugMsg:
		fprintf(stderr, "Debug: %s\\n", localMsg.constData());
		break;
	case QtInfoMsg:
		fprintf(stderr, "Info: %s\\n", localMsg.constData());
		break;
	case QtWarningMsg:
		fprintf(stderr, "Warning: %s\\n", localMsg.constData());
		break;
	case QtCriticalMsg:
		fprintf(stderr, "Critical: %s\\n", localMsg.constData());
		break;
	case QtFatalMsg:
		fprintf(stderr, "Fatal: %s\\n", localMsg.constData());
		abort();
	}
}

bool GetProcessInSystem(vector<int>* Storage_PID_Vector) {
	LPDWORD Process_Identification = (LPDWORD)malloc(sizeof(DWORD) * 1300);
	if (Process_Identification == NULL) {
		free(Process_Identification);
		return false;
	}
	for (int i = 0; i < 1300; i++) {
		Process_Identification[i] = -1;
	};
	DWORD received_Process_Identification_size;
	if (!EnumProcesses(Process_Identification, sizeof(DWORD) * 1300, &received_Process_Identification_size)) {
		free(Process_Identification);
		return false;
	}
	else {
		for (int i = 0; i < 1300; i++) {
			Storage_PID_Vector->push_back((int)Process_Identification[i]);
		}
		free(Process_Identification);
		return true;
	};
};

HANDLE GetProcessMemoryAddressByPID(int PID) {
	return OpenProcess(PROCESS_QUERY_INFORMATION, FALSE, PID);
}

bool GetProcessAssociateFilePath(HANDLE Process_Handle, char* buffer, int buffer_length) {
	if (!GetProcessImageFileNameA(Process_Handle, buffer, buffer_length)) {
		return false;
	}
	else {
		return true;
	}
}

bool GetProcessIncludeModule(DWORD Process_PID, vector<HMODULE>* module_vector) {
	HANDLE process_handle = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, Process_PID);
	HMODULE* module_list = (HMODULE*)malloc(sizeof(HMODULE) * 3060);
	if (module_list == NULL) {
		free(module_list);
		return false;
	}
	else {
		for (int i = 0; i < 3060; i++) {
			module_list[i] = NULL;
		}
		DWORD need_size;
		if (!EnumProcessModulesEx(process_handle, module_list, sizeof(HMODULE) * 3060, &need_size, LIST_MODULES_ALL)) {
			free(module_list);
			return false;
		}
		else {
			for (int i = 0; i < 3060; i++) {
				if (module_list[i] == NULL) {
					break;
				}
				else {
					module_vector->push_back(module_list[i]);
				}
			}
			return true;
		}
	}
}

bool GetModuleName(int Process_PID, HMODULE module_handle, char* name, DWORD size) {
	HANDLE process_handle = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, Process_PID);
	if (!GetModuleBaseNameA(process_handle, module_handle, name, size)) {
		return false;
	}
	else {
		return true;
	}
};

bool GetModulePath(int Process_PID, HMODULE module_handle, char* file_path, DWORD size) {
	HANDLE process_handle = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, Process_PID);
	if (!GetModuleFileNameA(module_handle, file_path, size)) {
		return false;
	}
	else {
		return true;
	}
}

MyMenu::MyMenu(QWidget* parent) : QMenu(parent) {

}

MyShowPIDAction::MyShowPIDAction(QWidget* parent) : QAction(parent) {

}

void MyShowPIDAction::triggeredShowPIDEvent() {
	bool result = emit this->ShowPIDEvent(this);
}


void MyShowPIDAction::TriggeredResponse() {
	this->triggeredShowPIDEvent();
};

MyKillPIDAction::MyKillPIDAction(QWidget* parent) : QAction(parent) {

}

void MyKillPIDAction::TriggeredResponse() {
	const char* danger_process_file[24] = { "explorer.exe", "lsass.exe","svchost.exe","csrss.exe","wininit.exe",
										   "smss.exe", "services.exe","spoolsv.exe","winlogon.exe","LsaIso.exe",
										   "fontdrvhost.exe","WUDFHost.exe","RuntimeBroker.exe","dllhost.exe",
										   "ctfmon.exe","ChsIME.exe","SystemSettingsBroker.exe","ApplicationFrameHost.exe",
										   "SecurityHealthSystray.exe","vmcompute.exe","ArmouryCrateControlInterface.exe",
										   "spoolsv.exe","WmiPrvSE.exe","amdfendrsr.exe"
	};
	int kill_process_pid = emit this->TriggeredGetProcessPID();
	char process_associate_file_path[2048] = "";
	char process_associate_exe[1024] = "";
	GetProcessAssociateFilePath(OpenProcess(PROCESS_QUERY_INFORMATION, FALSE, kill_process_pid), process_associate_file_path, 2048);
	string file_path(process_associate_file_path);
	int index = 0;
	for (int i = file_path.find_last_of("\\") + 1; i < file_path.size(); i++) {
		process_associate_exe[index] = file_path.at(i);
		index += 1;
	}
	HANDLE task_window = emit this->GetTaskManagerWindows();
	if (0 < kill_process_pid && kill_process_pid < 1024) { // system process, can't kill
		char critical_note[450];
		sprintf(critical_note, "Process identification \"%d\" point to system process, can't kill", kill_process_pid);
		QMessageBox::critical((QWidget*)task_window, "Process Kill Error", critical_note, QMessageBox::Button::Cancel);
		return;
	}
	else {
		for (int i = 0; i < 24; i++) {
			qDebug() << process_associate_exe << endl;
			if (strcmp(process_associate_exe, danger_process_file[i]) == 0) {
				char critical_note[450];
				sprintf(critical_note, "Process identification \"%d\" point to system process(Associated System File: \"%s\"), can't kill", kill_process_pid, process_associate_exe);
				QMessageBox::critical((QWidget*)task_window, "Process Kill Error", critical_note, QMessageBox::Button::Cancel);
				return;
			};
		}
		char warning_note[450];
		sprintf(warning_note, "Do you want to kill this process?\n(PID:\"%d\", Associated System File: \"%s\")", kill_process_pid, process_associate_exe);
		QMessageBox::StandardButton click_button = QMessageBox::warning((QWidget*)task_window, "Process Kill Warning", warning_note, QMessageBox::Button::Yes | QMessageBox::Button::No);
		if (click_button == QMessageBox::Yes) {
			TerminateProcess(OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_TERMINATE, FALSE, kill_process_pid), 0);
		};
	}
}

MySearchModuleMemoryAction::MySearchModuleMemoryAction(QWidget* parent) : QAction(parent) {

}

void MySearchModuleMemoryAction::TriggeredResponse() {
	char associate_file[2048] = "";
	int process_pid = emit this->GetAssociatedPID();
	emit this->GetModuleAssociateFile(associate_file);
	HANDLE process_handle = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, process_pid);
	HMODULE include_module_list[65535] = { NULL };
	HMODULE search_module;
	DWORD need_size;
	MODULEINFO module_info;
	EnumProcessModulesEx(process_handle, include_module_list, sizeof(include_module_list), &need_size, LIST_MODULES_ALL);
	for (int i = 0; i < sizeof(include_module_list) / sizeof(HMODULE); i++) {
		if (include_module_list[i] == NULL) {
			break;
		}
		else {
			char file_path_buffer[2048] = "";
			char file_name[1024] = "";
			GetModuleFileNameA(include_module_list[i], file_path_buffer, sizeof(file_path_buffer) / sizeof(char));
			string file_path(file_path_buffer);
			int index = 0;
			for (int j = file_path.find_last_of("\\") + 1; j < file_path.size(); j++) {
				file_name[index] = file_path.at(j);
				index += 1;
			}
			if (strcmp(file_name, associate_file) == 0) {
				if (!GetModuleInformation(process_handle, include_module_list[i], &module_info, sizeof(module_info))) {
					QMessageBox::critical(((ShowMemoryAddressInfo*)this->parent()), "Get Memory Information Error", "Can't get information of this memory", QMessageBox::Button::Cancel);
					return;
				};
				break;
			};
		}
	};
	QMainWindow* window = new QMainWindow(((ShowMemoryAddressInfo*)this->parent()));
	window->setFixedSize(800, 55);
	window->setWindowModality(Qt::WindowModality::ApplicationModal);
	if (module_info.EntryPoint == NULL || module_info.lpBaseOfDll == NULL || module_info.SizeOfImage == NULL) {
		QMessageBox::critical(((ShowMemoryAddressInfo*)this->parent()), "Get Memory Information Error", "Can't get information of this memory", QMessageBox::Button::Cancel);
	}
	else {
		window->setWindowTitle("Memory Information");
		QLabel* label1 = new QLabel(window);
		label1->setAlignment(Qt::AlignCenter);
		label1->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;");
		label1->resize(200, 25);
		label1->setText("Module Address");
		label1->move(0, 0);
		label1->show();
		QLabel* label2 = new QLabel(window);
		label2->setAlignment(Qt::AlignCenter);
		label2->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;");
		label2->resize(200, 25);
		label2->setText("Entry Point Address");
		label2->move(label1->x() + label1->width() - 1, label1->y());
		label2->show();
		QLabel* label3 = new QLabel(window);
		label3->setAlignment(Qt::AlignCenter);
		label3->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;");
		label3->resize(200, 25);
		label3->setText("Module Memory Size(bit)");
		label3->move(label2->x() + label2->width() - 1, label2->y());
		label3->show();
		QLabel* label4 = new QLabel(window);
		label4->setAlignment(Qt::AlignCenter);
		label4->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;");
		label4->resize(202, 25);
		label4->setText("Module Memory Size(byte)");
		label4->move(label3->x() + label3->width() - 1, label3->y());
		label4->show();
		char buffer1[1024] = "";
		sprintf(buffer1, "%p", module_info.lpBaseOfDll);
		QLineEdit* line1 = new QLineEdit(window);
		line1->setReadOnly(true);
		line1->setAlignment(Qt::AlignCenter);
		line1->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; font-family: 微软雅黑;");
		line1->setText(buffer1);
		line1->resize(200, 25);
		line1->move(0, label1->y() + label1->height() - 1);
		line1->show();
		char buffer2[1024] = "";
		sprintf(buffer2, "%p", module_info.EntryPoint);
		QLineEdit* line2 = new QLineEdit(window);
		line2->setReadOnly(true);
		line2->setAlignment(Qt::AlignCenter);
		line2->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; font-family: 微软雅黑;");
		line2->setText(buffer2);
		line2->resize(200, 25);
		line2->move(line1->x() + line1->width() - 1, label2->y() + label2->height() - 1);
		line2->show();
		char buffer3[1024] = "";
		sprintf(buffer3, "%d", module_info.SizeOfImage * 8);
		QLineEdit* line3 = new QLineEdit(window);
		line3->setReadOnly(true);
		line3->setAlignment(Qt::AlignCenter);
		line3->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; font-family: 微软雅黑;");
		line3->setText(buffer3);
		line3->resize(200, 25);
		line3->move(line2->x() + line2->width() - 1, label3->y() + label3->height() - 1);
		line3->show();
		char buffer4[1024] = "";
		sprintf(buffer4, "%d", module_info.SizeOfImage);
		QLineEdit* line4 = new QLineEdit(window);
		line4->setReadOnly(true);
		line4->setAlignment(Qt::AlignCenter);
		line4->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; font-family: 微软雅黑;");
		line4->setText(buffer4);
		line4->resize(202, 25);
		line4->move(line3->x() + line3->width() - 1, label4->y() + label4->height() - 1);
		line4->show();
		window->show();
	}
}

StorageModuleMemoryAddressLabel::StorageModuleMemoryAddressLabel(QWidget* parent) : QLineEdit(parent) {

};

void StorageModuleMemoryAddressLabel::GetModuleAssociateFile(char* associate_file) {
	int line_index = 0;
	for (int i = 0; i < form_line_edit_list.size(); i++) {
		if (this == form_line_edit_list.at(i)) {
			int cd_path = ((ShowMemoryAddressInfo*)this->parent())->GetCDPageNum();
			line_index = cd_path * 15 + i;
		}
	}
	strcpy(associate_file, (char*)memory_list[line_index + 1]);
};

void StorageModuleMemoryAddressLabel::mousePressEvent(QMouseEvent* mouse_event) {
	if (mouse_event->button() == Qt::MouseButton::RightButton) {
		QMenu* menu = new QMenu(NULL);
		MySearchModuleMemoryAction* show_detailed_infomation = new MySearchModuleMemoryAction(menu);
		QObject::connect(show_detailed_infomation, &QAction::triggered, show_detailed_infomation, &MySearchModuleMemoryAction::TriggeredResponse);
		QObject::connect(show_detailed_infomation, &MySearchModuleMemoryAction::GetModuleAssociateFile, this, &StorageModuleMemoryAddressLabel::GetModuleAssociateFile);
		QObject::connect(show_detailed_infomation, &MySearchModuleMemoryAction::GetAssociatedPID, ((ShowMemoryAddressInfo*)this->parent()), &ShowMemoryAddressInfo::GetAssociatePID);
		show_detailed_infomation->setText("Get Module Information");
		menu->addAction(show_detailed_infomation);
		menu->move(QCursor::pos().x(), QCursor::pos().y());
		menu->show();
	};
}

StorageProcessPIDInfoLabel::StorageProcessPIDInfoLabel(QWidget* parent) : QLineEdit(parent) {

};

bool StorageProcessPIDInfoLabel::SetProcessPID(int Process_PID) {
	this->process_pid = Process_PID;
	sprintf((char*)this->process_pid_str.c_str(), "%d", this->process_pid);
	this->setAlignment(Qt::AlignCenter);
	this->setReadOnly(true);
	this->setStyleSheet("border: 1px solid black; border-bottom-width: 0px");
	this->setText(this->process_pid_str.c_str());
	return true;
}

int StorageProcessPIDInfoLabel::GetProcessPID() {
	return this->process_pid;
}

void StorageProcessPIDInfoLabel::mousePressEvent(QMouseEvent* mouse_event) {
	if (mouse_event->button() == Qt::MouseButton::RightButton) {
		QMenu* menu = new MyMenu(NULL);
		MyKillPIDAction* kill_action = new MyKillPIDAction(NULL);
		QObject::connect(kill_action, &MyKillPIDAction::TriggeredGetProcessPID, this, &StorageProcessPIDInfoLabel::GetProcessPID);
		QObject::connect(kill_action, &MyKillPIDAction::triggered, kill_action, &MyKillPIDAction::TriggeredResponse);
		QObject::connect(kill_action, &MyKillPIDAction::triggered, (TaskManagerWindows*)this->parent()->parent()->parent(), &TaskManagerWindows::GetWindowHandle);
		kill_action->setText("Kill");
		MyShowPIDAction* show_pid_action = new MyShowPIDAction(NULL);
		QObject::connect(show_pid_action, &MyShowPIDAction::triggered, show_pid_action, &MyShowPIDAction::TriggeredResponse);
		QObject::connect(show_pid_action, &MyShowPIDAction::ShowPIDEvent, this, &StorageProcessPIDInfoLabel::ShowPID);
		if (this->status == true) {
			show_pid_action->setText("Lock");
		}
		else {
			show_pid_action->setText("Unlock");
			kill_action->setEnabled(false);
		}
		menu->addAction(kill_action);
		menu->addAction(show_pid_action);
		menu->move(QCursor().pos().x(), QCursor().pos().y());
		menu->show();
	}
}

bool StorageProcessPIDInfoLabel::ShowPID(QAction* action) {
	if (this->status == true) {
		this->status = false;
		char buffer[1024] = "";
		sprintf(buffer, "%d %s", this->process_pid, "(Lock)");
		for (int i = 0; i < ProcessInfoWidgetList.size(); i++) {
			if (this == ProcessInfoWidgetList.at(i)) {
				for (int j = 1; j < 4; j++) {
					ProcessInfoWidgetList.at(i + j)->setEnabled(false);
				}
			}
		}
		this->setText(buffer);
		return false;
	}
	else {
		this->status = true;
		this->setText(this->process_pid_str.c_str());
		for (int i = 0; i < ProcessInfoWidgetList.size(); i++) {
			if (this == ProcessInfoWidgetList.at(i)) {
				for (int j = 1; j < 4; j++) {
					ProcessInfoWidgetList.at(i + j)->setEnabled(true);
				}
			}
		}
		return true;
	}
}

StorageAssociateEXEInfoLabel::StorageAssociateEXEInfoLabel(QWidget* parent) : QLineEdit(parent) {

}

bool StorageAssociateEXEInfoLabel::SetAssociateEXE(string Associate_EXE) {
	this->associate_exe = Associate_EXE;
	this->setText(this->associate_exe.c_str());
	this->setAlignment(Qt::AlignCenter);
	this->setReadOnly(true);
	this->setStyleSheet("border: 1px solid black; border-bottom-width: 0px; border-left-width:0px;");
	return true;
}

StorageEXEFilePathInfoLabel::StorageEXEFilePathInfoLabel(QWidget* parent) : QLineEdit(parent) {

}

bool StorageEXEFilePathInfoLabel::SetEXEFilePath(string EXE_File_Path) {
	this->exe_file_path = EXE_File_Path;
	this->setText(this->exe_file_path.c_str());
	this->setAlignment(Qt::AlignLeft);
	this->setReadOnly(true);
	this->setStyleSheet("border: 1px solid black; border-bottom-width: 0px; border-left-width:0px;");
	return true;
}

string* StorageEXEFilePathInfoLabel::GetFilePath() {
	return &this->exe_file_path;
}

MySearchMemoryPIDAction::MySearchMemoryPIDAction(QWidget* parent) : QAction(parent) {

}

void MySearchMemoryPIDAction::TriggeredResponse() {
	StorageProcessMemoryAddressLabel* Memory_label = emit(StorageProcessMemoryAddressLabel*)this->GetProcessMemoryAddressLabelEvent();
	int Process_PID;
	for (int i = 0; i < ProcessInfoWidgetList.size(); i++) {
		if (ProcessInfoWidgetList.at(i) == Memory_label) {
			Process_PID = ((StorageProcessPIDInfoLabel*)ProcessInfoWidgetList.at(i - 3))->GetProcessPID();
		}
	}
	TaskManagerWindows* window_handle = (TaskManagerWindows*)this->GetTaskManagerWindowsHandle();
	ShowMemoryAddressInfo* window = new ShowMemoryAddressInfo(window_handle);
	window->SetAssociatePID(Process_PID);
	window->InitializeWindows();
	window->InitializeWidgets();
	vector<HMODULE>* include_module_list = new vector<HMODULE>();
	vector<char*> include_module_name_ = {};
	vector<char*> include_file_path_ = {};
	if (!GetProcessIncludeModule(Process_PID, include_module_list)) {
		include_module_list->push_back(NULL);
		include_module_name_.push_back((char*)"(Can't Search)");
		include_file_path_.push_back((char*)"(Can't Search)");
	}
	else {
		for (int i = 0; i < include_module_list->size(); i++) {
			char* include_module_name = (char*)malloc(sizeof(char) * 2040);
			for (int i = 0; i < 2040; i++) {
				include_module_name[i] = NULL;
			}
			GetModuleName(Process_PID, include_module_list->at(i), include_module_name, sizeof(char) * 2040);
			if (strlen(include_module_name) == 0) {
				strcpy(include_module_name, "(UNKNOW)");
			}
			include_module_name_.push_back(include_module_name);
		}
		for (int i = 0; i < include_module_list->size(); i++) {
			char* include_file_path = (char*)malloc(sizeof(char) * 6030);
			for (int i = 0; i < 6030; i++) {
				include_file_path[i] = NULL;
			}
			GetModulePath(Process_PID, include_module_list->at(i), include_file_path, sizeof(char) * 6030);
			if (strlen(include_file_path) == 0) {
				strcpy(include_file_path, "(UNKNOW)");
			}
			include_file_path_.push_back(include_file_path);
		}
	};
	for (int i = 0; i < include_module_list->size(); i++) {
		window->AddLine(include_module_list->at(i), include_module_name_[i], include_file_path_[i]);
	};
	window->TrunToPage(0);
	window->show();
}

StorageProcessMemoryAddressLabel::StorageProcessMemoryAddressLabel(QWidget* parent) : QLineEdit(parent) {

}

bool StorageProcessMemoryAddressLabel::SetProcessMemory(void* process_memory_address) {
	this->process_memory_address = process_memory_address;
	sprintf((char*)this->process_memory_address_str.c_str(), "%p", process_memory_address);
	this->setText(this->process_memory_address_str.c_str());
	this->setAlignment(Qt::AlignCenter);
	this->setReadOnly(true);
	this->setStyleSheet("border: 1px solid black; border-bottom-width: 0px; border-left-width:0px;");
	return true;
}

void StorageProcessMemoryAddressLabel::mousePressEvent(QMouseEvent* mouse_event) {
	if (mouse_event->button() == Qt::RightButton) {
		QMenu* menu = new QMenu(NULL);
		MySearchMemoryPIDAction* search_memory_address = new MySearchMemoryPIDAction(NULL);
		QObject::connect(search_memory_address, &MySearchMemoryPIDAction::GetProcessMemoryAddressLabelEvent, this, &StorageProcessMemoryAddressLabel::GetProcessMemoryAddressLabel);
		QObject::connect(search_memory_address, &MySearchMemoryPIDAction::triggered, search_memory_address, &MySearchMemoryPIDAction::TriggeredResponse);
		QObject::connect(search_memory_address, &MySearchMemoryPIDAction::GetTaskManagerWindowsHandle, (TaskManagerWindows*)this->parent()->parent()->parent(), &TaskManagerWindows::GetWindowHandle);
		search_memory_address->setText("Search Include Module");
		menu->addAction(search_memory_address);
		menu->move(QCursor::pos().x(), QCursor::pos().y());
		menu->show();
	}
	mouse_event->accept();
}

void* StorageProcessMemoryAddressLabel::GetProcessMemoryAddressLabel() {
	return this;
};

TaskManagerWindows::TaskManagerWindows(QWidget* parent) : QMainWindow(parent) {

};

bool TaskManagerWindows::InitializeWindows() {
	this->setWindowFlags(
		Qt::WindowType::WindowCloseButtonHint |
		Qt::WindowType::WindowMinMaxButtonsHint |
		Qt::WindowType::WindowMaximizeButtonHint
	);
	this->setWindowState(Qt::WindowState::WindowNoState);
	this->setWindowOpacity(0.95);
	this->setWindowModified(true);
	this->statusBar();
	this->statusTip();
	this->setWindowTitle("[*]Task Management Tool");
	this->resize(1200, 800); // Width: 800, Height: 600
	this->setMinimumSize(600, 200);
	this->setVisible(true);
	this->show();
	return true;
};

void TaskManagerWindows::VScrollBarValueChangeEvent(int value) {
	if (!IsEnough) {
		return;
	}
	for (int i = show_label_count_start; i < show_label_count_end; i++) {
		if (i > ProcessInfoWidgetList.size() - 5) {
			break;
		}
		ProcessInfoWidgetList[i * 4]->setParent(NULL);
		ProcessInfoWidgetList[i * 4]->setVisible(false);
		ProcessInfoWidgetList[i * 4 + 1]->setParent(NULL);
		ProcessInfoWidgetList[i * 4 + 1]->setVisible(false);
		ProcessInfoWidgetList[i * 4 + 2]->setParent(NULL);
		ProcessInfoWidgetList[i * 4 + 2]->setVisible(false);
		ProcessInfoWidgetList[i * 4 + 3]->setParent(NULL);
		ProcessInfoWidgetList[i * 4 + 3]->setVisible(false);
		ProcessInfoWidgetList[i * 4]->setStyleSheet("border-bottom-width: 0px;");
		ProcessInfoWidgetList[i * 4 + 1]->setStyleSheet("border-bottom-width: 0px;");
		ProcessInfoWidgetList[i * 4 + 2]->setStyleSheet("border-bottom-width: 0px;");
		ProcessInfoWidgetList[i * 4 + 3]->setStyleSheet("border-bottom-width: 0px;");
	};
	int index = 0;
	for (int i = value; i < value + (int)(this->ShowProcessInfoWidget->height() / 25); i++) {
		if (i > ProcessInfoWidgetList.size() - 5) {
			break;
		}
		ProcessInfoWidgetList[i * 4]->setParent(this->StorageProcessInfoWidget);
		ProcessInfoWidgetList[i * 4]->resize(this->process_PID->width() + 1, 25);
		ProcessInfoWidgetList[i * 4]->move(0, index * 25);
		ProcessInfoWidgetList[i * 4]->show();
		ProcessInfoWidgetList[i * 4 + 1]->setParent(this->StorageProcessInfoWidget);
		ProcessInfoWidgetList[i * 4 + 1]->resize(this->process_associate_exe->width() + 1, 25);
		ProcessInfoWidgetList[i * 4 + 1]->move(ProcessInfoWidgetList[i * 4]->x() + ProcessInfoWidgetList[i * 4]->width() - 1, index * 25);
		ProcessInfoWidgetList[i * 4 + 1]->show();
		ProcessInfoWidgetList[i * 4 + 2]->setParent(this->StorageProcessInfoWidget);
		ProcessInfoWidgetList[i * 4 + 2]->resize(this->exe_file_path->width() + 1, 25);
		ProcessInfoWidgetList[i * 4 + 2]->move(ProcessInfoWidgetList[i * 4 + 1]->x() + ProcessInfoWidgetList[i * 4 + 1]->width() - 1, index * 25);
		ProcessInfoWidgetList[i * 4 + 2]->show();
		ProcessInfoWidgetList[i * 4 + 3]->setParent(this->StorageProcessInfoWidget);
		ProcessInfoWidgetList[i * 4 + 3]->resize(this->process_memory_address->width() + 1, 25);
		ProcessInfoWidgetList[i * 4 + 3]->move(ProcessInfoWidgetList[i * 4 + 2]->x() + ProcessInfoWidgetList[i * 4 + 2]->width() - 1, index * 25);
		ProcessInfoWidgetList[i * 4 + 3]->show();
		if (i == value + (int)((height() - 54) / 25 - 1)) {
			ProcessInfoWidgetList[i * 4]->setStyleSheet("border-bottom-width: 1px;");
			ProcessInfoWidgetList[i * 4 + 1]->setStyleSheet("border-bottom-width: 1px;");
			ProcessInfoWidgetList[i * 4 + 2]->setStyleSheet("border-bottom-width: 1px;");
			ProcessInfoWidgetList[i * 4 + 3]->setStyleSheet("border-bottom-width: 1px;");
		}
		index += 1;
	}
	show_label_count_start = value;
	show_label_count_end = value + (int)(height() - 54) / 25;
};

int TaskManagerWindows::GetVScrollBarValue() {
	return this->VScrollBar->value();
}


bool TaskManagerWindows::InitializeWidgets() {
	if (this->IsInitializeWidget == false) {
		this->VScrollBar = new QScrollBar(this);
		this->VScrollBar->setValue(0);
		connect(this->VScrollBar, &QScrollBar::valueChanged, this, &TaskManagerWindows::VScrollBarValueChangeEvent);
		this->ShowProcessInfoWidget = new QWidget(this);
		this->StorageProcessInfoWidget = new QWidget(this->ShowProcessInfoWidget);
		this->process_PID = new QLabel(this);
		this->process_associate_exe = new QLabel(this);
		this->exe_file_path = new QLabel(this);
		this->process_memory_address = new QLabel(this);
		this->IsInitializeWidget = true;
		return true;
	}
	else {
		return false;
	}
};

bool TaskManagerWindows::PutWidgetsInWidget() {
	if (IsInitializeWidget == true) {
		this->VScrollBar->setOrientation(Qt::Vertical);
		this->VScrollBar->resize(25, this->size().height() - 54);
		this->VScrollBar->setValue(this->VScrollBar->value());
		this->VScrollBar->setEnabled(true);
		this->VScrollBar->move(0, 40);
		this->VScrollBar->show();
		this->ShowProcessInfoWidget->resize(this->size().width() - 28, height() - 54);
		this->VScrollBar->setMinimum(0);
		this->VScrollBar->setMaximum(thread_number - (int)(this->ShowProcessInfoWidget->height() / 25));
		this->ShowProcessInfoWidget->setStyleSheet("border: 1px solid black;font-family: 微软雅黑;");
		this->ShowProcessInfoWidget->move(25, 40);
		this->ShowProcessInfoWidget->show();
		this->StorageProcessInfoWidget->resize(this->ShowProcessInfoWidget->width(), this->ShowProcessInfoWidget->height());
		this->StorageProcessInfoWidget->move(0, 0);
		this->StorageProcessInfoWidget->show();
		this->process_PID->setText("PID");
		this->process_PID->setAlignment(Qt::AlignCenter);
		this->process_PID->resize((int)((this->size().width() - 26) * 1 / 10), 25);
		this->process_PID->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;border-bottom-width: 0px; border-right-width:0px;");
		this->process_PID->move(this->ShowProcessInfoWidget->x(), this->ShowProcessInfoWidget->y() - 25);
		this->process_PID->show();
		this->process_associate_exe->setText("Associate EXE");
		this->process_associate_exe->setAlignment(Qt::AlignCenter);
		this->process_associate_exe->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;border-bottom-width: 0px;border-right-width:0px;");
		this->process_associate_exe->resize((int)((this->size().width() - 26) * 2 / 10), 25);
		this->process_associate_exe->move(this->process_PID->x() + this->process_PID->width(), this->process_PID->y());
		this->process_associate_exe->show();
		this->exe_file_path->setText("Associate EXE File Path");
		this->exe_file_path->setAlignment(Qt::AlignCenter);
		this->exe_file_path->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;border-bottom-width: 0px;border-right-width:0px;");
		this->exe_file_path->resize((int)((this->size().width() - 26) * 5 / 10), 25);
		this->exe_file_path->move(this->process_associate_exe->x() + this->process_associate_exe->width(), this->process_associate_exe->y());
		this->exe_file_path->show();
		this->process_memory_address->setText("Process Memory Address");
		this->process_memory_address->setAlignment(Qt::AlignCenter);
		this->process_memory_address->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;border-bottom-width: 0px;");
		this->process_memory_address->resize((int)((this->size().width() - 26) * 2 / 10), 25);
		this->process_memory_address->move(this->exe_file_path->x() + this->exe_file_path->width(), this->exe_file_path->y());
		this->process_memory_address->show();
		this->VScrollBarValueChangeEvent(this->GetVScrollBarValue());
		return true;
	}
	else {
		return false;
	}
};

bool TaskManagerWindows::FilterProcess(int PID) {
	WaitForSingleObject(filter_semaphore, INFINITE);
	for (int i = 0; i < ProcessPIDList.size(); i++) {
		if (ProcessPIDList[i] == PID) {
			ProcessPIDList.erase(ProcessPIDList.begin() + i);
			break;
		}
	};
	for (int i = 0; i < ProcessInfoWidgetList.size(); i++) {
		char process_[2048] = "";
		QString process_info = ProcessInfoWidgetList.at(i)->text();
		for (int j = 0; j < process_info.size(); j++) {
			process_[j] = process_info.at(j).toLatin1();
		}
		char process_PID[12] = "";
		sprintf(process_PID, "%d", PID);
		if (strcmp(process_, process_PID) == 0) {
			for (int k = 0; k < 4; k++) {
				ProcessInfoWidgetList.erase(ProcessInfoWidgetList.begin() + i);
			}
		}
	}
	char buffer[1024] = "";
	sprintf(buffer, "Task Management Tool [Process Number: %d]", thread_number);
	this->setWindowTitle(buffer);
	this->thread_number -= 1;
	this->VScrollBar->setMaximum(thread_number - (int)(this->ShowProcessInfoWidget->height() / 25) - 1);
	this->VScrollBar->setValue(this->VScrollBar->value() - 1);
	ReleaseSemaphore(filter_semaphore, 1, NULL);
	return true;
};

void* TaskManagerWindows::GetWindowHandle() {
	return this;
}

void TaskManagerWindows::AddProcessInfoLine(int process_pid, char* exe_file_path, void* process_memory_address) {
	for (int i = 0; i < ProcessPIDList.size(); i++) {
		if (ProcessPIDList[i] == process_pid) {
			return;
		}
	};

	if (process_memory_address == NULL) {
		return;
	}
	StorageProcessPIDInfoLabel* process_pid_label = new StorageProcessPIDInfoLabel(this);
	process_pid_label->SetProcessPID(process_pid);

	StorageAssociateEXEInfoLabel* associate_exe_info_label = new StorageAssociateEXEInfoLabel(this);
	string associate_exe(exe_file_path);
	string associate_exe_buffer = "";
	associate_exe_buffer.resize(300);
	int index = 0;
	for (int i = associate_exe.find_last_of("\\") + 1; i < associate_exe.length(); i++) {
		associate_exe_buffer[index] = associate_exe[i];
		index += 1;
	}
	associate_exe_info_label->SetAssociateEXE(associate_exe_buffer);


	StorageEXEFilePathInfoLabel* exe_file_path_label = new StorageEXEFilePathInfoLabel(this);
	exe_file_path_label->SetEXEFilePath(exe_file_path);

	StorageProcessMemoryAddressLabel* process_memory_address_ = new StorageProcessMemoryAddressLabel(this);
	process_memory_address_->SetProcessMemory(process_memory_address);
	WaitForSingleObject(filter_semaphore, INFINITE);
	ProcessInfoWidgetList.push_back(process_pid_label);
	ProcessInfoWidgetList.push_back(associate_exe_info_label);
	ProcessInfoWidgetList.push_back(exe_file_path_label);
	ProcessInfoWidgetList.push_back(process_memory_address_);
	ProcessPIDList.push_back(process_pid);
	ReleaseSemaphore(filter_semaphore, 1, NULL);
	thread_number += 1;
	if (thread_number >= 50) {
		if (IsEnough == false) {
			IsEnough = true;
			this->PutWidgetsInWidget();
		}
	}
	char buffer[1024] = "";
	sprintf(buffer, "Task Management Tool [Process Number: %d]", thread_number);
	this->setWindowTitle(buffer);
	this->VScrollBar->setMaximum(thread_number - (int)(this->ShowProcessInfoWidget->height() / 25) - 1);
	FilterProcessThread* thread_ = new FilterProcessThread((QWidget*)this->parent());
	thread_->SetFilterPID(process_pid);
	connect(thread_, &FilterProcessThread::FilterProcessEvent, this, &TaskManagerWindows::FilterProcess);
	thread_->start();
};

bool TaskManagerWindows::ReleaseWidgets() {
	if (this->IsInitializeWidget == true) {
		this->VScrollBar->deleteLater();
		this->ShowProcessInfoWidget->deleteLater();
		this->StorageProcessInfoWidget->deleteLater();
		this->process_PID->deleteLater();
		this->process_associate_exe->deleteLater();
		this->exe_file_path->deleteLater();
		this->process_memory_address->deleteLater();
		for (int i = 0; i < ProcessInfoWidgetList.size(); i++) {
			ProcessInfoWidgetList[i]->deleteLater();
		}
		this->IsInitializeWidget = false;
		return true;
	}
	else {
		return false;
	}
};

void TaskManagerWindows::resizeEvent(QResizeEvent* event) {
	QMainWindow::resizeEvent(event);
	if (this->IsInitializeWidget == true) {
		TaskManagerWindows::PutWidgetsInWidget();
	}
}

ShowMemoryAddressInfo::ShowMemoryAddressInfo(QWidget* parent) : QMainWindow(parent) {

}

bool ShowMemoryAddressInfo::InitializeWindows() {
	this->setWindowState(Qt::WindowState::WindowNoState);
	this->setFixedSize(900, 400);
	this->setWindowTitle("[*]Memory Address Info [Page: No.1]");
	this->setWindowModality(Qt::WindowModality::WindowModal);
	this->setWindowModified(true);
	this->setWindowOpacity(0.98);
	return true;
}

bool ShowMemoryAddressInfo::InitializeWidgets() {
	QPushButton* button1 = new QPushButton(this);
	QObject::connect(button1, &QPushButton::pressed, this, &ShowMemoryAddressInfo::TurnBack);
	button1->setText("<");
	button1->resize(29, this->size().height() - 21);
	button1->move(0, 21);
	button1->show();
	QPushButton* button2 = new QPushButton(this);
	QObject::connect(button2, &QPushButton::pressed, this, &ShowMemoryAddressInfo::TurnFront);
	button2->setText(">");
	button2->resize(29, this->size().height() - 25);
	button2->move(this->width() - button2->width(), 25);
	button2->show();
	QLabel* label1 = new QLabel(this);
	label1->setText("Module Memory Address");
	label1->resize(200, 25);
	label1->setStyleSheet("border: 1px solid black; font-family: 微软雅黑;border-bottom-width: 0px;");
	label1->setAlignment(Qt::AlignCenter);
	label1->move(30, 25 - label1->height());
	label1->show();
	QLabel* label2 = new QLabel(this);
	label2->setText("Module Name");
	label2->resize(200, 25);
	label2->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; border-left-width: 0px;border-bottom-width: 0px;");
	label2->setAlignment(Qt::AlignCenter);
	label2->move(label1->x() + label1->width(), 25 - label2->height());
	label2->show();
	QLabel* label3 = new QLabel(this);
	label3->setText("Associate DLL File Path");
	label3->resize(this->width() - 460, 25);
	label3->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; border-left-width: 0px;border-bottom-width: 0px;");
	label3->setAlignment(Qt::AlignCenter);
	label3->move(label2->x() + label2->width(), 25 - label3->height());
	label3->show();
	for (int i = 1; i < (int)(this->height() / 25); i++) {
		StorageModuleMemoryAddressLabel* line_edit = new StorageModuleMemoryAddressLabel(this);
		line_edit->resize(200, 25);
		line_edit->move(30, 25 * i);
		line_edit->setReadOnly(true);
		line_edit->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; border-bottom-width: 0px;");
		line_edit->show();
		QLineEdit* line_edit2 = new QLineEdit(this);
		line_edit2->resize(200, 25);
		line_edit2->move(line_edit->x() + line_edit->width(), 25 * i);
		line_edit2->setReadOnly(true);
		line_edit2->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; border-bottom-width: 0px; border-left-width: 0px");
		line_edit2->show();
		QLineEdit* line_edit3 = new QLineEdit(this);
		line_edit3->resize(this->width() - 460, 25);
		line_edit3->move(line_edit2->x() + line_edit2->width(), 25 * i);
		line_edit3->setReadOnly(true);
		line_edit3->setStyleSheet("border: 1px solid black; font-family: 微软雅黑; border-bottom-width: 0px; border-left-width: 0px");
		line_edit3->show();
		form_line_edit_list.push_back(line_edit);
		form_line_edit_list.push_back(line_edit2);
		form_line_edit_list.push_back(line_edit3);
	}
	return true;
}

bool ShowMemoryAddressInfo::AddLine(void* module_memory_address, char* module_name, char* module_DLL_file_path) {
	char* module_memory_address_ = (char*)malloc(sizeof(char) * 25);
	sprintf(module_memory_address_, "%p", module_memory_address);
	memory_list.push_back(module_memory_address_);
	memory_list.push_back(module_name);
	memory_list.push_back(module_DLL_file_path);
	this->Column += 1;
	this->Page_Num = (int)(this->Column / (int)(form_line_edit_list.size() / 3));
	return true;
};

bool ShowMemoryAddressInfo::TrunToPage(int page_num) {
	if (page_num < 0 || page_num > this->Page_Num) {
		return false;
	};
	int start_line = page_num * (int)(form_line_edit_list.size() / 3);
	int end_line = start_line + (int)(form_line_edit_list.size() / 3);
	int index = 0;
	for (int i = start_line; i < end_line; i++) {
		if (i * 3 + 2 > memory_list.size() - 1) {
			break;
		}
		else {
			form_line_edit_list[index * 3]->setText((char*)memory_list[i * 3]);
			form_line_edit_list[index * 3 + 1]->setText((char*)memory_list[i * 3 + 1]);
			form_line_edit_list[index * 3 + 2]->setText((char*)memory_list[i * 3 + 2]);
			form_line_edit_list[index * 3]->setAlignment(Qt::AlignCenter);
		}
		index += 1;
	}
	return true;
};

void ShowMemoryAddressInfo::TurnBack() {
	if (this->TrunToPage(this->cd_page - 1)) {
		this->cd_page -= 1;
	};
	char new_title[1024] = "";
	sprintf(new_title, "[*]Memory Address Info [Page: No.%d]", this->cd_page + 1);
	this->setWindowTitle(new_title);
}

void ShowMemoryAddressInfo::TurnFront() {
	if (this->TrunToPage(this->cd_page + 1)) {
		this->cd_page += 1;
	}
	char new_title[1024] = "";
	sprintf(new_title, "[*]Memory Address Info [Page: No.%d]", this->cd_page + 1);
	this->setWindowTitle(new_title);
};

bool ShowMemoryAddressInfo::SetAssociatePID(int PID) {
	this->associate_pid = PID;
	return true;
};

int ShowMemoryAddressInfo::GetCDPageNum() {
	return this->cd_page;
}

int ShowMemoryAddressInfo::GetAssociatePID() {
	return this->associate_pid;
};

AddProcessInfoLineThread::AddProcessInfoLineThread(QWidget* parent) : QThread(parent) {

}

void AddProcessInfoLineThread::run() {

	while (true) {
		vector<int> process_list;
		GetProcessInSystem(&process_list);
		for (int i = 0; i < process_list.size() / sizeof(int); i++) {
			int pid = process_list.at(i);
			if (pid == -1) {
				break;
			}
			HANDLE process_memory = GetProcessMemoryAddressByPID(pid);
			if (process_memory == NULL) {
				continue;
			}
			char* buffer = (char*)malloc(sizeof(char) * 2048);
			GetProcessAssociateFilePath(process_memory, buffer, 2048);
			emit AddProcessNewLineInfoEvent(pid, buffer, GetProcessMemoryAddressByPID(pid));
		}
		Sleep(5000);
	}
}

FilterProcessThread::FilterProcessThread(QWidget* parent) : QThread(parent) {

}

void FilterProcessThread::SetFilterPID(int process_PID) {
	this->listen_PID = process_PID;
}
void FilterProcessThread::run() {
	HANDLE process_handle = OpenProcess(PROCESS_QUERY_INFORMATION, FALSE, this->listen_PID);
	while (true) {
		Sleep(1);
		DWORD exit_code;
		if (GetExitCodeProcess(process_handle, &exit_code)) {
			if (exit_code == STILL_ACTIVE) {
				continue;
			}
			else {
				qDebug() << exit_code << endl;
			}
		};
		bool status = emit FilterProcessEvent(this->listen_PID);
		break;
	};
}