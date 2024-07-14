# 修饰器模式: 作用: 1. 无需更改函数中的代码,就可以为函数添加新的功能 
#                   2. 可以通过缓存提高函数的调用效率
#             代码规范: 1. 创建一个修饰器(是一个函数,该函数用于接收一个函数,并返回一个函数),然后将函数与修饰器通过语法糖"@"将其进行关联
#                       2. 同1,但还需要创建一个缓存变量(是一个字典),在修饰器返回的函数中需要检查用户传入到函数内部的参数是否已经在缓存中
#                          ,如果存在就返回缓存的值,如果不存在,调用完函数过后,将函数的参数和返回值以键值对的形式进行存储
#             使用场景: 1. 编写一个函数,但不想更改这一个函数内部的代码
#                       2. 编写了一个高消耗的函数,需要降低函数的运行成本

from py_spider import PySpiderRequest

if __name__ == "__main__":
    py_spider_url = PySpiderRequest("http://www.baidu.com",is_random_user_agent=True)
    py_spider_parser1 = py_spider_url.get({})
    bs4_with_html_parser = py_spider_parser1.bs4_with_html_parse()
    py_spider_parser2 = py_spider_url.get({})
    print(py_spider_parser1)
    print(py_spider_parser2)
    print(py_spider_url.spider_get_cache)
    py_spider_url.clear_cache()
    print(py_spider_url.spider_get_cache)
    