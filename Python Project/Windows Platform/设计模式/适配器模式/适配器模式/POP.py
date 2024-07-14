from base_protocol_connection import BaseProtocolConnection
from protocol_connection_status import POPProtocolConnectionStatus

from poplib import POP3,POP3_SSL
import typing


class POPConnection(BaseProtocolConnection):
    
    def __init__(self,host:str,use_ssl:bool=True,timeout:int=30):
        if use_ssl:
           super(POPConnection,self).__init__(host,995,timeout)
        else:
           super(POPConnection,self).__init__(host,110,timeout) 
        self.use_ssl = use_ssl
        imap_connection,imap_connection_status = self.connect_to_pop_server()
        self.connection = imap_connection
        self.connect_status = imap_connection_status
        self.has_connect = (True if imap_connection_status in [POPProtocolConnectionStatus.FINE,POPProtocolConnectionStatus.DONE] else False)
        
    def connect_to_pop_server(self) -> typing.Tuple[typing.Union[POP3,POP3_SSL,None,int]]:
        if self.has_connect is False:
           try:
              if self.use_ssl:
                 return (POP3_SSL(self.host,timeout=self.timeout),POPProtocolConnectionStatus.FINE)
              else:
                 return (POP3(self.host,timeout=self.timeout),POPProtocolConnectionStatus.FINE)
           except Exception:
              return (None,POPProtocolConnectionStatus.LOSE)
        else:
           return (None,POPProtocolConnectionStatus.DONE)
    
    def is_connect_to_pop_server(self) -> typing.Tuple[typing.Union[bool,int]]:
        return self.has_connect,self.connect_status
    
    def close_pop_connection(self) -> bool:
        if self.connection is not None:
           self.connection.close()
           self.connection = None
           self.has_connect = False
           self.connect_status = 0
           return True
        else:
           return False