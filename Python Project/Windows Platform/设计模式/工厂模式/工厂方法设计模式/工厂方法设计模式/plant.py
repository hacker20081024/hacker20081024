import typing
from plant_attribute import PlantAttribute

from plant_attribute import ShooterAttribute,ShooterBulletAttribute
class BasePlant(object):
    
    def __init__(self,plant_attribute_obj: PlantAttribute):
        self.plant_attribute_obj = plant_attribute_obj
        

class Shooter(BasePlant):
  
    def __init__(self,plant_attribute_obj: PlantAttribute, shooter_attribute_obj: ShooterAttribute, shooter_bullet_attribute_objs: typing.List[ShooterBulletAttribute]):
       super(Shooter,self).__init__(plant_attribute_obj)
       self.shooter_attribute_obj = shooter_attribute_obj
       self.shooter_bullet_attribute_obj = shooter_attribute_obj
       
class PeaShooter(Shooter):
    
    def __init__(self):
        super(PeaShooter,self).__init__(PlantAttribute("豌豆射手",100,25,150,"pea_shooter.gif"),
                                        ShooterAttribute(25,"pea_shooter_start_shoot.gif","pea_shooter_finish_shoot.gif"),
                                        [ShooterBulletAttribute("pea_shooter_bullet.png",25,"pea_shooter_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_RIGHT)])
        
class DoublePeaShooter(Shooter):
    
    def __init__(self):
        super(DoublePeaShooter,self).__init__(PlantAttribute("双发豌豆射手",200,25,150,"double_pea_shooter.gif"),
                                               ShooterAttribute(25,"double_pea_shooter_start_shoot.gif","double_pea_shooter_finish_shoot.gif"),
                                               [ShooterBulletAttribute("pea_shooter_bullet.png",25,"pea_shooter_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_RIGHT),
                                                ShooterBulletAttribute("pea_shooter_bullet.png",22,"pea_shooter_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_RIGHT)])
       
class ColdIcePeaShooter(Shooter):
    
    def __init__(self):
        super(ColdIcePeaShooter,self).__init__(PlantAttribute("寒冰豌豆射手",150,25,150,"double_pea_shooter.gif"),
                                               ShooterAttribute(25,"cold_ice_pea_shooter_start_shoot.gif","cold_ice_pea_shooter_finish_shoot.gif"),
                                               [ShooterBulletAttribute("code_ice_pea_shooter_bullet.png",25,"code_ice_pea_shooter_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_RIGHT)])
        
class CrakedPodPeaShooter(Shooter):
    
    def __init__(self):
        super(CrakedPodPeaShooter,self).__init__(PlantAttribute("裂荚豌豆射手",150,25,150,"double_pea_shooter.gif"),
                                               ShooterAttribute(25,"craked_pod_pea_shooter_start_shoot.gif","craked_pod_pea_shooter_finish_shoot.gif"),
                                               [ShooterBulletAttribute("craked_pod_pea_shooter_bullet.png",25,"craked_pod_pea_shooter_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_RIGHT),
                                                ShooterBulletAttribute("craked_pod_pea_shooter_bullet.png",25,"craked_pod_pea_shooter_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_LEFT),
                                                ShooterBulletAttribute("craked_pod_pea_shooter_bullet.png",22,"craked_pod_pea_shooter_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_LEFT)])

class MachineGunPeaShooter(Shooter):
    
    def __init__(self):
        super(MachineGunPeaShooter, self).__init__(
            PlantAttribute("机枪豌豆射手", 300, 30, 250, "machine_gun_pea_shooter.gif"),
            ShooterAttribute(30, "machine_gun_pea_shooter_start_shoot.gif", "machine_gun_pea_shooter_finish_shoot.gif"),
            [ShooterBulletAttribute("machine_gun_pea_shooter_bullet.png", 25, "machine_gun_pea_shooter_bullet_bomb.png", ShooterBulletAttribute.BulletShootWay.TO_RIGHT),
             ShooterBulletAttribute("machine_gun_pea_shooter_bullet.png", 22, "machine_gun_pea_shooter_bullet_bomb.png", ShooterBulletAttribute.BulletShootWay.TO_RIGHT),
             ShooterBulletAttribute("machine_gun_pea_shooter_bullet.png", 19, "machine_gun_pea_shooter_bullet_bomb.png", ShooterBulletAttribute.BulletShootWay.TO_RIGHT),
             ShooterBulletAttribute("machine_gun_pea_shooter_bullet.png", 16, "machine_gun_pea_shooter_bullet_bomb.png", ShooterBulletAttribute.BulletShootWay.TO_RIGHT),
             ShooterBulletAttribute("machine_gun_pea_shooter_bullet.png", 13, "machine_gun_pea_shooter_bullet_bomb.png", ShooterBulletAttribute.BulletShootWay.TO_RIGHT),]
        )
        
class CatTail(Shooter):
    
    def __init__(self):
        super(CatTail,self).__init__(PlantAttribute("香蒲",350,45,250,"cat_tail.gif"),
                                               ShooterAttribute(25,"cat_tail_start_shoot.gif","cat_tail_finish_shoot.gif"),
                                               [ShooterBulletAttribute("cat_tail_bullet.png",25,"cat_tail_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.LOCK_ZOMBIE)])
        
class Carambola(Shooter):
    
    def __init__(self):
        super(Carambola,self).__init__(PlantAttribute("杨桃",350,45,250,"carambola.gif"),
                                               ShooterAttribute(25,"carambola_start_shoot.gif","carambola_finish_shoot.gif"),
                                               [ShooterBulletAttribute("carambola_bullet.png",25,"carambola_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_LEFT),
                                                ShooterBulletAttribute("carambola_bullet.png",25,"carambola_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_RIGHT),
                                                ShooterBulletAttribute("carambola_bullet.png",25,"carambola_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_TOP),
                                                ShooterBulletAttribute("carambola_bullet.png",25,"carambola_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_BOTTOM),
                                                ShooterBulletAttribute("carambola_bullet.png",25,"carambola_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_LEFT_TOP),
                                                ShooterBulletAttribute("carambola_bullet.png",25,"carambola_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_LEFT_BOTTOM),
                                                ShooterBulletAttribute("carambola_bullet.png",25,"carambola_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_RIGHT_TOP),
                                                ShooterBulletAttribute("carambola_bullet.png",25,"carambola_bullet_bomb.png",ShooterBulletAttribute.BulletShootWay.TO_RIGHT_BOTTOM)])

__all__ = ["PeaShooter","DoublePeaShooter","ColdIcePeaShooter","CrakedPodPeaShooter","MachineGunPeaShooter","CatTail","Carambola"]