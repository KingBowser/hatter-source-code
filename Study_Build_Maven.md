
```
mvn dependency:tree -Dverbose

mvn help:effective-pom

mvn dependency:copy-dependencies -DincludeScope=test|compile
```

Maven debug surefire test:
```
-Dmaven.surefire.debug
```

My maven aliases:
```
alias mci='mvn clean install -Dmaven.test.skip'
alias mcis='mvn clean install -Dmaven.test.skip -Dcheck.parent.skip'
alias mee='mvn eclipse:clean eclipse:eclipse'
alias mees='mvn eclipse:clean eclipse:eclipse -Dcheck.parent.skip'
alias mct='mvn clean test'
```

Debug maven:
```
export MAVEN_OPTS='-Xdebug -Xrunjdwp:transport=dt_socket,address=4000,server=y,suspend=y'
```

pom.xml exclude gid/aid:
```
<dependency>
  <groupId>GID_</groupId>
  <artifactId>AID_</artifactId>
  <exclusions>
    <exclusion>
      <groupId>GID_</groupId>
      <artifactId>AID_</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```


使用 maven shade plugin 解决 jar 或类的多版本冲突 <sup>[1]</sup>


### 参考资料 ###
`[1].` http://kiminotes.iteye.com/blog/1695344<br>