import typing

class BasePlantAttribute(object):
    
    def __init__(self):
        self.plant_health: int = 0
        self.plant_attack_value: int = 0
        self.plant_name: str  = ""
        self.plant_uuid: str  = ""
        self.plant_plant_sound_effect: str = ""
        self.plant_plant_cartoon: str = ""
        self.dig_plant_sound_effect: str = ""
        self.dig_plant_cartoon: str = ""
        self.plant_cartton: str = ""
        self.plant_pos: typing.List[int] = []


class SunCreatorPlantAttribute(object):
    
    def __init__(self):
        self.sun_creator_create_sun_value: int = 0
        self.sun_creator_create_sun_interval: int = 0
        self.sun_creator_will_create_sun_cartoon: str = ""
        self.sun_creator_finish_created_sun_cartoon: str = ""   


class ShooterPlantBulletAttribute(object):
    
    class BulletMoveWay(object):
        TO_LEFT         = 0
        TO_RIGHT        = 1
        TO_TOP          = 2
        TO_BOTTOM       = 3
        TO_LEFT_TOP     = 4
        TO_LEFT_BOTTOM  = 5
        TO_RIGHT_TOP    = 6
        TO_RIGHT_BOTTOM = 7
        LOCK_ZOMBIE     = 8
        __all__ = [TO_LEFT,TO_RIGHT,TO_TOP,TO_BOTTOM,TO_LEFT_TOP,TO_LEFT_BOTTOM,TO_RIGHT_TOP,TO_RIGHT_BOTTOM,LOCK_ZOMBIE]

    class BulletEffect(object):
        SLOW_DOWN_ZOMBIE_SPEED = 1
        __all__ = [SLOW_DOWN_ZOMBIE_SPEED]

    def __init__(self):
        self.bullet_move_speed: int = 0
        self.bullet_move_way: int = 0
        self.bullet_move_cartoon: str = ""
        self.bullet_hit_zombie_cartoon: str = ""
        self.bullet_hit_zombie_sound_effects: str = ""
        self.bullet_hit_zombie_attack_value: int = 0
        self.bullet_hit_zombie_effect: int = 0


class ShooterPlantAttribute(object):
    
    def __init__(self):
        self.shooter_shoot_bullet_time_interval: int = 0  
        self.shooter_shoot_bullets: typing.List[ShooterPlantBulletAttribute]
        self.shooter_will_shoot_bullet_cartoon: str = ""
        self.shooter_will_shoot_bullet_sound_effects: str = ""
        self.shooter_finished_shoot_bullet_cartoon: str = ""
        self.shooter_finished_shoot_bullet_cartoon_sound_effects: str = ""


class ProtectionPlantAttribute(object):
    
    def __init__(self):
        self.protection_plant_health: int = 0
        self.protection_plant_was_eaten_sound_effect: str = ""
        self.protection_plant_health_with_cartoon: typing.Dict[int,str] = {}
        self.protection_plant_hart_with_cartoon: typing.Dict[int,str] = {}
      
        
class BombPlantAttribute(object):
    
    def __init__(self):
        self.bomb_plant_attack_value: int = 0
        self.bomb_plant_attack_range: typing.List[typing.Tuple[int]] = []
        self.start_bomb_cartoon: str = ""
        self.start_bomb_sound_effect: str = ""
        self.bomb_sound_effect: str = ""
        self.bomb_pict_effect: str = ""
        

class MinePlantAttribute(object):
    
    def __init__(self):
        self.mine_plant_attack_value: int = 0
        self.mine_plant_is_grow_up: bool = False
        self.mine_plant_attack_range: typing.List[typing.Tuple[int]] = []
        self.mine_plant_grow_time: int = 0
        self.mine_plant_seed_cartoon: str = ""
        self.mine_plant_grow_up_cartoon: str = ""
        self.mine_plant_grow_up_sound_effect: str = ""
        self.mine_plant_cartoon: str = ""
        self.bomb_sound_effect: str = ""
        self.bomb_pict_effect: str = ""


class EatZombiePlantAttribute(object):
    
    def __init__(self):
        self.chew_zombie_time: int = 0
        self.will_eat_zombie_cartoon: str = ""
        self.eat_zombie_sound_effect: str = ""
        self.finish_eaten_zombie_cartoon: str = ""
        self.chew_zombie_cartoon: str = ""
        self.finish_chew_zombie_cartoon: str = ""
        

__all__ = ["BasePlantAttribute",
           "SunCreatorPlantAttribute",
           "ShooterPlantBulletAttribute",
           "ShooterPlantAttribute",
           "ProtectionPlantAttribute",
           "BombPlantAttribute",
           "MinePlantAttribute",
           "EatZombiePlantAttribute"
          ]