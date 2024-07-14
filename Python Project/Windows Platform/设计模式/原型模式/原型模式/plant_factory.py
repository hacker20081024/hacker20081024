from all_plant import *

from copy import deepcopy


pea_shooter              = PeaShooter()
sun_flower               = SunFlower()
cherry_bomb              = CherryBomb()
nut                      = Nut()
potato_mine              = PotatoMine()
cold_ice_pea_shooter     = ColdIcePeaShooter()
big_mouth_flower         = BigMouthFlower()
double_shoot_pea_shooter = DoubleShootPeaShooter()

class ClonePlant(object):
    def __init__(self):
        self.cloned_object_dict = {} 
        self.register_cloned_plant_obj("pea_shooter",pea_shooter)
        self.register_cloned_plant_obj("sun_flower",sun_flower)
        self.register_cloned_plant_obj("nut",nut)
        self.register_cloned_plant_obj("potato_mine",potato_mine)
        self.register_cloned_plant_obj("cold_ice_pea_shooter",cold_ice_pea_shooter)
        self.register_cloned_plant_obj("big_mouth_flower",big_mouth_flower)
        self.register_cloned_plant_obj("double_shoot_pea_shooter",double_shoot_pea_shooter)
        
    def register_cloned_plant_obj(self,key_name: str,cloned_obj: object) -> bool:
        if key_name not in list(self.cloned_object_dict.keys()): 
           self.cloned_object_dict[key_name] = cloned_obj
           return True
        else:
           return False 

    def un_register_cloned_plant_obj(self,key_name: str) -> bool:
        if key_name in list(self.cloned_object_dict.keys()):
           del self.cloned_object_dict[key_name]
           return True
        else:
           return False
        
    def clone_plant_obj(self,key_name: str) -> None | object:
        if key_name in list(self.cloned_object_dict.keys()):
           return deepcopy(self.cloned_object_dict[key_name])
        else:
           return None 