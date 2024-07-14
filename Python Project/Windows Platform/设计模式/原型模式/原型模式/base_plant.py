from plant_attribute import BasePlantAttribute 
from plant_attribute import SunCreatorPlantAttribute
from plant_attribute import ShooterPlantAttribute
from plant_attribute import BombPlantAttribute
from plant_attribute import ProtectionPlantAttribute
from plant_attribute import ProtectionPlantAttribute
from plant_attribute import MinePlantAttribute
from plant_attribute import EatZombiePlantAttribute

class BasePlant(object):
    
    def __init__(self, base_plant_attribute: BasePlantAttribute) -> None:
        self.base_plant_attribute = base_plant_attribute
        

class BaseSunCreatorPlant(BasePlant):
    
    def __init__(self, base_plant_attribute: BasePlantAttribute,sun_creator_plant_attribute: SunCreatorPlantAttribute) -> None:
        super(BaseSunCreatorPlant,self).__init__(base_plant_attribute)
        self.sun_creator_plant_attribute = sun_creator_plant_attribute
        

class BaseShooterPlant(BasePlant):
    
    def __init__(self,base_plant_attribute: BasePlantAttribute,shooter_plant_attribute: ShooterPlantAttribute) -> None:
        super(BaseShooterPlant,self).__init__(base_plant_attribute)
        self.shooter_plant_attribute = shooter_plant_attribute


class BaseProtectionPlant(BasePlant):
    
    def __init__(self,base_plant_attribute: BasePlantAttribute,protection_plant_attribute: ProtectionPlantAttribute) -> None:
        super(BaseProtectionPlant,self).__init__(base_plant_attribute)
        self.protection_plant_attribute = protection_plant_attribute
        

class BaseBombPlant(BasePlant):
    def __init__(self,base_plant_attribute: BasePlantAttribute, bomb_plant_attribute: BombPlantAttribute) -> None:
        super(BaseBombPlant,self).__init__(base_plant_attribute)
        self.base_bomb_plant = bomb_plant_attribute


class BaseMinePlant(BasePlant):
    def __init__(self,base_plant_attribute: BasePlantAttribute, mine_plant_attribute: MinePlantAttribute) -> None:
        super(BaseMinePlant,self).__init__(base_plant_attribute)
        self.mine_plant_attribute = mine_plant_attribute


class BaseEatZombiePlant(BasePlant):
    
    def __init__(self,base_plant_attribute: BasePlantAttribute, eat_zombie_plant_attribute: EatZombiePlantAttribute) -> None:
        super(BaseEatZombiePlant,self).__init__(base_plant_attribute)
        self.eat_zombie_attribute = eat_zombie_plant_attribute
        
__all__ = ["BasePlant",
           "BaseSunCreatorPlant",
           "BaseShooterPlant",
           "BaseProtectionPlant",
           "BaseBombPlant",
           "BaseMinePlant",
           "BaseEatZombiePlant"
          ]