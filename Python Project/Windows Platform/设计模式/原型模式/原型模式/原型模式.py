# 原型模式: 1.使用场景: 当模块开发者发现某一个类的类实例化对象内部的属性和属性的值没有发生改变的时候,就可以使用原型模式优化代码
#           2.代码规范: 模块开发者需要创建一个类对象,并在类对象中创建3个函数,一个函数用于注册要克隆的对象,另外一个函数用于注销要克隆的对象,
#                       还有一个克隆函数,用于通过某一个类实例化对象,创建出一个新的与该类实例化对象关联类相同的类实例化对象,并且该类实例化对
#                       象中的属性所指向的值与该类实例化对象中属性指向的值是一致的.

# copy.deepcopy -> 该函数可以根据一个类实例化对象创建出一个新的类实例化对象,并将新的类实例化对象内部的属性所指向的值设置为该类实例化对象中属性的值

# 任务: 优化植物大战僵尸底层代码

from plant_factory import *

if __name__ == "__main__":
   clone_plant_obj = ClonePlant()
   pea_shooter_obj = clone_plant_obj.clone_plant_obj("pea_shooter")
   print(pea_shooter_obj,clone_plant_obj.cloned_object_dict.get("pea_shooter"))