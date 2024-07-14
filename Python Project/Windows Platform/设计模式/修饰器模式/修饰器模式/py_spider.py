from copy import deepcopy
from http.client import HTTPResponse
from fake_useragent import UserAgent
from urllib.request import urlopen
from urllib.request import Request
from bs4 import BeautifulSoup
import typing


class PySpiderParser(object):
    
    def __init__(self,http_response: HTTPResponse):
        self.http_response = http_response 
        
    def bs4_with_html_parse(self):
        return BeautifulSoup(self.http_response,'html.parser')
    
    def bs4_with_lxml_parse(self):
        return BeautifulSoup(self.http_response,'lxml')


class PySpiderRequest(object):
    
    __spider_get_cache:  typing.Dict[typing.Tuple[typing.Any],typing.Any] = {}
    __spider_post_cache: typing.Dict[typing.Tuple[typing.Any],typing.Any] = {}

    __UA_creator = UserAgent()
    
    def __spider_get_cache_manager(func):
        def callback(*args,**argv):
            form_table  = (args[1] if len(args) == 2 else argv["form_table"])
            form_table_str = b""
            for i,form_table_ in enumerate(list(form_table.keys())):
                form_table_str += form_table_ + b"=" + form_table[form_table_]
                if i != len(list(form_table.keys())) - 1:
                    form_table_str += b"&"
            request_url = args[0].url
            if (request_url,form_table_str,) in PySpiderRequest.__spider_get_cache:
               return PySpiderRequest.__spider_get_cache[(request_url,form_table_str,)]
            else:
               http_response = func(*args,*argv) 
               if args[0].is_cache:
                  PySpiderRequest.__spider_get_cache[(request_url,form_table_str,)] = http_response
               return http_response
        return callback


    def __spider_post_cache_manager(func):
        def callback(*args,**argv):
            form_table = args[1] if len(args) == 2 else argv["form_table"]
            form_table_str = b""
            for i,form_table_ in enumerate(list(form_table.key())):
                form_table_str += form_table_ + b"=" + form_table[form_table_]
                if i != len(list(form_table.key())) - 1:
                    form_table_str += b"&"
            request_url = args[0].url
            if (request_url,form_table_str,) in PySpiderRequest.__spider_post_cache:
                return PySpiderRequest.__spider_post_cache[(request_url,form_table_str,)]
            else:
                http_response = func(*args,**argv)
                if args[0].is_cache:
                   PySpiderRequest.__spider_post_cache[(request_url,form_table_str,)] = http_response
                return http_response                 
        return callback

    def __init__(self,url,timeout: int=30,is_random_user_agent:bool=False,is_cache:bool = True):
        self.url = url
        self.timeout = 30
        self.is_cache = is_cache
        self.is_random_user_agent = is_random_user_agent
        
    @__spider_get_cache_manager
    def get(self,form_table: typing.Dict[str,str]) -> PySpiderParser:
        request_ua =  PySpiderRequest.__get_random_ua()
        return PySpiderParser(urlopen(Request(url=self.url,data=form_table,method="GET",headers={"User-Agent":request_ua}),timeout=self.timeout))

    @__spider_post_cache_manager
    def post(self,form_table: typing.Dict[str,str]) -> PySpiderParser:
        request_ua =  PySpiderRequest.__get_random_ua()
        return  PySpiderParser(urlopen(Request(url=self.url,data=form_table,method="POST",headers={"User-Agent":request_ua}),timeout=self.timeout))
    
    def clear_cache(self) -> bool:
        PySpiderRequest.__spider_get_cache.clear()
        PySpiderRequest.__spider_post_cache.clear()
        return True

    def clear_get_cache(self) -> bool:
        PySpiderRequest.__spider_get_cache.clear()
        return True

    def clear_post_cache(self) -> bool:
        PySpiderRequest.__spider_post_cache.clear()
        return True

    @property
    def spider_get_cache(self):
        return PySpiderRequest.__spider_get_cache

    @property
    def spider_post_cache(self):
        return PySpiderRequest.__spider_post_cache

    @staticmethod
    def __get_random_ua() -> str:
        return PySpiderRequest.__UA_creator.random
       