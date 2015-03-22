# Spring自定义标签 #

## Spring自定义标签的原理 ##
XML通常通过DTD、XSD定义，但DTD的表达能力较弱，XSD定义则能力比较强，能够定义类型，出现次数等。自定义标签需要XSD支持，在实现时使用Namespace扩展来支持自定义标签。

当你在苦逼的写下面的代码时：
```
    <bean id="beanId" class="com.xxx.xxxx.Xxxxx">
        <property name="property1">
            <value>XXXX</value>
        </property>
        <property name="property2">
            <value>XXXX</value>
        </property>
    </bean>
```
是不是会羡慕这样写代码呢？
```
    <xxx:xxxx id="beanId"/>
```

Spring通过XML解析程序将其解析为DOM树，通过NamespaceHandler指定对应的Namespace的BeanDefinitionParser将其转换成BeanDefinition。再通过Spring自身的功能对BeanDefinition实例化对象。

在期间，Spring还会加载两项资料：
  * <b><code>META-INF/spring.handlers</code></b> <br>指定NamespaceHandler(实现<code>org.springframework.beans.factory.xml.NamespaceHandler</code>)接口，或使用<code>org.springframework.beans.factory.xml.NamespaceHandlerSupport</code>的子类。<br>
<ul><li><b><code>META-INF/spring.schemas</code></b> <br>在解析XML文件时将XSD重定向到本地文件，避免在解析XML文件时需要上网下载XSD文件。通过现实<code>org.xml.sax.EntityResolver</code>接口来实现该功能。</li></ul>

<h2>制作自定义的标签</h2>
<code>spring.handlers</code>:<br>
<pre><code>http\://test.hatter.me/schema/test=me.hatter.test.TestNamespaceHandler<br>
</code></pre>

<code>spring.schemas</code>:<br>
<pre><code>http\://test.hatter.me/schema/test/test.xsd=META-INF/test.xsd<br>
</code></pre>

<code>test.xsd</code>:<br>
<pre><code>&lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;<br>
&lt;xsd:schema xmlns="http://test.hatter.me/schema/test"<br>
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" <br>
	targetNamespace="http://test.hatter.me/schema/test"&gt;<br>
<br>
	&lt;xsd:element name="custom" type="customType"&gt;<br>
	&lt;/xsd:element&gt;<br>
	<br>
	&lt;xsd:complexType name="customType"&gt;<br>
		&lt;xsd:attribute name="id" type="xsd:ID"&gt;<br>
		&lt;/xsd:attribute&gt;<br>
		&lt;xsd:attribute name="name" type="xsd:string"&gt;<br>
		&lt;/xsd:attribute&gt;<br>
	&lt;/xsd:complexType&gt;<br>
<br>
&lt;/xsd:schema&gt;<br>
</code></pre>

<code>me.hatter.test.TestNamespaceHandler</code>:<br>
<pre><code>package me.hatter.test;<br>
<br>
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;<br>
<br>
public class TestNamespaceHandler extends NamespaceHandlerSupport {<br>
<br>
    public void init() {<br>
        registerBeanDefinitionParser("custom", new TestCustomBeanDefinitionParser());<br>
    }<br>
}<br>
</code></pre>

<code>me.hatter.test.TestCustomBeanDefinitionParser</code>:<br>
<pre><code>package me.hatter.test;<br>
<br>
import me.hatter.test.bean.TestBean;<br>
<br>
import org.springframework.beans.factory.config.BeanDefinition;<br>
import org.springframework.beans.factory.support.RootBeanDefinition;<br>
import org.springframework.beans.factory.xml.BeanDefinitionParser;<br>
import org.springframework.beans.factory.xml.ParserContext;<br>
import org.w3c.dom.Element;<br>
<br>
public class TestCustomBeanDefinitionParser implements BeanDefinitionParser {<br>
<br>
    public BeanDefinition parse(Element element, ParserContext parserContext) {<br>
<br>
        String id = element.getAttribute("id");<br>
        String name = element.getAttribute("name");<br>
<br>
        RootBeanDefinition beanDefinition = new RootBeanDefinition();<br>
        beanDefinition.setBeanClass(TestBean.class);<br>
        beanDefinition.getPropertyValues().addPropertyValue("name", name);<br>
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);<br>
<br>
        return beanDefinition;<br>
    }<br>
}<br>
</code></pre>

<h2>测试代码</h2>
<code>test.xml</code>:<br>
<pre><code>&lt;?xml version="1.0" encoding="UTF-8"?&gt;<br>
&lt;beans xmlns="http://www.springframework.org/schema/beans"<br>
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"<br>
    xmlns:test="http://test.hatter.me/schema/test"<br>
    xsi:schemaLocation="<br>
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd<br>
        http://test.hatter.me/schema/test http://test.hatter.me/schema/test/test.xsd"&gt;<br>
	<br>
	&lt;test:custom id="testCustom" name="this is a test custom tag" /&gt;<br>
&lt;/beans&gt;<br>
</code></pre>

<code>me.hatter.test.main.Main</code>:<br>
<pre><code>package me.hatter.test.main;<br>
<br>
import org.springframework.context.ApplicationContext;<br>
import org.springframework.context.support.ClassPathXmlApplicationContext;<br>
<br>
public class Main {<br>
<br>
    public static void main(String[] args) {<br>
        String xml = "classpath:me/hatter/test/main/test.xml";<br>
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { xml });<br>
        System.out.println(context.getBean("testCustom"));<br>
    }<br>
}<br>
</code></pre>

上例输出为：<br>
<pre><code>TestBean [name=this is a test custom tag]<br>
</code></pre>


<h2>测试代码地址</h2>
<pre><code>svn co https://hatter-source-code.googlecode.com/svn/trunk/tests/springcustomtag<br>
</code></pre>