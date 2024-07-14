import typing

class PlantAttribute(object):
    def __init__(self,plant_name: str,plant_pay_sun: int,plant_attack_value: int,plant_blood_volume: int,plant_cartoon: str):
        self.plant_name = plant_name
        self.plant_pay_sun = plant_pay_sun
        self.plant_attack_value = plant_attack_value
        self.plant_blood_volume = plant_blood_volume
        self.plant_cartoon = plant_cartoon

class ShooterAttribute(object):
    def __init__(self,interval_time_for_shooter_shoot_out_bullet: int, shooter_will_shoot_bullet_cartoon: str, shooter_finish_shoot_bullet_cartoon: str):
        self.interval_time_for_shooter_shoot_out_bullet = interval_time_for_shooter_shoot_out_bullet
        self.shooter_will_shoot_bullet_cartoon  = shooter_will_shoot_bullet_cartoon
        self.shooter_finish_shoot_bullet_cartoon = shooter_finish_shoot_bullet_cartoon

class ShooterBulletAttribute(object):
    
    class BulletShootWay(object):
        TO_RIGHT  = 1
        TO_LEFT   = 2  
        TO_TOP    = 3
        TO_BOTTOM = 4
        TO_LEFT_BOTTOM  = 5
        TO_RIGHT_BOTTOM = 6
        TO_LEFT_TOP = 8
        TO_RIGHT_TOP = 9
        LOCK_ZOMBIE = 10 

    def __init__(self,bullet_pict: str, bullet_shoot_speed: int,bullet_shoot_to_zombie_pict: str,bullet_shoot_way: int):
        self.bullet_pict = bullet_pict
        self.bullet_shoot_speed = bullet_shoot_speed
        self.bullet_shoot_to_zombie_pict = bullet_shoot_to_zombie_pict
        self.bullet_shoot_way = bullet_shoot_way
        
__all__ = ["PlantAttribute","ShooterAttribute","ShooterBulletAttribute"]