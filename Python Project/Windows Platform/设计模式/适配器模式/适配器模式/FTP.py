from base_protocol_connection import BaseProtocolConnection
from protocol_connection_status import FTPProtocolConnectionStatus

from ftplib import FTP
import typing

class FTPConnection(BaseProtocolConnection):
    
    def __init__(self,host: str,username: str,password: str,timeout: int = 30):
        super(FTPConnection,self).__init__(host,21,timeout)
        self.__username = username
        self.__password = password
        ftp_connection, ftp_connection_status = self.connect_to_ftp_server()
        self.connection = ftp_connection
        self.connect_status = ftp_connection_status
        self.has_connect = (True if self.connect_status in [FTPProtocolConnectionStatus.FINE,FTPProtocolConnectionStatus.DONE] else False)
          
    def connect_to_ftp_server(self) -> typing.Tuple[typing.Union[FTP,None,int]]:
        if self.has_connect is False:
           try:
             return (FTP(self.host,user=self.__username,passwd=self.__password,timeout=self.timeout),FTPProtocolConnectionStatus.FINE,)
           except Exception:
             return (None,FTPProtocolConnectionStatus.LOSE,)
        else:
           return (None,FTPProtocolConnectionStatus.DONE,)
        
    def is_connect_to_ftp_server(self) -> typing.Tuple[typing.Union[bool,int]]:
       return self.has_connect,self.connect_status
   
    def close_ftp_connection(self) -> bool:
       if self.connection is not None:
          self.connection.close()
          self.has_connect = True
          self.connect_status = 0
          return True
       else:
          return False