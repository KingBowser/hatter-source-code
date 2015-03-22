最近接手一个应用，该用使用Cobar连接数据库的架构，平时相处相当和谐，但由于Cobar设计的一个问题带来了悲剧，当使用多台Cobar服务器连接数据库时，每台Cobar服务器都各自和连接的数据之前做心跳检测，所以在某些特殊场景下多台Cobar连接的主/备数据库不一致【见下图】。

然而，我们可以使用[Zookeeper](http://zookeeper.apache.org/)协调Cobar服务器之间的状态，也可以扩展客户端数据源直接接到Zookeeper集群，从而抛弃F5/LVS设备。【P.S. F5设备超贵，即使走F5也带来不必要的性能损耗】

# Client/F5/Cobar的架构 #
![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/cobar/client-f5-cobar-db.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/cobar/client-f5-cobar-db.png)

# 去F5 增加Zookeeper后的架构 #
![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/cobar/zk-client-cobar-db.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/cobar/zk-client-cobar-db.png)

# 数据库配置 #
## 建表及用户SQL ##
```
create schema dbtest1;
create schema dbtest2;

create table test (id bigint unsigned, value varchar(1000), primary key (id));

grant select,insert,update,delete on dbtest1.* to test@'%' identified by  'test'; 
grant select,insert,update,delete on dbtest2.* to test@'%' identified by  'test';
```

## 表结构 ##
```
mysql> desc test;
+-------+---------------------+------+-----+---------+-------+
| Field | Type                | Null | Key | Default | Extra |
+-------+---------------------+------+-----+---------+-------+
| id    | bigint(20) unsigned | NO   | PRI | 0       |       |
| value | varchar(1000)       | YES  |     | NULL    |       |
+-------+---------------------+------+-----+---------+-------+
```

## 数据库数据【未配置数据库同步】 ##
```
// 数据库A, Schema: dbtest1
mysql> select * from test;
+----+--------+
| id | value  |
+----+--------+
| 1  | hatter |
+----+--------+

// 数据库B, Schema: dbtest1
mysql> select * from test;
+----+-------+
| id | value |
+----+-------+
| 1  | jiang |
+----+-------+
```

# Cobar配置 #
## server.xml配置【修改代码后新增加的，Cobar版本为1.2.6】 ##
```
    <!-- Zookeeper配置 -->
    <property name="zkConfig">c1.hatter.zj.cn:2181,b1.hatter.zj.cn:2181,a1.hatter.zj.cn:2181/</property>
    <!-- 当前Cobar集群名 -->
    <property name="clusterName">cluster1</property>
    <!-- 当前Cobar的IP及端口号 -->
    <property name="ipAndPort">c1.hatter.zj.cn:8066</property>
```

## schema.xml配置 ##
```
<cobar:schema xmlns:cobar="http://cobar.alibaba.com/">

  <schema name="dbtest" dataNode="dnTest1">
    <table name="test" dataNode="dnTest1,dnTest2" rule="rule1" />
  </schema>

  <dataNode name="dnTest1">
    <property name="dataSource">
      <dataSourceRef>dsTest[0]</dataSourceRef> <!-- 主库 -->
      <dataSourceRef>dsTest[2]</dataSourceRef> <!-- 备库 -->
    </property>
    <property name="heartbeat">select 1</property>
  </dataNode>
  <dataNode name="dnTest2">
    <property name="dataSource">
      <dataSourceRef>dsTest[1]</dataSourceRef>
      <dataSourceRef>dsTest[3]</dataSourceRef>
    </property>
    <property name="heartbeat">select 1</property>
  </dataNode>

  <dataSource name="dsTest" type="mysql">
    <property name="location">
      <location>a2.hatter.zj.cn:3306/dbtest1</location>
      <location>a2.hatter.zj.cn:3306/dbtest2</location>
      <location>a3.hatter.zj.cn:3306/dbtest1</location>
      <location>a3.hatter.zj.cn:3306/dbtest2</location>
    </property>
    <property name="user">test</property>
    <property name="password">test</property>
    <property name="sqlMode">STRICT_TRANS_TABLES</property>
  </dataSource>

</cobar:schema>
```

## 测试代码 ##
```
public class ZookeeperCobarDruidDatasourceTest {

    public static void main(String[] args) {
        ZookeeperCobarDruidDataSource dataSource = new ZookeeperCobarDruidDataSource();
        dataSource.setUrl("jdbc:cobarzk://c1.hatter.zj.cn:2181,b1.hatter.zj.cn:2181/;cluster1:dbtest");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");

        for (int i = 0; i < 1000; i++) {
            try {
                System.out.println("[INFO] Round: " + i);
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select * from test");
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    System.out.println("[INFO] " + rs.getLong("id") + ":" + rs.getString("value"));
                }
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
        }
    }
}
```

# 程序输出 #
## Cobar选举Leader输出 ##
另一台Cobar服务器关闭，当前Cobar服务器切换状态，从FOLLOWER变成了LEADER：
```
19:37:17,460 INFO  ZK cobar cluster path: /cobar/cluster1
19:37:17,485 INFO  Register server: /cobar/cluster1/servers/server0000000036, value: c1.hatter.zj.cn:8066
19:37:17,493 INFO  /cobar/cluster1/servers: [server0000000035, server0000000036]/ /cobar/cluster1/servers/server0000000036
19:37:17,493 INFO  UPDATE ROLE: FOLLOWER // 当前状态为FOLLOWER
19:37:24,388 INFO  /cobar/cluster1/servers: [server0000000036]/ /cobar/cluster1/servers/server0000000036
19:37:24,388 INFO  UPDATE ROLE: LEADER // 当前状态变成了LEADER
```

## Zookeeper数据 ##
两台Cobar正常服务时Zookeeper中的结构：
```
'-+cobar
  '-+cluster1
    '-+databases
      '-+dnTest1 // DataNode，结点包含当前的主机序号
      '-+dnTest2
    '-+servers
      '-+server0000000036 // CobarNode，结点包含当前的Cobar服务器的IP地址和端口号
      '-+server0000000038
```

## 启停MySQL ##
```
// 启动备机
#service mysql start
Starting MySQL..                                           [  OK  ]

// 停主机
#service mysql stop 
Shutting down MySQL....                                    [  OK  ]
```

## Cobar切换主/备输出 ##
主机关闭时Cobar服务器接收到主/备切换消息，此时该Cobar集群的服务器都收到该方法：
```
19:40:07,407 ERROR #!DN_SWITCH#[name=dnTest2,result=[0->1],reason=SYNC LEADER]
19:40:07,407 ERROR #!DN_SWITCH#[name=dnTest2,result=[0->1],reason=SYNC LEADER]
19:40:07,409 INFO  Data node switch: dnTest1 to: 1 for running sync leader
19:40:07,409 ERROR #!DN_SWITCH#[name=dnTest1,result=[0->1],reason=SYNC LEADER]
19:40:07,409 ERROR #!DN_SWITCH#[name=dnTest1,result=[0->1],reason=SYNC LEADER]
```

## 客户测试程序输出 ##
客户端测试程序打印的日志：
```
[INFO] Round: 0
[WARN] Add server: c2.hatter.zj.cn:8066 // 初始化时找到了两台Cobar服务器
[WARN] Add server: c1.hatter.zj.cn:8066
[INFO] 1:hatter // 读到的A数据库的数据
[INFO] Round: 1
[INFO] 1:hatter
[INFO] Round: 2
[INFO] 1:hatter
[WARN] Remove server: c2.hatter.zj.cn:8066 // 关闭了一台Cobar服务器
[INFO] Round: 3
[INFO] 1:hatter
[INFO] Round: 4
[INFO] 1:hatter
[WARN] Add server: c2.hatter.zj.cn:8066 // 重新启动了刚才关闭的Cobar服务器
[INFO] Round: 5
[INFO] 1:hatter
[INFO] Round: 6
[INFO] 1:hatter
[INFO] Round: 7
[ERROR] Query execution was interrupted // 主备切换过程中产生了一个错误
[INFO] Round: 8
[INFO] 1:jiang // 读到的B数据库的数据
[INFO] Round: 9
[INFO] 1:jiang
[INFO] Round: 10
[INFO] 1:jiang
[INFO] Round: 11
[INFO] 1:jiang
[INFO] Round: 12
[INFO] 1:jiang
```

### 代码清单 ###
`[1].` [cobar-server-1.2.6-hatter.tar.gz](http://code.google.com/p/hatter-source-code/downloads/detail?name=cobar-server-1.2.6-hatter.tar.gz&can=2) 测试用的Cobar服务器<br>
<code>[2].</code> <a href='http://code.google.com/p/hatter-source-code/downloads/detail?name=cobar-1.2.6-hatter-sources.tar.gz&can=2'>cobar-1.2.6-hatter-sources.tar.gz</a> 修改后的源代码【代码写的很挫，仅用于实验/测试目的】<br>
<code>[3].</code> <a href='http://hatter-source-code.googlecode.com/svn/trunk/tests/zookeeper/src/main/java/me/hatter/tests/datasource/ZookeeperCobarConfigLoader.java'>ZookeeperCobarConfigLoader.java</a><br>
<code>[4].</code> <a href='http://hatter-source-code.googlecode.com/svn/trunk/tests/zookeeper/src/main/java/me/hatter/tests/datasource/ZookeeperCobarDruidDataSource.java'>ZookeeperCobarDruidDataSource.java</a><br>
<code>[5].</code> <a href='http://hatter-source-code.googlecode.com/svn/trunk/tests/zookeeper/src/main/java/me/hatter/tests/datasource/ZookeeperCobarDruidDatasourceTest.java'>ZookeeperCobarDruidDatasourceTest.java</a><br>