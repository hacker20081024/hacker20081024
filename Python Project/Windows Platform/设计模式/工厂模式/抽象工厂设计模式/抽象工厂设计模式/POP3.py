from base_connection import BaseConnection
from protocol_status import POP3ProtocolStatus

from poplib import POP3,POP3_SSL

import typing

class POP3Connection(BaseConnection):
    
    def __init__(self,server_host:str,timeout: int=30,is_encryption: bool=True):
        super(POP3Connection,self).__init__(server_host,110 if is_encryption == True else 995)
        self.is_encryption = is_encryption
        self.timeout = timeout
        self.connection,self.status = self.__connect_to_server()
        self.is_connect_ = (True if self.connection is not None else False)
        
    def is_connect(self) -> typing.Tuple[bool | int]:
        return self.is_connect_
    
    def close_connect(self) -> bool:
        if self.connection is not None:
           self.connection.close()
           return True
        else:
           return False
    
    def __connect_to_server(self) -> typing.Tuple[POP3 | POP3_SSL | int | None]:
        if self.is_connect_ == True:
           return (None,POP3ProtocolStatus.FINE,)
        else: 
           try:
             if self.is_encryption:
                return (POP3(self.server_host,timeout=self.timeout),POP3ProtocolStatus.FINE,)
             else:
                return (POP3_SSL(self.server_host,timeout=self.timeout),POP3ProtocolStatus.FINE,)
           except:
             return (None,POP3ProtocolStatus.FAILED,)  
         
__all__ = ["POP3Connection"]      