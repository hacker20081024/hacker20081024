# 抽象工厂设计模式
#
# 该设计模式与方法工厂模式类似,都是让模块使用者去通过一个模块就可以使用其他模块的资源,只不过抽象工厂模式与方法工厂模式在不同的情景下使用

# 当模块内部的资源较少时: 工厂方法设计模式, 当模块内部的资源较多时: 抽象工厂设计模式

# 抽象工厂模式: 开发者如果要让模块使用者通过一个模块就可以去访问其他模块的资源,访问的资源较多时,就可以使用该设计模式,开发者要在模块内部创建
#               一个类(抽象工厂类),在这一个类中创建多个类实例化方法(方法不能是静态的),每一个方法都会返回指定类的类实例化对象
#               
# 相比于工厂方法设计模式,抽象工厂设计模式的优点在于,使用者可以直接通过类内部的方法创建每一个类的类实例化对象,这就避免了如果创建的类实例化对象
# 的资源过多时,用户需要在工厂方法内部传入创建某一个类实例化对象对象的标志,从而让用户使用该模块的难度降低

from network_connection import ApplicationLayerConnection

if __name__ == "__main__":
   application_layer_connection_factory = ApplicationLayerConnection()
   POP3_Connection = application_layer_connection_factory.POP3_Connection("pop.163.com",is_encryption=True)
   print(POP3_Connection.is_connect())
   POP3_Connection.close_connect()