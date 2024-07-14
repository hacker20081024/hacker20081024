import typing
from plant import *
from zombie import *

class PlantName(object):
    PEA_SHOOTER = "pea_shooter"
    DOUBLE_PEA_SHOOTER ="double_pea_shooter"
    COLD_ICE_PEA_SHOOTER = "cold_ice_pea_shooter"
    CRAKED_POD_PEA_SHOOTER = "craked_pod_pea_shooter"
    MACHINE_GUN_PEA_SHOOTER = "machine_gun_pea_shooter"
    CAT_TAIL = "cat_tail"
    CARAMBOLA = "carambola"

class NoFoundPlantClassException(Exception):
   def __init__(self,error_plant_name: str):
      self.error_plant_name = error_plant_name
      
   def __str__(self):
      return "No Found: \"" + self.error_plant_name + "\" this plant class"
  
   def __repr__(self) -> str:
      return self.__str__()

def __plant_factory(plant_name: str) -> typing.Union[PeaShooter,DoublePeaShooter,ColdIcePeaShooter,CrakedPodPeaShooter,MachineGunPeaShooter,CatTail,Carambola,None]:
    """
    根据植物名称创建植物实例。

    :param plant_name: 植物名称
    :return: 植物实例或 None
    """
    if plant_name == PlantName.PEA_SHOOTER:
       return PeaShooter()
    elif plant_name == PlantName.DOUBLE_PEA_SHOOTER:
       return DoublePeaShooter()
    elif plant_name == PlantName.COLD_ICE_PEA_SHOOTER:
       return ColdIcePeaShooter()
    elif plant_name == PlantName.CRAKED_POD_PEA_SHOOTER:
       return CrakedPodPeaShooter()
    elif plant_name == PlantName.MACHINE_GUN_PEA_SHOOTER:
       return MachineGunPeaShooter()
    elif plant_name == PlantName.CAT_TAIL:
       return CatTail()
    elif plant_name == PlantName.CARAMBOLA:
       return Carambola()
    else:
       raise NoFoundPlantClassException(plant_name)
  
def create_plant(plant_name: str) -> typing.Union[PeaShooter,DoublePeaShooter,ColdIcePeaShooter,CrakedPodPeaShooter,MachineGunPeaShooter,CatTail,Carambola,None]:
   try:
      return __plant_factory(plant_name)
   except NoFoundPlantClassException:
      return None

class ZombieName(object):
   SIMPLE_ZOMBIE = "simple_zombie"
   
class NoFoundZombieClassException(Exception):
   def __init__(self,error_plant_name: str):
      self.error_plant_name = error_plant_name
      
   def __str__(self):
      return "No Found: \"" + self.error_plant_name + "\" this zombie class"
  
   def __repr__(self) -> str:
      return self.__str__()
   
def __zombie_factory(zombie_name: str) -> typing.Union[SimpleZombie,None]:
   if zombie_name == ZombieName.SIMPLE_ZOMBIE:
      return SimpleZombie()
   else:
      raise NoFoundZombieClassException(zombie_name)
  
def create_zombie(zombie_name: str) -> typing.Union[SimpleZombie,None]:
   """
   根据僵尸名称创建僵尸实例。

   :param zombie_name: 僵尸名称
   :return: 僵尸实例或 None
   """
   try:
      return  __zombie_factory(zombie_name)
   except NoFoundZombieClassException:
      return None     
  
__all__ = ["PlantName","create_plant","ZombieName","create_zombie"]