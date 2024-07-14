class BaseProtocolConnection(object):
    
    def __init__(self,host: str,port: int,timeout: int) -> bool:
        self.host:str = host 
        self.port:int = port
        self.timeout:int = timeout
        self.connection: object = None
        self.has_connect: bool = False
        self.connect_status: int = 0
        
