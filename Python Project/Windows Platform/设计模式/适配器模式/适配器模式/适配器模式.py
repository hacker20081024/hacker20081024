# 适配器模式: 
# 作用:     适配器模式可以让使用者自定义指向类实例化函数的属性
# 代码规范: 创建一个类(Adapter Class),在适配器类中的实例化对象入口点接收
#           类实例化对象,自定义属性,要指向的类实例化函数,并用自定义属性指向类实例化方法,在用一个属性指向类实例化对象
# 使用创建: 创建了一些类,类内部的一些函数有相同的行为,这个时候就可以通过配装器模式将这一些类指向于同一个名称相同的自定义属性名


from protocol_connection import ApplicationLayerConnectionFactory
from protocol_connection import AdapterApplicationLayerConnection

if __name__ == "__main__":
   application_layer_connection_factory = ApplicationLayerConnectionFactory()
   pop_connection_object = application_layer_connection_factory.create_pop_connection("pop.163.com",True,30)
   adapter_pop_connection_object = AdapterApplicationLayerConnection(pop_connection_object)
   print(adapter_pop_connection_object.is_connect())
   print(adapter_pop_connection_object.close_connect())
   print(adapter_pop_connection_object)
   print("")
   imap_connection_object = application_layer_connection_factory.create_imap_connection("imap.163.com",True,30)
   adapter_imap_connection_object = AdapterApplicationLayerConnection(imap_connection_object)
   print(adapter_imap_connection_object.is_connect())
   print(adapter_imap_connection_object.close_connect())
   print(adapter_imap_connection_object)

   