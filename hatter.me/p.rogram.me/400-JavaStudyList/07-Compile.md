编译原理知识

1. 用Java实现以下表达式解析并返回结果（语法和Oracle中的`select sysdate-1 from dual`类似）：
%%% prettify ln=1 rl=1
sysdate
sysdate - 1
sysdate - 1/24
sysdate - 1/(12*2)
%%%
1. 实现对一个List通过DSL筛选
%%% prettify ln=1 rl=1
QList<Map<String, Object>> mapList = new QList<Map<String, Object>>;
mapList.add({"name": "hatter test"});
mapList.add({"id": -1,"name": "hatter test"});
mapList.add({"id": 0, "name": "hatter test"});
mapList.add({"id": 1, "name": "test test"});
mapList.add({"id": 2, "name": "hatter test"});
mapList.add({"id": 3, "name": "test hatter"});
mapList.query("id is not null and id > 0 and name like '%hatter%'");
%%%
要求返回列表中匹配的对象，即最后两个对象；
1. 用Java实现以下程序（语法和变量作用域处理都和JavaScript类似）：
代码：
%%% prettify ln=1 rl=1
var a = 1;
var b = 2;
var c = function() {
__var a = 3;
__println(a);
__println(b);
};
c();
println(a);
println(b);
%%%
输出：
%%% prettify ln=1
3
2
1
2
%%%

#### 相关资料
1. [!](http://en.wikipedia.org/wiki/Abstract_syntax_tree)
1. [!](https://javacc.java.net/)
1. [!](http://www.antlr.org/)

