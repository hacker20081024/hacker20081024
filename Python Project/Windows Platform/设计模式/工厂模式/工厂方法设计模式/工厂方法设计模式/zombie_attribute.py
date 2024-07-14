class ZombieAttribute(object):
    
    def __init__(self,zombie_name: str, 
                      zombie_move_speed: int,
                      zombie_eat_plant_attack_value: int, 
                      zombie_blood_volume: int, 
                      zombie_eat_plant_cartoon: str,
                      zombie_finish_eat_plant_cartton: str,
                      zombize_common_move_cartoon: str,
                      zombize_injured_cartoon: str,
                      zombize_half_blood_volume_move_cartoon: str,
                      zombize_die_cartoon: str):
        self.zombie_name = zombie_name
        self.zombie_move_speed = zombie_move_speed
        self.zombie_eat_plant_attack_value  = zombie_eat_plant_attack_value
        self.zombie_blood_volume = zombie_blood_volume
        self.zombie_eat_plant_cartoon = zombie_eat_plant_cartoon
        self.zombie_finish_eat_plant_cartton = zombie_finish_eat_plant_cartton
        self.zombize_common_move_cartoon = zombize_common_move_cartoon
        self.zombize_injured_cartoon = zombize_injured_cartoon
        self.zombize_half_blood_volume_move_cartoon = zombize_half_blood_volume_move_cartoon
        self.zombize_die_cartoon = zombize_die_cartoon
       
__all__ = ["ZombieAttribute"]