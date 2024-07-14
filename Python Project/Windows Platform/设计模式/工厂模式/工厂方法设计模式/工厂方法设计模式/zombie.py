from zombie_attribute import ZombieAttribute

class BaseZombie(object):
    
    def __init__(self,zombie_attribute: ZombieAttribute):
        self.zombie_attribute = zombie_attribute
        

class SimpleZombie(BaseZombie):
    
    def __init__(self):
        super(SimpleZombie,self).__init__(ZombieAttribute( 
                zombie_name="普通僵尸",
                zombie_move_speed=5,
                zombie_eat_plant_attack_value=15,
                zombie_blood_volume=100,
                zombie_eat_plant_cartoon="eat_plant.gif",
                zombie_finish_eat_plant_cartton="finish_eat_plant.gif",
                zombize_common_move_cartoon="common_move.gif",
                zombize_injured_cartoon="injured.gif",
                zombize_half_blood_volume_move_cartoon="half_blood_volume_move.gif",
                zombize_die_cartoon="die.gif"))

__all__ = ["SimpleZombie"]