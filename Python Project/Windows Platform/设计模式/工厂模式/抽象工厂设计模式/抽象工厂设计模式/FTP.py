from base_connection import BaseConnection
from protocol_status import FTPProtocolStatus

from ftplib import FTP

import typing

class FTPConnection(BaseConnection):    

    def __init__(self,server_host: str,username: str, password: str,timeout: int = 30):
        super(FTPConnection,self).__init__(server_host,21)
        self.username   = username
        self.password   = password
        self.timeout    = timeout
        self.connection, self.status = self.__connect_to_server()
        self.is_connect_ = True if self.connection is not None else False
       
    def is_connect(self) -> typing.Tuple[typing.Union[None,int]]:
        return (self.is_connect_,self.status)
    
    def close_connect(self) -> bool:
        if self.connection is not None:
           self.connection.close()
           return True
        else:
           return False
        
    def __connect_to_server(self) -> typing.Tuple[typing.Union[FTP,int,None]]:
        if self.is_connect_ == True:
           return (None,FTPProtocolStatus.FINE,)
        else:  
          try:
            return (FTP(self.server_host,self.username,self.password,timeout=self.timeout),FTPProtocolStatus.FINE,)
          except:
            return (None,FTPProtocolStatus.FAILED,)  

__all__ = ["FTPConnection"]  