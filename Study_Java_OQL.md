# Object Query Language(OQL) #

## 定义 ##
Object Query Language 即对象查询语言，用于在JVM dump映象文件中通过一种类SQL的查询语言查找指定的对象。

## 软件 ##
  * Sun/Oracle JDK软件自带 `jhat`
  * Memory Analyzer (MAT) http://www.eclipse.org/mat/

## 语法 ##
```
  select <JavaScript expression to select>
    [ from [instanceof] <class name> <identifier>
    [ where <JavaScript boolean expression to filter> ] ]
```
| **`<class name>`** | 完整的类名（如果用接口名即使加了instanceof也似乎查不了） |
|:-------------------|:--------------------------------------------------------------------------------|
| **`instanceof`** | 查找指定类包含其子类的实例 |
| **`<identifier>`** | 相关于数据库的别名，在查找结果和条件中使用 |

## 例子 ##

```
// 查找所有长度超过100的字符串
select s from java.lang.String s where s.count >= 100

// 查找所有长度超过256的int数组
select a from [I a where a.length >= 256

// 查找所有匹配指定正则表达式的字符串
select s.value.toString() from java.lang.String s where /java/((s && s.value) ? s.value.toString(): "")

// 查找所有文件的path
select file.path.value.toString() from java.io.File file

// 查找所有ClassLoader的名字
select classof(cl).name from instanceof java.lang.ClassLoader cl

// 查询所有value不同的整数
select unique(x, "it.value") from java.lang.Integer x
```

## 内建对象、函数 ##
  * heap对象
> heap对象的内建函数：
    * `heap.forEachClass` -- 为每个Java类调用一次callback回调函数
```
  heap.forEachClass(callback);
```
    * `heap.forEachObject` -- 为每个Java对象调用callback回调函数
```
  heap.forEachObject(callback, clazz, includeSubtypes);
```
    * `heap.findClass` -- finds Java Class of given name
```
  heap.findClass(className);
```
    * `heap.findObject` -- finds object from given object id
```
  heap.findObject(stringIdOfObject);
```
    * `heap.classes` -- returns an enumeration of all Java classes
    * `heap.objects` -- returns an enumeration of Java objects
```
  heap.objects(clazz, [includeSubtypes], [filter])
```
    * `heap.finalizables` -- returns an enumeration of Java objects that are pending to be finalized.
    * `heap.livepaths` -- return an array of paths by which a given object is alive. This method accepts optional second parameter that is a boolean flag. This flag tells whether to include paths with weak reference(s) or not. By default, paths with weak reference(s) are not included.
```
  select heap.livepaths(s) from java.lang.String s
```
    * `heap.roots` -- returns an Enumeration of Roots of the heap. Each Root object has the following properties:
      * id - String id of the object that is referred by this root
      * type - descriptive type of Root (JNI Global, JNI Local, Java Static etc)
      * description - String description of the Root
      * referrer - Thread Object or Class object that is responsible for this root or null
  * functions on individual objects
    * `allocTrace(jobject)`
    * `classof(jobject)`
    * `forEachReferrer(callback, jobject)`
    * `identical(o1, o2)`
    * `objectid(jobject)`
    * `reachables(jobject, excludedFields)`
    * `referrers(jobject)`
    * `referees(jobject)`
    * `refers(jobject)`
    * `root(jobject)`
    * `sizeof(jobject)`
    * `toHtml(obj)`
  * array/iterator/enumeration manipulation functions
    * `concat(array1/enumeration1, array2/enumeration2)`
    * `contains(array/enumeration, expression)`
    * `count(array/enumeration, expression)`
    * `filter(array/enumeration, expression)`
    * `length(array/enumeration)`
    * `map(array/enumeration, expression)`
    * `max(array/enumeration, [expression])`
    * `min(array/enumeration, [expression])`
    * `sort(array/enumeration, [expression])`
    * `sum(array/enumeration, [expression])`
    * `toArray(array/enumeration)`
    * `unique(array/enumeration, [expression])`

## MAT OQL [Object Query Language](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.mat.ui.help%2Ftasks%2Fqueryingheapobjects.html) ##


### 参考资料 ###
`[1].` jhat/oqlhelp/<br>
<code>[2].</code> <a href='http://visualvm.java.net/oqlhelp.html'>http://visualvm.java.net/oqlhelp.html</a><br>