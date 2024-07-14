from FTP import FTPConnection
from POP import POPConnection
from IMAP import IMAPConnection

import typing

class ApplicationLayerConnectionFactory(object):
    
    def __init__(self):
        pass
    
    def create_ftp_connection(self,host: str,username: str,password: str,timeout: int = 30):
        return FTPConnection(host,username,password,timeout)
    
    def create_pop_connection(self,host:str,use_ssl:bool=True,timeout:int=30):
        return POPConnection(host,use_ssl,timeout)
    
    def create_imap_connection(self,host:str,use_ssl:bool = True,timeout: int = 30):
        return IMAPConnection(host,use_ssl,timeout)
    

class AdapterApplicationLayerConnection(object):
    
    def __init__(self,connection_obj: typing.Union[FTPConnection,POPConnection,IMAPConnection]):
        self.connection = connection_obj
        if hasattr(self.connection,"is_connect_to_ftp_server") and hasattr(self.connection,"close_ftp_connection"):
           self.is_connect = self.connection.is_connect_to_ftp_server 
           self.close_connect = self.connection.close_ftp_connection
        elif hasattr(self.connection,"is_connect_to_pop_server") and hasattr(self.connection,"close_pop_connection"):
           self.is_connect = connection_obj.is_connect_to_pop_server
           self.close_connect = connection_obj.close_pop_connection
        elif hasattr(self.connection,"is_connect_to_imap_server") and hasattr(self.connection,"close_imap_connection"):
            self.is_connect = connection_obj.is_connect_to_imap_server
            self.close_connect = connection_obj.close_imap_connection
        else:
            self.is_connect = None
            self.close_connect = None
        
    def __str__(self):
        adapter_class = str(self.__class__).replace("'","").split(" ")[1].rstrip(">").split(".")[-1]
        adapter_class_id = "0x"+hex(id(self)).upper().replace("0X","")
        adapter_class_associated_class = str(self.connection.__class__).replace("'","").split(" ")[1].rstrip(">").split(".")[-1]
        adapter_class_associated_class_id = "0x"+hex(id(self.connection)).upper().replace("0X","")
        return f"<{adapter_class} at {adapter_class_id} <connection: {adapter_class_associated_class} at {adapter_class_associated_class_id}>>"