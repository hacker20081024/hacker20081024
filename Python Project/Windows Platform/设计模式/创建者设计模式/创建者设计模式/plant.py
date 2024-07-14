from plant_attribute import *
from plant_attribute import BasePlantAttribute

class BasePlant(object):
    
    def __init__(self,base_plant_attribute: BasePlantAttribute):
        self.base_plant_attribute = base_plant_attribute
        

class SunBuilderPlant(BasePlant):
    
    def __init__(self,base_plant_attribute: BasePlantAttribute,sun_builder_plant_attribute: SunBuilderPlantAttribute):
        super(SunBuilderPlant,self).__init__(base_plant_attribute)
        self.sun_builder_plant_attribute = sun_builder_plant_attribute

class ShooterPlant(BasePlant):
    
    def __init__(self,base_plant_attribute: BasePlantAttribute, shooter_plant_attribute: ShooterPlantAttribute):
        super(ShooterPlant,self).__init__(base_plant_attribute)
        self.shooter_plant_attribute = shooter_plant_attribute
        

class SunFlower(SunBuilderPlant):
    
    def __init__(self):
        super(SunFlower,self).__init__(
              BuildBasePlantAttribute("向日葵",100,0,"sun_flower_picture.png","sun_flower_grow_cartoon.gif","sun_flower_cartoon.gif",["sun_flower_cartoon.gif"],"sun_flower_choose_sound_effects.mp3","sun_flower_plant_sound_effects.mp3").base_plant_attribute
             ,BuildSunBuilderPlantAttribute([50],[1],[100],["sun_flower_will_create_sun_cartoon.gif"],["sun_flower_finish_created_sun_cartoon.gif"],"sun_flower_grow_create_sun_sound_effects.mp3").sun_builder_plant_attribute)
            

class PeaShooter(ShooterPlant):
    
    def __init__(self):
        super(PeaShooter,self).__init__(
            BuildBasePlantAttribute("豌豆射手",100,12.5,"pea_shooter_picture.png","pea_shooter_grow_cartoon.gif","pea_shooter_cartoon.gif",["pea_shooter_cartoon.gif"],"pea_shooter_choose_sound_effects.mp3","pea_shooter_plant_sound_effects.mp3").base_plant_attribute
            ,BuildShooterPlantAttribute(5,"pea_shooter_will_shoot_bullet_cartoon.gif","pea_shooter_finish_shoot_bullet_cartoon.gif","pea_shooter_shoot_bullet_sound_effect.mp3",[BuildShooterPlantShootBulletAttribute("pea_shooter_bullet_cartoon.gif","pea_shooter_bullet_burst_cartoon.gif",25,ShooterPlantShootBulletAttribute.BulletMoveWay.TO_RIGHT,12.5)]).shooter_plant_attribute
        )