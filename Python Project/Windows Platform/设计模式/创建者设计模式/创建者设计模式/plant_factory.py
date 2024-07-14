from plant import SunFlower, PeaShooter

class PlantFactory(object):
    def __init__(self):
        pass
    
    def create_sun_flower(self) -> SunFlower:
        return SunFlower()
    
    def create_pea_shooter(self) -> PeaShooter:
        return PeaShooter()