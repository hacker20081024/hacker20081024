from base_protocol_connection import BaseProtocolConnection
from protocol_connection_status import IMAPProtocolConnectionStatus

from imapclient import IMAPClient
import typing

class IMAPConnection(BaseProtocolConnection):
    
    def __init__(self,host:str,use_ssl:bool = True,timeout: int = 30):
        if use_ssl is True:
           super(IMAPConnection,self).__init__(host,993,timeout)
        else:
           super(IMAPConnection,self).__init__(host,143,timeout)
        self.use_ssl = use_ssl
        imap_connection,imap_connection_status = self.connect_to_imap_server()
        self.connection = imap_connection
        self.connect_status = imap_connection_status
        self.has_connect = (True if self.connect_status in [IMAPProtocolConnectionStatus.FINE,IMAPProtocolConnectionStatus.DONE] else False)
        
    def connect_to_imap_server(self) -> typing.Tuple[typing.Union[IMAPClient,None,int]]: 
        if self.has_connect is False:
           try:
             return (IMAPClient(host=self.host,port=self.port,ssl=self.use_ssl,timeout=self.timeout),IMAPProtocolConnectionStatus.FINE)
           except Exception:
             return (None,IMAPProtocolConnectionStatus.LOSE)
        else:
           return (None,IMAPProtocolConnectionStatus.DONE)
        
    def is_connect_to_imap_server(self) -> typing.Tuple[bool,int]:
        return self.has_connect,self.connect_status 
    
    def close_imap_connection(self) -> bool:
        if self.connection is not None:
           self.connection = None
           self.has_connect = False
           self.connect_status = 0
           return True
        else:
           return False
