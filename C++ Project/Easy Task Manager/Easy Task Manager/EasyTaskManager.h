#pragma once
#include <iostream>
#include <Windows.h>
#include <Psapi.h>
#include <QtWidgets/qmainwindow.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qscrollbar.h>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qaction.h>
#include <QtWidgets/qmenu.h>
#include <QtWidgets/qmessagebox.h>
#include <qthread.h>
#include <qdebug.h>
#include <qevent.h>
#include <qcursor.h>
#include "ui_EasyTaskManager.h"
using namespace std;

class TaskManagerWindows : public QMainWindow {
	Q_OBJECT
public slots:
	void AddProcessInfoLine(int process_pid, char* associate_file, void* process_memory_address);
	bool FilterProcess(int PID);
	void* GetWindowHandle();
public:
	TaskManagerWindows(QWidget* parent);
	bool InitializeWindows();
	bool InitializeWidgets();
	bool PutWidgetsInWidget();
	bool ReleaseWidgets();
	void VScrollBarValueChangeEvent(int value);
	int GetVScrollBarValue();
protected:
	void resizeEvent(QResizeEvent* event) override;
private:
	QLabel* process_PID;
	QLabel* process_associate_exe;
	QLabel* exe_file_path;
	QLabel* process_memory_address;
	QScrollBar* VScrollBar;
	QWidget* ShowProcessInfoWidget;
	QWidget* StorageProcessInfoWidget;
	bool IsInitializeWidget = false;
	int show_label_count_start = 0;
	int show_label_count_end = 0;
	int thread_number = 0;
	bool IsEnough = false;
	vector<int> delete_pid_list = {};
};
class MyMenu : public QMenu {
public:
	MyMenu(QWidget* parent);
};

class ShowMemoryAddressInfo : public QMainWindow {
	Q_OBJECT;
public slots:
	void TurnBack();
	void TurnFront();
	int GetAssociatePID();
public:
	ShowMemoryAddressInfo(QWidget* parent);
	bool InitializeWindows();
	bool InitializeWidgets();
	bool AddLine(void* module_memory_address, char* module_name, char* module_DLL_file_path);
	bool TrunToPage(int page_num);
	bool SetAssociatePID(int pid);
	int GetCDPageNum();
private:
	bool is_initialized_widgets;
	int Column = 0;
	int Page_Num = 0;
	int cd_page = 0;
	int associate_pid;
};

class StorageModuleMemoryAddressLabel : public QLineEdit {
	Q_OBJECT;
public slots:
	void GetModuleAssociateFile(char* associate_file);
public:
	StorageModuleMemoryAddressLabel(QWidget* parent);
protected:
	void mousePressEvent(QMouseEvent* mouse_event) override;
private:
	int module_name;
};

class StorageProcessPIDInfoLabel : public QLineEdit {
	Q_OBJECT
public slots:
	bool ShowPID(QAction* action);
	int GetProcessPID();
public:
	StorageProcessPIDInfoLabel(QWidget* parent);
	bool SetProcessPID(int Process_PID);
protected:
	void mousePressEvent(QMouseEvent*) override;
private:
	int process_pid;
	string process_pid_str;
	bool status = true;
};

class MyShowPIDAction : public QAction {
	Q_OBJECT
signals:
	bool ShowPIDEvent(QAction* action);
public slots:
	void TriggeredResponse();
	void triggeredShowPIDEvent();
public:
	MyShowPIDAction(QWidget* parent);
};

class MyKillPIDAction : public QAction {
	Q_OBJECT
signals:
	int TriggeredGetProcessPID();
	void* GetTaskManagerWindows();
public:
	MyKillPIDAction(QWidget* parent);
	void TriggeredResponse();
};

class MySearchModuleMemoryAction : public QAction {
	Q_OBJECT
public slots:
	void TriggeredResponse();
signals:
	void GetModuleAssociateFile(char* associate_file);
	int GetAssociatedPID();
public:
	MySearchModuleMemoryAction(QWidget* parent);
};

class StorageAssociateEXEInfoLabel : public QLineEdit {
public:
	StorageAssociateEXEInfoLabel(QWidget* parent);
	bool SetAssociateEXE(string Associate_EXE);
private:
	string associate_exe;
};

class StorageEXEFilePathInfoLabel : public QLineEdit {
public:
	StorageEXEFilePathInfoLabel(QWidget* parent);
	bool SetEXEFilePath(string EXE_File_Path);
	string* GetFilePath();
private:
	string exe_file_path;
};

class MySearchMemoryPIDAction : public QAction {
	Q_OBJECT
public slots:
	void TriggeredResponse();
signals:
	void* GetProcessMemoryAddressLabelEvent();
	void* GetTaskManagerWindowsHandle();
public:
	MySearchMemoryPIDAction(QWidget* parent);
};

class StorageProcessMemoryAddressLabel : public QLineEdit {
	Q_OBJECT
public slots:
	void* GetProcessMemoryAddressLabel();
public:
	StorageProcessMemoryAddressLabel(QWidget* parent);
	bool SetProcessMemory(void* process_memory_address);
	void* process_memory_address;
protected:
	void mousePressEvent(QMouseEvent* mouse_event) override;
private:
	string process_memory_address_str;
};

class AddProcessInfoLineThread : public QThread {
	Q_OBJECT
signals:
	void AddProcessNewLineInfoEvent(int process_pid, char* associate_file, void* process_memory_address);
public:
	AddProcessInfoLineThread(QWidget* parent);
protected:
	void run() override;
};

class FilterProcessThread : public QThread {
	Q_OBJECT
signals:
	bool FilterProcessEvent(int PID);
public:
	FilterProcessThread(QWidget* parent);
	void SetFilterPID(int process_PID);
private:
	int listen_PID;
protected:
	void run() override;
};