# 创建者设计模式: 创建三个类,分别是产品类,构造类,指挥类. 
#                 产品类: 内部的类实例化对象属性全部初始化
#                 构造类: 创建产品类的类实例化对象,并写一些方法用于去设置产品类的类实例化对象内部的属性(一个方法设置一个属性)
#                 指挥类: 创建构造类的类实例化对象,并调用内部的函数去设置创建的产品类的类实例化对象内部的属性,并提供一个函数用于返回产品类的类实例化对象

# 使用场景: 如果一个类内部的属性太多了,这个时候就可以把这一个类看作为产品类,并使用创建者设计模式去设置类内部的属性

# 案例: 优化植物大战僵尸底层逻辑的Zombie类和Plant

from plant_factory import PlantFactory

if __name__ == "__main__":
   plant_factory = PlantFactory()
   sun_flower  = plant_factory.create_sun_flower()
   pea_shooter = plant_factory.create_pea_shooter()
   print(sun_flower,":",sun_flower.__dict__)
   print(pea_shooter,":",pea_shooter.__dict__)