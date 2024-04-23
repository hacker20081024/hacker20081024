#include "EasyTaskManager.h"
#include <QtWidgets/QApplication>

int main(int argc, char* argv[])
{
	QApplication app(argc, argv);
	TaskManagerWindows* Windows = new TaskManagerWindows(NULL);
	AddProcessInfoLineThread* AddProcessInfoLineThreadObj = new AddProcessInfoLineThread(Windows);
	QObject::connect(AddProcessInfoLineThreadObj, &AddProcessInfoLineThread::AddProcessNewLineInfoEvent, Windows, &TaskManagerWindows::AddProcessInfoLine);
	Windows->InitializeWindows();
	Windows->InitializeWidgets();
	Windows->PutWidgetsInWidget();
	AddProcessInfoLineThreadObj->start();
	return app.exec();
}
