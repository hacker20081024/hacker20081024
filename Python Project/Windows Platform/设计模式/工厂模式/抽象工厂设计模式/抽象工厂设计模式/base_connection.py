import typing


class BaseConnection(object):
    def __init__(self,server_host: str,server_port: int):
        self.server_host = server_host
        self.server_port = server_port
        self.is_connect_  = False
    
    def is_connect(self) -> typing.Tuple[typing.Union[bool,int]]:
        pass

    def close_connect(self) -> bool:
        pass
    
    def __connect_to_server(self) -> typing.Tuple[typing.Union[typing.Any,int,None]]:
        pass
    
__all__ = ["BaseConnection"]