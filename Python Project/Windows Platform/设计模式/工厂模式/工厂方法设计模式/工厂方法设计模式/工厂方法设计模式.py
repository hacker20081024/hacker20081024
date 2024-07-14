# 工厂方法设计模式: 创建一个函数(工厂函数),在函数中会返回其他类(产品类)的类实例化对象

# 工厂方法设计模式的使用场景: 在模块化编程中,如果开发者在不同的模块内部创建了多个类,但开发者想要让使用者通过一个模块就可以访问其他模块的资源
# 这个时候就需要使用工厂方法设计模式
# 
# 例如: 开发者在模块A中创建了一个a类,在模块B中创建了一个b类,在模块C中创建了一个c类,这个时候开发者想要让使用者通过一个模块就可以访问a,b,c类,
#       这个使用开发者就可以创建一个模块d,在模块d内部使用工厂方法设计模式创建一个函数,在这一个函数中用于接收使用者发送的一些参数,并根据参数
#       创建指定的类实例化对象

# 案例: 实现植物大战僵尸底层逻辑

from plant_vs_zombie_factory import PlantName,create_plant
from plant_vs_zombie_factory import ZombieName,create_zombie 

if __name__ == "__main__":
   pea_shooter = create_plant(PlantName.PEA_SHOOTER) # 豌豆射手
   double_pea_shooter      = create_plant(PlantName.DOUBLE_PEA_SHOOTER) # 双发豌豆射手
   cold_ice_pea_shooter    = create_plant(PlantName.COLD_ICE_PEA_SHOOTER) # 寒冰豌豆射手
   craked_pod_pea_shooter  = create_plant(PlantName.CRAKED_POD_PEA_SHOOTER) # 裂荚豌豆射手
   machine_gun_pea_shooter = create_plant(PlantName.MACHINE_GUN_PEA_SHOOTER) # 机器豌豆射手
   cat_tail                = create_plant(PlantName.CAT_TAIL) # 魔法猫咪
   carambola               = create_plant(PlantName.CARAMBOLA) # 杨桃
   
   simple_zombie           = create_zombie(ZombieName.SIMPLE_ZOMBIE) # 普通僵尸
   
   print(pea_shooter)
   print(double_pea_shooter)
   print(cold_ice_pea_shooter)
   print(craked_pod_pea_shooter)
   print(machine_gun_pea_shooter)
   print(cat_tail)
   print(carambola)
   print(simple_zombie)