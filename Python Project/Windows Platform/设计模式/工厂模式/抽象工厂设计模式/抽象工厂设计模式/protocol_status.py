class BaseProtocolStatus(object):
    FINE = 0
    FAILED = -1
    
class FTPProtocolStatus(BaseProtocolStatus):
    pass

class POP3ProtocolStatus(BaseProtocolStatus):
    pass

__all__ = ["BaseProtocolStatus","FTPProtocolStatus","POP3ProtocolStatus"]