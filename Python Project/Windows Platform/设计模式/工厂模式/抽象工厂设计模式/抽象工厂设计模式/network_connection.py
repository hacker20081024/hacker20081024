from FTP import FTPConnection
from POP3 import POP3Connection

class ApplicationLayerConnection(object):
    
    def __init__(self):
        pass
    
    def FTP_Connection(self,server_host: str,username: str, password: str,timeout: int = 30) -> FTPConnection:
        # 注意: 抽象工厂设计模式中,工厂类的函数不能是静态的
        return FTPConnection(server_host,username,password,timeout)
    
    def POP3_Connection(self,server_host:str,timeout: int=30,is_encryption: bool=True) -> POP3Connection:
        # 注意: 抽象工厂设计模式中,工厂类的函数不能是静态的
        return POP3Connection(server_host,timeout,is_encryption)