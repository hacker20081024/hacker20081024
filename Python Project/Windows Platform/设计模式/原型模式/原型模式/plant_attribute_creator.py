from ast import List
from plant_attribute import ShooterPlantBulletAttribute
from plant_attribute_builder import *

import typing


class BasePlantAttributeCreator(object):
    
    def __init__(self,plant_health:int,plant_attack_value:int,plant_name:str,plant_uuid:str,plant_plant_sound_effect:str,plant_plant_cartoon:str,dig_plant_sound_effect:str,dig_plant_cartoon:str,plant_cartton:str,plant_pos:typing.List[int]):
        self.base_plant_attribute_builder = BasePlantAttributeBuilder()
        self.base_plant_attribute_builder.set_plant_health(plant_health)
        self.base_plant_attribute_builder.set_plant_attack_value(plant_attack_value)
        self.base_plant_attribute_builder.set_plant_name(plant_name)
        self.base_plant_attribute_builder.set_plant_uuid(plant_uuid)
        self.base_plant_attribute_builder.set_plant_plant_sound_effect(plant_plant_sound_effect)
        self.base_plant_attribute_builder.set_plant_plant_cartoon(plant_plant_cartoon)
        self.base_plant_attribute_builder.set_dig_plant_sound_effect(dig_plant_sound_effect)
        self.base_plant_attribute_builder.set_dig_plant_cartoon(dig_plant_cartoon)
        self.base_plant_attribute_builder.set_plant_cartton(plant_cartton)
        self.base_plant_attribute_builder.set_plant_pos(plant_pos)
        
    @property
    def base_plant_attribute(self):
        return self.base_plant_attribute_builder.base_plant_attribute


class SunCreatorPlantAttributeCreator(object):

    def __init__(self,sun_creator_create_sun_value:int,sun_creator_create_sun_interval:int,sun_creator_will_create_sun_cartoon:str,sun_creator_finish_created_sun_cartoon:str):
        self.sun_creator_plant_attribute_builder = SunCreatorPlantAttributeBuilder()
        self.sun_creator_plant_attribute_builder.set_sun_creator_create_sun_value(sun_creator_create_sun_value)
        self.sun_creator_plant_attribute_builder.set_sun_creator_create_sun_interval(sun_creator_create_sun_interval)
        self.sun_creator_plant_attribute_builder.set_sun_creator_will_create_sun_cartoon(sun_creator_will_create_sun_cartoon)
        self.sun_creator_plant_attribute_builder.set_sun_creator_finish_created_sun_cartoon(sun_creator_finish_created_sun_cartoon)

    @property
    def sun_creator_plant_attribute(self):
        return self.sun_creator_plant_attribute_builder.sun_creator_plant_attribute 
      

class ShooterPlantBulletAttributeCreator(object):
    
    def __init__(self,bullet_move_speed:int,bullet_move_way:int,bullet_move_cartoon:str,bullet_hit_zombie_cartoon:str,bullet_hit_zombie_sound_effects:str,bullet_hit_zombie_attack_value:int,bullet_hit_zombie_effect:int):
        self.shooter_plant_bullet_attribute_builder = ShooterPlantBulletAttributeBuilder()
        self.shooter_plant_bullet_attribute_builder.set_bullet_move_speed(bullet_move_speed)
        self.shooter_plant_bullet_attribute_builder.set_bullet_move_way(bullet_move_way)
        self.shooter_plant_bullet_attribute_builder.set_bullet_move_cartoon(bullet_move_cartoon)
        self.shooter_plant_bullet_attribute_builder.set_bullet_hit_zombie_cartoon(bullet_hit_zombie_cartoon)
        self.shooter_plant_bullet_attribute_builder.set_bullet_hit_zombie_sound_effects(bullet_hit_zombie_sound_effects)
        self.shooter_plant_bullet_attribute_builder.set_bullet_hit_zombie_attack_value(bullet_hit_zombie_attack_value)
        self.shooter_plant_bullet_attribute_builder.set_bullet_hit_zombie_effect(bullet_hit_zombie_sound_effects)

    @property
    def shooter_plant_bullet_attribute(self):
        return self.shooter_plant_bullet_attribute_builder.shooter_plant_bullet_attribute


class ShooterPlantAttributeCreator(object):
    
    def __init__(self,shooter_shoot_bullet_time_interval:int,shooter_shoot_bullets:typing.List[ShooterPlantBulletAttribute],shooter_will_shoot_bullet_cartoon:str,shooter_will_shoot_bullet_sound_effects:str,shooter_finished_shoot_bullet_cartoon:str,shooter_finished_shoot_bullet_cartoon_sound_effects:str):
        self.shooter_plant_attribute_builder = ShooterPlantAttributeBuilder()
        self.shooter_plant_attribute_builder.set_shooter_shoot_bullet_time_interval(shooter_shoot_bullet_time_interval)
        self.shooter_plant_attribute_builder.set_shooter_shoot_bullets(shooter_shoot_bullets)
        self.shooter_plant_attribute_builder.set_shooter_will_shoot_bullet_cartoon(shooter_will_shoot_bullet_cartoon)
        self.shooter_plant_attribute_builder.set_shooter_will_shoot_bullet_sound_effects(shooter_will_shoot_bullet_sound_effects)
        self.shooter_plant_attribute_builder.set_shooter_finished_shoot_bullet_cartoon(shooter_finished_shoot_bullet_cartoon)
        self.shooter_plant_attribute_builder.set_shooter_finished_shoot_bullet_cartoon_sound_effects(shooter_finished_shoot_bullet_cartoon_sound_effects)

    @property
    def shooter_plant_attribute(self):
        return self.shooter_plant_attribute_builder.shooter_plant_attribute

class ProtectionPlantAttributeCreator(object):
    
    def __init__(self,protection_plant_health:int,protection_plant_was_eaten_sound_effect:str,protection_plant_health_with_cartoon: typing.Dict[int,str],protection_plant_hart_with_cartoon: typing.Dict[int,str]):
        self.protection_plant_attribute_builder = ProtectionPlantAttributeBuilder()
        self.protection_plant_attribute_builder.set_protection_plant_health(protection_plant_health)
        self.protection_plant_attribute_builder.set_protection_plant_was_eaten_sound_effect(protection_plant_was_eaten_sound_effect)
        self.protection_plant_attribute_builder.set_protection_plant_health_with_cartoon(protection_plant_health_with_cartoon)
        self.protection_plant_attribute_builder.set_protection_plant_hart_with_cartoon(protection_plant_hart_with_cartoon)

    @property
    def protection_plant_attribute(self):
        return self.protection_plant_attribute_builder.protection_plant_attribute


class BombPlantAttributeCreator(object):
    
    def __init__(self,bomb_plant_attack_value:int,bomb_plant_attack_range: typing.List[typing.Tuple[int]],start_bomb_cartoon:str,start_bomb_sound_effect:str,bomb_sound_effect:str,bomb_pict_effect:str):
        self.base_bomb_plant_attribute_builder = BombPlantAttributeBuilder()
        self.base_bomb_plant_attribute_builder.set_bomb_plant_attack_value(bomb_plant_attack_value)
        self.base_bomb_plant_attribute_builder.set_bomb_plant_attack_range(bomb_plant_attack_range)
        self.base_bomb_plant_attribute_builder.set_start_bomb_cartoon(start_bomb_cartoon)
        self.base_bomb_plant_attribute_builder.set_start_bomb_sound_effect(start_bomb_sound_effect)
        self.base_bomb_plant_attribute_builder.set_bomb_sound_effect(bomb_sound_effect)
        self.base_bomb_plant_attribute_builder.set_bomb_pict_effect(bomb_pict_effect)
        
    @property
    def base_bomb_plant_attribute(self):
        return self.base_bomb_plant_attribute_builder.base_bomb_plant_attribute


class MinePlantAttributeCreator(object):
    
    def __init__(self,bomb_pict_effect:str,set_bomb_sound_effect:str,mine_plant_attack_range: typing.List[typing.Tuple[int]],mine_plant_attack_value:int,mine_plant_is_grow_up: bool,mine_plant_grow_time: int,mine_plant_seed_cartoon: str,mine_plant_grow_up_cartoon: str,mine_plant_grow_up_sound_effect: str,mine_plant_cartoon: str):
        self.mine_plant_attribute_builder = MinePlantAttributeBuilder()
        self.mine_plant_attribute_builder.set_bomb_pict_effect(bomb_pict_effect)
        self.mine_plant_attribute_builder.set_bomb_sound_effect(set_bomb_sound_effect)
        self.mine_plant_attribute_builder.set_mine_plant_attack_range(mine_plant_attack_range)
        self.mine_plant_attribute_builder.set_mine_plant_attack_value(mine_plant_attack_value)
        self.mine_plant_attribute_builder.set_mine_plant_is_grow_up(mine_plant_is_grow_up)
        self.mine_plant_attribute_builder.set_mine_plant_grow_time(mine_plant_grow_time)
        self.mine_plant_attribute_builder.set_mine_plant_seed_cartoon(mine_plant_seed_cartoon)
        self.mine_plant_attribute_builder.set_mine_plant_grow_up_cartoon(mine_plant_grow_up_cartoon)
        self.mine_plant_attribute_builder.set_mine_plant_grow_up_sound_effect(mine_plant_grow_up_sound_effect)
        self.mine_plant_attribute_builder.set_mine_plant_cartoon(mine_plant_cartoon)
        
    @property
    def mine_plant_attribute(self):
        return self.mine_plant_attribute_builder.mine_plant_attribute


class EatZombiePlantAttributeCreator(object):
    
    def __init__(self,chew_zombie_time:int,will_eat_zombie_cartoon:str,eat_zombie_sound_effect:str, finish_eaten_zombie_cartoon:str,chew_zombie_cartoon:str,finish_chew_zombie_cartoon:str):
        self.eat_zombie_plant_attribute_builder = EatZombiePlantAttributeBuilder()
        self.eat_zombie_plant_attribute_builder.set_chew_zombie_time(chew_zombie_time)
        self.eat_zombie_plant_attribute_builder.set_will_eat_zombie_cartoon(will_eat_zombie_cartoon)
        self.eat_zombie_plant_attribute_builder.set_eat_zombie_sound_effect(eat_zombie_sound_effect)
        self.eat_zombie_plant_attribute_builder.set_finish_eaten_zombie_cartoon(finish_eaten_zombie_cartoon)
        self.eat_zombie_plant_attribute_builder.set_chew_zombie_cartoon(chew_zombie_cartoon)
        self.eat_zombie_plant_attribute_builder.set_finish_chew_zombie_cartoon(finish_chew_zombie_cartoon)

    @property
    def eat_zombie_plant_attribute(self):
        return self.eat_zombie_plant_attribute_builder.eat_zombie_plant_attribute


__all__ = ["BasePlantAttributeCreator",
           "SunCreatorPlantAttributeCreator",
           "ShooterPlantBulletAttributeCreator",
           "ShooterPlantAttributeCreator",
           "ProtectionPlantAttributeCreator",
           "BombPlantAttributeCreator",
           "MinePlantAttributeCreator",
           "EatZombiePlantAttributeCreator"
          ]