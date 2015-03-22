# Introduction #

A light weight JSSP container.

# Details #

Get JsspServer:
```
svn co http://hatter-source-code.googlecode.com/svn/trunk/jsspserver/
gradle
```

Then you will get `jsspserver-N.N.jar`.

Now you can programming with jsspserver.

Sample:
```
svn co http://hatter-source-code.googlecode.com/svn/trunk/hostsmanager/ hostsmanager
```

## Programming with winstone(servlet container) ##
  * http://winstone.sourceforge.net/
  * https://hatter-source-code.googlecode.com/svn/trunk/winstonejsspserver

web.xml sample:
```
<?xml version="1.0" encoding="UTF-8" ?>
<web-app version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
        http://java.sun.com/xml/ns/j2ee  http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd
    ">
	<filter>
		<filter-name>welcome</filter-name>
		<filter-class>filter.welcome</filter-class>
	</filter>
	<filter>
		<filter-name>jssp</filter-name>
		<filter-class>filter.jssp</filter-class>
	</filter>
	<filter>
		<filter-name>jsonp</filter-name>
		<filter-class>filter.jsonp</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>welcome</filter-name>
		<url-pattern>/</url-pattern>
	</filter-mapping>
	<filter-mapping>
		<filter-name>jssp</filter-name>
		<url-pattern>*.jssp</url-pattern>
	</filter-mapping>
	<filter-mapping>
		<filter-name>jsonp</filter-name>
		<url-pattern>*.jsonp</url-pattern>
	</filter-mapping>

	<welcome-file-list>
		<welcome-file>/index.jssp</welcome-file>
	</welcome-file-list>
</web-app>
```