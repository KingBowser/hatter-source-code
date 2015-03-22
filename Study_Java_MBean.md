An MXBean is a type of MBean that references only a predefined set of data types. In this way, you can be sure that your MBean will be usable by any client, including remote clients, without any requirement that the client have access to model-specific classes representing the types of your MBeans. MXBeans provide a convenient way to bundle related values together, without requiring clients to be specially configured to handle the bundles.

通过加载 `management-agent.jar` 在目标JVM上打开本地JMX服务。

```
MBeanServer server = ManagementFactory.getPlatformMBeanServer();
```


### 参考资料 ###
`[1].` http://docs.oracle.com/javase/tutorial/jmx/mbeans/mxbeans.html<br>