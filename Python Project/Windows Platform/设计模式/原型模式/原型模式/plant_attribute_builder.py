from plant_attribute import *

import typing


class BasePlantAttributeBuilder(object):
    
    def __init__(self):
        self.base_plant_attribute = BasePlantAttribute()
        
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
        
    def set_plant_name(self,plant_name: str) -> bool:
        if plant_name != "":
           self.base_plant_attribute.plant_name = plant_name
           return True
        else:
           return False
        
    def set_plant_uuid(self,plant_uuid: str) -> bool:
        if plant_uuid != "":
           self.base_plant_attribute.plant_uuid = plant_uuid
           return True
        else:
           return False 
       
    def set_plant_plant_sound_effect(self,plant_plant_sound_effect: str) -> bool:
        if self.is_sound_effect(plant_plant_sound_effect):
           self.base_plant_attribute.plant_plant_sound_effect = plant_plant_sound_effect
           return True
        else:
           return False
       
    def set_plant_plant_cartoon(self,plant_plant_cartoon: str) -> bool:
        if self.is_cartoon(plant_plant_cartoon):
            self.base_plant_attribute.plant_plant_cartoon = plant_plant_cartoon
            return True
        else:
            return False

    def set_dig_plant_sound_effect(self,dig_plant_sound_effect: str) -> bool:
        if self.is_sound_effect(dig_plant_sound_effect):
            self.base_plant_attribute.dig_plant_sound_effect = dig_plant_sound_effect
            return True
        else:
            return False

    def set_dig_plant_cartoon(self,dig_plant_cartoon: str) -> bool:
        if self.is_cartoon(dig_plant_cartoon):
            self.base_plant_attribute.dig_plant_cartoon = dig_plant_cartoon
            return True
        else:
            return False

    def set_plant_cartton(self,plant_cartton: str) -> bool:
        if self.is_cartoon(plant_cartton):
            self.base_plant_attribute.plant_cartton = plant_cartton
            return True
        else:
            return False

    def set_plant_pos(self,plant_pos: typing.List[int]) -> bool:
        if len(plant_pos) == 2 and isinstance(plant_pos[0],int) and isinstance(plant_pos[1],int):
           self.base_plant_attribute.plant_pos = plant_pos
           return True
        else:
           return False 
    
    def is_sound_effect(self,sound_effect_file_name: str) -> bool:
        sound_effect_file_name_list = sound_effect_file_name.split(".")
        return len(sound_effect_file_name_list) == 2 and sound_effect_file_name_list[1] == "mp3"
           
    def is_cartoon(self,cartoon_file_name: str) -> bool:
        cartoon_file_name_list = cartoon_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "gif"
    

class SunCreatorPlantAttributeBuilder(object):
    
    def __init__(self):
        self.sun_creator_plant_attribute = SunCreatorPlantAttribute()
        
    def set_sun_creator_create_sun_value(self,sun_creator_create_sun_value: int) -> bool:
        if sun_creator_create_sun_value >= 0:
           self.sun_creator_plant_attribute.sun_creator_create_sun_value = sun_creator_create_sun_value
           return True
        else:
           return False
        
    def set_sun_creator_create_sun_interval(self,sun_creator_create_sun_interval: int) -> bool:
        if sun_creator_create_sun_interval >= 0:
           self.sun_creator_plant_attribute.sun_creator_create_sun_interval = sun_creator_create_sun_interval
           return True
        else:
           return False

    def set_sun_creator_will_create_sun_cartoon(self,sun_creator_will_create_sun_cartoon: str) -> bool:
        if self.is_cartoon(sun_creator_will_create_sun_cartoon):
           self.sun_creator_plant_attribute.sun_creator_will_create_sun_cartoon = sun_creator_will_create_sun_cartoon
           return True
        else:
           return False 
       
    def set_sun_creator_finish_created_sun_cartoon(self,sun_creator_finish_created_sun_cartoon: str) -> bool:
        if self.is_cartoon(sun_creator_finish_created_sun_cartoon):
           self.sun_creator_plant_attribute.sun_creator_finish_created_sun_cartoon = sun_creator_finish_created_sun_cartoon
           return True
        else:
           return False 
           
    def is_cartoon(self,cartoon_file_name: str) -> bool:
        cartoon_file_name_list = cartoon_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "gif"


class ShooterPlantBulletAttributeBuilder(object):
    
    def __init__(self):
        self.shooter_plant_bullet_attribute = ShooterPlantBulletAttribute()
        
    def set_bullet_move_speed(self,bullet_move_speed: int) -> bool:
        if bullet_move_speed > 0:
            self.shooter_plant_bullet_attribute.bullet_move_speed = bullet_move_speed
            return True
        else:
            return False
        
    def set_bullet_move_way(self,bullet_move_way: int) -> bool:
        if bullet_move_way in ShooterPlantBulletAttribute.BulletMoveWay.__all__:
           self.shooter_plant_bullet_attribute.bullet_move_way = bullet_move_way
           return True
        else:
           return False
        
    def set_bullet_move_cartoon(self,bullet_move_cartoon: str) -> bool:
        if self.is_cartoon(bullet_move_cartoon):
           self.shooter_plant_bullet_attribute.bullet_move_cartoon = bullet_move_cartoon
           return True
        else:
           return False 
       
    def set_bullet_hit_zombie_cartoon(self,bullet_hit_zombie_cartoon: str) -> bool:
        if self.is_cartoon(bullet_hit_zombie_cartoon):
           self.shooter_plant_bullet_attribute.bullet_hit_zombie_cartoon = bullet_hit_zombie_cartoon
           return True
        else:
           return False
        
    def set_bullet_hit_zombie_sound_effects(self,bullet_hit_zombie_sound_effects: str) -> bool:
        if self.is_sound_effect(bullet_hit_zombie_sound_effects):
           self.shooter_plant_bullet_attribute.bullet_hit_zombie_sound_effects = bullet_hit_zombie_sound_effects
           return True
        else:
           return False 

    def set_bullet_hit_zombie_attack_value(self,bullet_hit_zombie_attack_value: int) -> bool:
        if bullet_hit_zombie_attack_value >= 0:
           self.shooter_plant_bullet_attribute.bullet_hit_zombie_attack_value = bullet_hit_zombie_attack_value
           return True
        else:
           return False 
       
    def set_bullet_hit_zombie_effect(self,bullet_hit_zombie_effect: int) -> bool:
        if bullet_hit_zombie_effect in ShooterPlantBulletAttribute.BulletEffect.__all__:
           self.shooter_plant_bullet_attribute.bullet_hit_zombie_effect = bullet_hit_zombie_effect
           return True
        else:
           return False 

    def is_sound_effect(self,sound_effect_file_name: str) -> bool:
        sound_effect_file_name_list = sound_effect_file_name.split(".")
        return len(sound_effect_file_name_list) == 2 and sound_effect_file_name_list[1] == "mp3"
    
    def is_cartoon(self,cartoon_file_name: str) -> bool:
        cartoon_file_name_list = cartoon_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "gif"
    

class ShooterPlantAttributeBuilder(object):
    
    def __init__(self):
        self.shooter_plant_attribute = ShooterPlantAttribute()

    def set_shooter_shoot_bullet_time_interval(self,shooter_shoot_bullet_time_interval:int) -> bool:
        if shooter_shoot_bullet_time_interval >= 0:
           self.shooter_plant_attribute.shooter_shoot_bullet_time_interval = shooter_shoot_bullet_time_interval
           return True
        else:
           return False
        
    def set_shooter_shoot_bullets(self,shooter_shoot_bullets: typing.List[ShooterPlantBulletAttribute]) -> bool:
        if len(shooter_shoot_bullets) != 0:
           self.shooter_plant_attribute.shooter_shoot_bullets = shooter_shoot_bullets
           return True
        else:
           return False 
    
    def set_shooter_will_shoot_bullet_cartoon(self,shooter_will_shoot_bullet_cartoon: str) -> bool:
        if self.is_cartoon(shooter_will_shoot_bullet_cartoon):
           self.shooter_plant_attribute.shooter_will_shoot_bullet_cartoon = shooter_will_shoot_bullet_cartoon
           return True
        else:
           return False
        
    def set_shooter_will_shoot_bullet_sound_effects(self,shooter_will_shoot_bullet_sound_effects:str) -> bool:
        if self.is_sound_effect(shooter_will_shoot_bullet_sound_effects):
           self.shooter_plant_attribute.shooter_will_shoot_bullet_sound_effects = shooter_will_shoot_bullet_sound_effects
           return True
        else:
           return False
        
    def set_shooter_finished_shoot_bullet_cartoon(self,shooter_finished_shoot_bullet_cartoon:str) -> bool:
        if self.is_cartoon(shooter_finished_shoot_bullet_cartoon):
           self.shooter_plant_attribute.shooter_finished_shoot_bullet_cartoon = shooter_finished_shoot_bullet_cartoon
           return True
        else:
           return False 

    def set_shooter_finished_shoot_bullet_cartoon_sound_effects(self,shooter_finished_shoot_bullet_cartoon_sound_effects:str) -> bool:
        if self.is_sound_effect(shooter_finished_shoot_bullet_cartoon_sound_effects):
           self.shooter_plant_attribute.shooter_finished_shoot_bullet_cartoon_sound_effects = shooter_finished_shoot_bullet_cartoon_sound_effects
           return True
        else:
           return False 

    def is_sound_effect(self,sound_effect_file_name: str) -> bool:
        sound_effect_file_name_list = sound_effect_file_name.split(".")
        return len(sound_effect_file_name_list) == 2 and sound_effect_file_name_list[1] == "mp3"
    
    def is_cartoon(self,cartoon_file_name: str) -> bool:
        cartoon_file_name_list = cartoon_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "gif"
    

class ProtectionPlantAttributeBuilder(object):
    
    def __init__(self):
        self.protection_plant_attribute = ProtectionPlantAttribute()
     
    def set_protection_plant_health(self,protection_plant_health: int) -> bool:
        if protection_plant_health > 0:
           self.protection_plant_attribute.protection_plant_health = protection_plant_health
           return True
        else:
           return False 

    def set_protection_plant_was_eaten_sound_effect(self,protection_plant_was_eaten_sound_effect:str) -> bool:
        if self.is_sound_effect(protection_plant_was_eaten_sound_effect):
            self.protection_plant_attribute.protection_plant_was_eaten_sound_effect = protection_plant_was_eaten_sound_effect
            return True
        else:
            return False
    
    def set_protection_plant_health_with_cartoon(self,protection_plant_health_with_cartoon: typing.Dict[int,str]) -> bool:
        self.protection_plant_attribute.protection_plant_health_with_cartoon = protection_plant_health_with_cartoon
        return True
        
    def set_protection_plant_hart_with_cartoon(self,protection_plant_hart_with_cartoon: typing.Dict[int,str]) -> bool:
        self.protection_plant_attribute.protection_plant_hart_with_cartoon = protection_plant_hart_with_cartoon
        return True

    def is_sound_effect(self,sound_effect_file_name: str) -> bool:
        sound_effect_file_name_list = sound_effect_file_name.split(".")
        return len(sound_effect_file_name_list) == 2 and sound_effect_file_name_list[1] == "mp3"
    
    def is_cartoon(self,cartoon_file_name: str) -> bool:
        cartoon_file_name_list = cartoon_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "gif"
    

class BombPlantAttributeBuilder(object):
    
    def __init__(self):
        self.base_bomb_plant_attribute = BombPlantAttribute()
        
    def set_bomb_plant_attack_value(self,bomb_plant_attack_value:int) -> bool:
        if bomb_plant_attack_value >= 0:
           self.base_bomb_plant_attribute.bomb_plant_attack_value = bomb_plant_attack_value
           return True
        else:
           return False 
    
    def set_bomb_plant_attack_range(self,bomb_plant_attack_range: typing.List[typing.Tuple[int]]) -> bool:
        if bomb_plant_attack_range != False:
           self.base_bomb_plant_attribute.bomb_plant_attack_range = bomb_plant_attack_range
           return True
        else:
           return False 

    def set_start_bomb_cartoon(self,start_bomb_cartoon: str) -> bool:
        if self.is_cartoon(start_bomb_cartoon):
           self.base_bomb_plant_attribute.start_bomb_cartoon = start_bomb_cartoon
           return True
        else:
           return False 
    
    def set_start_bomb_sound_effect(self,start_bomb_sound_effect: str) -> bool:
        if self.is_sound_effect(start_bomb_sound_effect):
           self.base_bomb_plant_attribute.start_bomb_sound_effect = start_bomb_sound_effect
           return True
        else:
           return False

    def set_bomb_sound_effect(self,bomb_sound_effect:str) -> bool:
        if self.is_sound_effect(bomb_sound_effect):
           self.base_bomb_plant_attribute.bomb_sound_effect = bomb_sound_effect
           return True
        else:
           return False
       
    def set_bomb_pict_effect(self,bomb_pict_effect:str) -> bool:
        if self.is_pict(bomb_pict_effect):
           self.base_bomb_plant_attribute.bomb_pict_effect = bomb_pict_effect

    def is_sound_effect(self,sound_effect_file_name: str) -> bool:
        sound_effect_file_name_list = sound_effect_file_name.split(".")
        return len(sound_effect_file_name_list) == 2 and sound_effect_file_name_list[1] == "mp3"
    
    def is_cartoon(self,cartoon_file_name: str) -> bool:
        cartoon_file_name_list = cartoon_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "gif"
    
    def is_pict(self,pict_file_name: str) -> bool:
        cartoon_file_name_list = pict_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "png"


class MinePlantAttributeBuilder(object):
    
    def __init__(self):
        self.mine_plant_attribute = MinePlantAttribute()
        
    def set_mine_plant_attack_value(self,mine_plant_attack_value: int) -> bool:
        if mine_plant_attack_value >= 0:
          self.mine_plant_attribute.mine_plant_attack_value = mine_plant_attack_value
          return True
        else:
          return False  
    
    def set_mine_plant_is_grow_up(self,mine_plant_is_grow_up:bool) -> bool:
        self.mine_plant_attribute.mine_plant_is_grow_up = mine_plant_is_grow_up
        return True
    
    def set_mine_plant_attack_range(self,mine_plant_attack_range:typing.List[typing.Tuple[int]]) -> bool:
        if mine_plant_attack_range != False:
           self.mine_plant_attribute.mine_plant_attack_range = mine_plant_attack_range
           return True
        else:
           return False
       
    def set_mine_plant_grow_time(self,mine_plant_grow_time: int) -> bool:
        if mine_plant_grow_time >= 0:
           self.mine_plant_attribute.mine_plant_grow_time = mine_plant_grow_time
           return True
        else:
           return False 
     
    def set_mine_plant_seed_cartoon(self,mine_plant_seed_cartoon: str) -> bool:
        if self.is_cartoon(mine_plant_seed_cartoon):
           self.mine_plant_attribute.mine_plant_seed_cartoon = mine_plant_seed_cartoon
           return True
        else:
           return False
       
    def set_mine_plant_grow_up_cartoon(self,mine_plant_grow_up_cartoon: str) -> bool:
        if self.is_cartoon(mine_plant_grow_up_cartoon):
           self.mine_plant_attribute.mine_plant_grow_up_cartoon = mine_plant_grow_up_cartoon
           return True
        else:
           return False 

    def set_mine_plant_grow_up_sound_effect(self,mine_plant_grow_up_sound_effect: str) -> bool:
        if self.is_sound_effect(mine_plant_grow_up_sound_effect):
           self.mine_plant_attribute.mine_plant_grow_up_sound_effect = mine_plant_grow_up_sound_effect
           return True
        else:
           return False 
    
    def set_mine_plant_cartoon(self,mine_plant_cartoon: str) -> bool:
        if self.is_cartoon(mine_plant_cartoon):
           self.mine_plant_attribute.mine_plant_cartoon = mine_plant_cartoon
           return True
        else:
           return False
        
    def set_bomb_sound_effect(self,bomb_sound_effect: str) -> bool:
        if self.is_sound_effect(bomb_sound_effect):
           self.mine_plant_attribute.bomb_sound_effect = bomb_sound_effect
           return True
        else:
           return False
        
    def set_bomb_pict_effect(self,bomb_pict_effect: str) -> bool:
        if self.is_sound_effect(bomb_pict_effect):
           self.mine_plant_attribute.bomb_pict_effect = bomb_pict_effect
           return True
        else:
           return False 

    def is_sound_effect(self,sound_effect_file_name: str) -> bool:
        sound_effect_file_name_list = sound_effect_file_name.split(".")
        return len(sound_effect_file_name_list) == 2 and sound_effect_file_name_list[1] == "mp3"
    
    def is_cartoon(self,cartoon_file_name: str) -> bool:
        cartoon_file_name_list = cartoon_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "gif"
    
    def is_pict(self,pict_file_name: str) -> bool:
        cartoon_file_name_list = pict_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "png"
    
class EatZombiePlantAttributeBuilder(object):
    
    def __init__(self):
        self.eat_zombie_plant_attribute = EatZombiePlantAttribute()
        
    def set_chew_zombie_time(self,chew_zombie_time: int) -> bool:
        if chew_zombie_time >= 0:
           self.eat_zombie_plant_attribute.chew_zombie_time = chew_zombie_time
           return True
        else:
           return False
        
    def set_will_eat_zombie_cartoon(self,will_eat_zombie_cartoon: str) -> bool:
        if self.is_cartoon(will_eat_zombie_cartoon):
           self.eat_zombie_plant_attribute.will_eat_zombie_cartoon = will_eat_zombie_cartoon
           return True
        else:
           return False  

    def set_eat_zombie_sound_effect(self,eat_zombie_sound_effect: str) -> bool:
        if self.is_sound_effect(eat_zombie_sound_effect):
           self.eat_zombie_plant_attribute.eat_zombie_sound_effect = eat_zombie_sound_effect
           return True
        else:
           return False
        
    def set_finish_eaten_zombie_cartoon(self,finish_eaten_zombie_cartoon: str) -> bool:
        if self.is_cartoon(finish_eaten_zombie_cartoon):
           self.eat_zombie_plant_attribute.finish_eaten_zombie_cartoon = finish_eaten_zombie_cartoon
           return True
        else:
           return False 

    def set_chew_zombie_cartoon(self,chew_zombie_cartoon: str) -> bool:
        if self.is_cartoon(chew_zombie_cartoon):
           self.eat_zombie_plant_attribute.chew_zombie_cartoon = chew_zombie_cartoon
           return True
        else:
           return False
        
    def set_finish_chew_zombie_cartoon(self,finish_chew_zombie_cartoon: str) -> bool:
        if self.is_cartoon(finish_chew_zombie_cartoon):
           self.eat_zombie_plant_attribute.finish_chew_zombie_cartoon = finish_chew_zombie_cartoon
           return True
        else:
           return False 

    def is_sound_effect(self,sound_effect_file_name: str) -> bool:
        sound_effect_file_name_list = sound_effect_file_name.split(".")
        return len(sound_effect_file_name_list) == 2 and sound_effect_file_name_list[1] == "mp3"
    
    def is_cartoon(self,cartoon_file_name: str) -> bool:
        cartoon_file_name_list = cartoon_file_name.split(".")
        return len(cartoon_file_name_list) == 2 and cartoon_file_name_list[1] == "gif"
    

__all__ = ["BasePlantAttributeBuilder",
           "SunCreatorPlantAttributeBuilder",
           "ShooterPlantBulletAttributeBuilder",
           "ShooterPlantAttributeBuilder",
           "ProtectionPlantAttributeBuilder",
           "BombPlantAttributeBuilder",
           "MinePlantAttributeBuilder",
           "EatZombiePlantAttributeBuilder"
          ]