import typing

class BasePlantAttribute(object):
    
    def __init__(self):
        self.plant_name: str = ""
        self.plant_health: int = 0
        self.plant_attack_value: int = 0
        self.plant_choose_picture: str = ""
        self.plant_grow_cartoon: str = ""
        self.plant_cartoon: str = ""
        self.plant_cartton_list: typing.List[str] = ""
        self.plant_choose_sound_effects: str = ""
        self.plant_plant_sound_effects: str = ""
    
        
class BasePlantAttributeBuilder(object):
    
    def __init__(self):
        self.base_plant_attribute = BasePlantAttribute()
            
    def set_plant_name(self,plant_name: str) -> bool:
        self.base_plant_attribute.plant_name = plant_name
        return True

    def set_plant_health(self,plant_health: int) -> bool:
        if plant_health >= 0:
            self.base_plant_attribute.plant_health = plant_health
            return True
        else:
            return False
        
    def set_plant_attack_value(self,plant_attack_value: int) -> bool:
        if plant_attack_value >= 0:
           self.base_plant_attribute.plant_attack_value = plant_attack_value
           return True
        else:
           return False

    def set_plant_choose_picture(self,plant_picture: str) -> bool:
        if self.__is_picture(plant_picture):
           self.base_plant_attribute.plant_choose_picture = plant_picture
           return True
        else:
           return False
    
    def set_plant_grow_cartoon(self,plant_grow_cartoon: str) -> bool:
        if self.__is_cartoon(plant_grow_cartoon):
           self.base_plant_attribute.plant_grow_cartoon = plant_grow_cartoon 
           return True
        else:
           return False 

    def set_plant_cartoon(self,plant_cartoon: str) -> bool:
        if self.__is_cartoon(plant_cartoon):
           self.base_plant_attribute.plant_cartoon = plant_cartoon
           return True
        else:
           return False 

    def set_plant_cartoon_list(self,plant_cartton_list: typing.List[str]) -> bool:
        for plant_cartoon in plant_cartton_list:
            if not self.__is_cartoon(plant_cartoon):
               return False
        self.base_plant_attribute.plant_cartton_list = plant_cartton_list
        return True
          
    def set_plant_choose_sound_effects(self,plant_choose_sound_effects: str) -> bool:
        if self.__is_sound_effects(plant_choose_sound_effects):
           self.base_plant_attribute.plant_choose_sound_effects = plant_choose_sound_effects
           return True
        else:
           return False 

    def set_plant_plant_sound_effects(self,plant_plant_sound_effects: str) -> bool:
        if self.__is_sound_effects(plant_plant_sound_effects):
           self.base_plant_attribute.plant_plant_sound_effects = plant_plant_sound_effects
           return True
        else:
           return False

    def __is_picture(self,picture_file_name: str) -> bool:
        file_name_split = picture_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["png"]
    
    def __is_cartoon(self,cartoon_file_name: str) -> bool:
        file_name_split = cartoon_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["gif"]

    def __is_sound_effects(self,sound_effects_file_name: str) -> bool:
        file_name_split = sound_effects_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["mp3"]


class BuildBasePlantAttribute(object):
    
    def __init__(self,plant_name: str,plant_health: int, plant_attack_value: int, plant_choose_picture: str,plant_grow_cartoon: str,plant_cartoon: str,plant_cartoon_list: typing.List[str],plant_choose_sound_effects: str,plant_plant_sound_effects: str):
        self.plant_attribute_builder = BasePlantAttributeBuilder()
        self.plant_attribute_builder.set_plant_name(plant_name)
        self.plant_attribute_builder.set_plant_attack_value(plant_attack_value)
        self.plant_attribute_builder.set_plant_choose_picture(plant_choose_picture)
        self.plant_attribute_builder.set_plant_grow_cartoon(plant_grow_cartoon)
        self.plant_attribute_builder.set_plant_cartoon(plant_cartoon)
        self.plant_attribute_builder.set_plant_cartoon_list(plant_cartoon_list)
        self.plant_attribute_builder.set_plant_choose_sound_effects(plant_choose_sound_effects)
        self.plant_attribute_builder.set_plant_plant_sound_effects(plant_plant_sound_effects)  
        
    @property
    def base_plant_attribute(self):
        return self.plant_attribute_builder.base_plant_attribute


class SunBuilderPlantAttribute(object):
    
    def __init__(self):
        self.sun_value: typing.List[int] = [0]
        self.sun_number: typing.List[int]  = [0]
        self.sun_create_speed: typing.List[int] = [0]
        self.will_creat_sun_cartoon: typing.List[str] = [""]
        self.finish_created_sun_cartoon: typing.List[str] = [""]
        self.sun_plant_grow_sound_effects: str = ""
    
        
class SunBuilderPlantAttributeBuilder(object):
    
    def __init__(self):
        self.sun_builder_plant_attribute = SunBuilderPlantAttribute()
    
    def set_sun_value(self,sun_value: typing.List[int]) -> bool:
        for sun_value_ in sun_value:
            if not isinstance(sun_value_,int) or sun_value_ <= 0:
               return False
        self.sun_builder_plant_attribute.sun_value = sun_value    
        return True

    def set_sun_number(self,sun_number: typing.List[int]) -> bool:
        for sun_number_ in sun_number:
            if not isinstance(sun_number_,int) or sun_number_ <= 0:
               return False
        self.sun_builder_plant_attribute.sun_number = sun_number       
             
    def set_sun_create_speed(self,sun_create_speed: typing.List[int]) -> bool:
        for sun_create_speed_ in sun_create_speed:
            if not isinstance(sun_create_speed_,int) or sun_create_speed_ <= 0:
               return False
        self.sun_builder_plant_attribute.sun_create_speed = sun_create_speed
        return True
       
    def set_will_creat_sun_cartoon(self,will_creat_sun_cartoon: typing.List[str]) -> bool:
        for will_create_sun_cartoon_ in will_creat_sun_cartoon:
            if not self.__is_cartoon(will_create_sun_cartoon_):
               return False
        self.sun_builder_plant_attribute.will_creat_sun_cartoon = will_creat_sun_cartoon
        return True
    
    def set_finish_created_sun_cartoon(self,finish_created_sun_cartoon: typing.List[str]) -> bool:
        for finish_created_sun_cartoon_ in finish_created_sun_cartoon:
            if not self.__is_cartoon(finish_created_sun_cartoon_):
               return False
        self.sun_builder_plant_attribute.finish_created_sun_cartoon = finish_created_sun_cartoon
        return True

    def set_sun_plant_grow_sound_effects(self,sun_plant_grow_sound_effects: str) -> bool:
        if self.__is_sound_effects(sun_plant_grow_sound_effects):
           self.sun_builder_plant_attribute.sun_plant_grow_sound_effects = sun_plant_grow_sound_effects
           return True
        else:
           return False 

    def __is_picture(self,picture_file_name: str) -> bool:
        file_name_split = picture_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["png"]
    
    def __is_cartoon(self,cartoon_file_name: str) -> bool:
        file_name_split = cartoon_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["gif"]

    def __is_sound_effects(self,sound_effects_file_name: str) -> bool:
        file_name_split = sound_effects_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["mp3"]
    

class BuildSunBuilderPlantAttribute(object):
    
    def __init__(self,sun_value: typing.List[int],sun_number: typing.List[int],sun_create_speed: typing.List[int],will_create_sun_cartoon: typing.List[str],finish_created_sun_cartoon: typing.List[str],sun_plant_grow_sound_effects:str):
        self.sun_builder_plant_attribute_builder = SunBuilderPlantAttributeBuilder()
        self.sun_builder_plant_attribute_builder.set_sun_value(sun_value)
        self.sun_builder_plant_attribute_builder.set_sun_number(sun_number)
        self.sun_builder_plant_attribute_builder.set_sun_create_speed(sun_create_speed)
        self.sun_builder_plant_attribute_builder.set_will_creat_sun_cartoon(will_create_sun_cartoon)
        self.sun_builder_plant_attribute_builder.set_finish_created_sun_cartoon(finish_created_sun_cartoon)
        self.sun_builder_plant_attribute_builder.set_sun_plant_grow_sound_effects(sun_plant_grow_sound_effects)

    @property
    def sun_builder_plant_attribute(self):
        return self.sun_builder_plant_attribute_builder.sun_builder_plant_attribute


class ShooterPlantShootBulletAttribute(object):
    class BulletMoveWay(object):
        TO_LEFT:         int = 1
        TO_RIGHT:        int = 2
        TO_TOP:          int = 3
        TO_BOTTOM:       int = 4
        TO_LEFT_TOP:     int = 5
        TO_LEFT_BOTTOM:  int = 6
        TO_RIGHT_TOP:    int = 7
        TO_RIGHT_BOTTON: int = 8
        LOCK_ZOMBIE:     int = 9
        __all__ = [TO_LEFT,TO_TOP,TO_TOP,TO_BOTTOM,TO_LEFT_TOP,TO_LEFT_BOTTOM,TO_RIGHT_TOP,TO_RIGHT_BOTTON,LOCK_ZOMBIE]

    def __init__(self):
        self.bullet_cartoon: str = ""
        self.bullet_burst_cartoon: str = ""
        self.bullet_move_speed: int = ""
        self.bullet_move_way: int = 0
        self.bullet_attack_value: int = 0
        

class ShooterPlantShootBulletAttributeBuilder(object):
    
    def __init__(self):
        self.shooter_plant_shoot_bullet_attribute = ShooterPlantShootBulletAttribute()
        
    def set_bullet_cartoon(self,bullet_cartoon: str) -> bool:
        if self.__is_cartoon(bullet_cartoon):
           self.shooter_plant_shoot_bullet_attribute.bullet_cartoon = bullet_cartoon
           return True
        else:
           return False
        
    def set_bullet_burst_cartoon(self,bullet_burst_cartoon: str) -> bool:
        if self.__is_cartoon(bullet_burst_cartoon):
           self.shooter_plant_shoot_bullet_attribute.bullet_burst_cartoon = bullet_burst_cartoon
           return True
        else:
           return False 

    def set_bullet_move_speed(self,bullet_move_speed: int) -> bool:
        if bullet_move_speed >= 0:
            self.shooter_plant_shoot_bullet_attribute.bullet_move_speed = bullet_move_speed
            return True
        else:
            return False
        
    def set_bullet_move_way(self,bullet_move_way: int) -> bool:
        if bullet_move_way in ShooterPlantShootBulletAttribute.BulletMoveWay.__all__:
            self.shooter_plant_shoot_bullet_attribute.bullet_move_way = bullet_move_way
            return True
        else:
            return False

    def set_bullet_attack_value(self,bullet_attack_value: int) -> bool:
        if bullet_attack_value >= 0:
            self.shooter_plant_shoot_bullet_attribute.bullet_attack_value = bullet_attack_value
            return True
        else:
            return False

    def __is_cartoon(self,cartoon_file_name: str) -> bool:
        file_name_split = cartoon_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["gif"]


class BuildShooterPlantShootBulletAttribute(object):
    
    def __init__(self,bullet_cartoon: str,bullet_burst_cartoon: str,bullet_move_speed: int,bullet_move_way: int,bullet_attack_value: int):
        self.shooter_plant_shoot_bullet_attribute_builder = ShooterPlantShootBulletAttributeBuilder()
        self.shooter_plant_shoot_bullet_attribute_builder.set_bullet_cartoon(bullet_cartoon)
        self.shooter_plant_shoot_bullet_attribute_builder.set_bullet_burst_cartoon(bullet_burst_cartoon)
        self.shooter_plant_shoot_bullet_attribute_builder.set_bullet_move_speed(bullet_move_speed)
        self.shooter_plant_shoot_bullet_attribute_builder.set_bullet_move_way(bullet_move_way)
        self.shooter_plant_shoot_bullet_attribute_builder.set_bullet_attack_value(bullet_attack_value)

    @property
    def shooter_plant_shoot_bullet_attribute(self):
        return self.shooter_plant_shoot_bullet_attribute_builder.shooter_plant_shoot_bullet_attribute
    

class ShooterPlantAttribute(object):
    
    def __init__(self):
        self.shooter_shoot_bullet_interval: int = ""
        self.shooter_will_shoot_bullet_cartoon: str = ""
        self.shooter_finish_shoot_bullet_cartton: str = ""
        self.shooter_shoot_bullet_sound_effects: str = ""
        self.shooter_shoot_bullet: typing.List[ShooterPlantShootBulletAttribute] = []
        

class ShooterPlantAttributeBuilder(object):
    
    def __init__(self):
        self.shooter_plant_attribute = ShooterPlantAttribute()
        
    def set_shooter_shoot_bullet_interval(self,shooter_shoot_bullet_interval: int) -> bool:
        if shooter_shoot_bullet_interval >= 0:
           self.shooter_plant_attribute.shooter_shoot_bullet_interval = shooter_shoot_bullet_interval
           return True
        else:
           return False 

    def set_shooter_will_shoot_bullet_cartoon(self,shooter_will_shoot_bullet_cartoon: str) -> bool:
        if self.__is_cartoon(shooter_will_shoot_bullet_cartoon):
           self.shooter_plant_attribute.shooter_will_shoot_bullet_cartoon = shooter_will_shoot_bullet_cartoon
           return True
        else:
           return False
       
    def set_shooter_shoot_bullet_sound_effects(self,shooter_shoot_bullet_sound_effects: str) -> bool:
        if self.__is_sound_effects(shooter_shoot_bullet_sound_effects):
           self.shooter_plant_attribute.shooter_shoot_bullet_sound_effects = shooter_shoot_bullet_sound_effects
           return True
        else:
           return False 
    
    def set_shooter_finish_shoot_bullet_cartton(self,shooter_finish_shoot_bullet_cartton: str) -> bool:
        if self.__is_cartoon(shooter_finish_shoot_bullet_cartton):
           self.shooter_plant_attribute.shooter_shoot_bullet_sound_effects = shooter_finish_shoot_bullet_cartton
           return True
        else:
           return False 

    def set_shooter_shoot_bullet(self,shooter_shoot_bullet: typing.List[ShooterPlantShootBulletAttribute]) -> bool:
        if len(shooter_shoot_bullet) != 0:
           self.shooter_plant_attribute.shooter_shoot_bullet = shooter_shoot_bullet
           return True
        else:
           return False 

    def __is_cartoon(self,cartoon_file_name: str) -> bool:
        file_name_split = cartoon_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["gif"]
    
    def __is_sound_effects(self,sound_effects_file_name: str) -> bool:
        file_name_split = sound_effects_file_name.split(".")
        return len(file_name_split) == 2 and file_name_split[1] in ["mp3"]
    

class BuildShooterPlantAttribute(object):
    
    def __init__(self,shooter_shoot_bullet_interval: int,shooter_will_shoot_bullet_cartoon: str,shooter_finish_shoot_bullet_cartton: str,shooter_shoot_bullet_sound_effects: str,shooter_shoot_bullet: typing.List[ShooterPlantShootBulletAttribute]):
        self.shooter_plant_attribute_builder = ShooterPlantAttributeBuilder()
        self.shooter_plant_attribute_builder.set_shooter_shoot_bullet_interval(shooter_shoot_bullet_interval)
        self.shooter_plant_attribute_builder.set_shooter_will_shoot_bullet_cartoon(shooter_will_shoot_bullet_cartoon)
        self.shooter_plant_attribute_builder.set_shooter_finish_shoot_bullet_cartton(shooter_finish_shoot_bullet_cartton)
        self.shooter_plant_attribute_builder.set_shooter_shoot_bullet_sound_effects(shooter_shoot_bullet_sound_effects)
        self.shooter_plant_attribute_builder.set_shooter_shoot_bullet(shooter_shoot_bullet)
        
    @property
    def shooter_plant_attribute(self):
        return self.shooter_plant_attribute_builder.shooter_plant_attribute