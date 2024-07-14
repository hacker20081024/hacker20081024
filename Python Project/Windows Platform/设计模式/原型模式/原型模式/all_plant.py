from base_plant import BaseSunCreatorPlant
from base_plant import BaseShooterPlant
from base_plant import BaseProtectionPlant
from base_plant import BaseBombPlant
from base_plant import BaseMinePlant
from base_plant import BaseEatZombiePlant
from plant_attribute import ShooterPlantAttribute, ShooterPlantBulletAttribute
from plant_attribute_creator import *

class PeaShooter(BaseShooterPlant):
    
    def __init__(self):
        super(PeaShooter,self).__init__(BasePlantAttributeCreator(1,1,"","1","xxx.mp3","xxx.gif","xxx.mp3","xxx.gif","xxx.gif",(0,0,)),
                                        ShooterPlantAttributeCreator(20,[ShooterPlantBulletAttributeCreator(1,ShooterPlantBulletAttribute.BulletMoveWay.TO_RIGHT,"xxx.gif","xxx.gif","xxx.mp3",10,ShooterPlantBulletAttribute.BulletEffect.SLOW_DOWN_ZOMBIE_SPEED)],"xxx.gif","xxx.mp3","xxx.gif","xxx.mp3"))

class SunFlower(BaseSunCreatorPlant):
    
    def __init__(self):
        super(SunFlower,self).__init__(BasePlantAttributeCreator(1,1,"","1","xxx.mp3","xxx.gif","xxx.mp3","xxx.gif","xxx.gif",(0,0,)),
                                        SunCreatorPlantAttributeCreator(25,12,"xxx.gif","xxx.gif"))


class CherryBomb(BaseBombPlant):
    def __init__(self):
        super(CherryBomb,self).__init__(BasePlantAttributeCreator(1,1,"","1","xxx.mp3","xxx.gif","xxx.mp3","xxx.gif","xxx.gif",(0,0,)),
                                        BombPlantAttributeCreator(4500,[(1,1)],"xxx.gif","xxx.mp3","xxx.gif","xxx.png"))


class Nut(BaseProtectionPlant):
    def __init__(self):
        super(Nut,self).__init__(BasePlantAttributeCreator(1,1,"","1","xxx.mp3","xxx.gif","xxx.mp3","xxx.gif","xxx.gif",(0,0,)),
                                 ProtectionPlantAttributeCreator(1,"xxx.mp3",{1:"xxx.gif"},{1:"xxx.gif"}))


class PotatoMine(BaseMinePlant):
    def __init__(self):
        super(PotatoMine,self).__init__(BasePlantAttributeCreator(1,1,"","1","xxx.mp3","xxx.gif","xxx.mp3","xxx.gif","xxx.gif",(0,0,)),
                                        MinePlantAttributeCreator("xxx.png","xxx.mp3",[(1,1)],10,False,20,"xxx.gif","xxx.gif","xxx.mp3","xxx.gif"))


class ColdIcePeaShooter(BaseShooterPlant):
    def __init__(self):
        super(ColdIcePeaShooter,self).__init__(BasePlantAttributeCreator(1,1,"","1","xxx.mp3","xxx.gif","xxx.mp3","xxx.gif","xxx.gif",(0,0,)),
                                        ShooterPlantAttributeCreator(20,[ShooterPlantBulletAttributeCreator(1,ShooterPlantBulletAttribute.BulletMoveWay.TO_RIGHT,"xxx.gif","xxx.gif","xxx.mp3",10,ShooterPlantBulletAttribute.BulletEffect.SLOW_DOWN_ZOMBIE_SPEED)],"xxx.gif","xxx.mp3","xxx.gif","xxx.mp3"))


class BigMouthFlower(BaseEatZombiePlant):
    def __init__(self):
        super(BigMouthFlower,self).__init__(BasePlantAttributeCreator(1,1,"","1","xxx.mp3","xxx.gif","xxx.mp3","xxx.gif","xxx.gif",(0,0,)),
                                            EatZombiePlantAttributeCreator(20,"xxx.gif","xxx.mp3","xxx.gif","xxx.gif","xxx.gif"))


class DoubleShootPeaShooter(BaseShooterPlant):
    def __init__(self):
        super(DoubleShootPeaShooter,self).__init__(BasePlantAttributeCreator(1,1,"","1","xxx.mp3","xxx.gif","xxx.mp3","xxx.gif","xxx.gif",(0,0,)),
                                        ShooterPlantAttributeCreator(20,[ShooterPlantBulletAttributeCreator(1,ShooterPlantBulletAttribute.BulletMoveWay.TO_RIGHT,"xxx.gif","xxx.gif","xxx.mp3",10,ShooterPlantBulletAttribute.BulletEffect.SLOW_DOWN_ZOMBIE_SPEED),
                                                                         ShooterPlantBulletAttributeCreator(1,ShooterPlantBulletAttribute.BulletMoveWay.TO_RIGHT,"xxx.gif","xxx.gif","xxx.mp3",10,ShooterPlantBulletAttribute.BulletEffect.SLOW_DOWN_ZOMBIE_SPEED)],"xxx.gif","xxx.mp3","xxx.gif","xxx.mp3"))


__all__ = ["PeaShooter",
           "SunFlower",
           "CherryBomb",
           "Nut",
           "PotatoMine",
           "ColdIcePeaShooter",
           "BigMouthFlower",
           "DoubleShootPeaShooter"
          ]