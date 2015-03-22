我的一个 `build.xml` 示例：
```
<project name="commons" default="compile" basedir=".">

	<!-- Output Directories -->
	<property name="classes.dir" value="classes" />
	<property name="config.dir" value="main/conf" />

	<!-- Source Directories -->
	<property name="src.dir" value="main/java" />
	<property name="lib.dir" value="lib" />

	<!--
	<property name="jar.main" value="**.**.**" />
	<property name="jar.manifest" value="jar.manifest" />
	-->

	<!-- Class Path -->
	<path id="classpath">
		<fileset dir="${lib.dir}" includes="**/*.jar" />
	</path>

	<!-- Targets -->
	<target name="prepare">
		<mkdir dir="${classes.dir}" />
	</target>

	<target name="compile" depends="prepare">
		<javac srcdir="${src.dir}" destdir="${classes.dir}" source="1.6" target="1.6" 
                       debug="true" debuglevel="lines,vars,source">
			<classpath>
				<path refid="classpath" />
			</classpath>
		</javac>
		<!--
		<manifest file="${jar.manifest}">
			<attribute name="Main-Class" value="${jar.main}" />
		</manifest>
		-->
		<jar destfile="commons-1.0.jar"> <!-- manifest="${jar.manifest}"> -->
			<fileset dir="${classes.dir}" includes="**/*.class" />
			<fileset dir="${src.dir}" includes="**/*.java" />
			<fileset dir="${config.dir}" includes="**/*.*" />
		</jar>
		<delete dir="${classes.dir}" />
		<!--
		<delete file="${jar.manifest}" />
		-->
	</target>
</project>
```