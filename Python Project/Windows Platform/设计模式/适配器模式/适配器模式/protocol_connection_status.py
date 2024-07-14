

class BaseProtocolConnectionStatus(object):
    FINE = 1
    LOSE = 0
    DONE = -1


class FTPProtocolConnectionStatus(BaseProtocolConnectionStatus):
    pass


class IMAPProtocolConnectionStatus(BaseProtocolConnectionStatus):
    pass

class POPProtocolConnectionStatus(BaseProtocolConnectionStatus):
    pass