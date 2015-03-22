## 官方网站 ##
http://www.gradle.org/ ---- Gradle - Build Automation Evolved

Gradle 是使用 `groovy` 作为配制文件的构建工具，同时也可以无缝集成 `ant`  和 `maven`。

和 `ant, maven` 类似，在文件夹放置文件 `build.gradle` 就可以自动构建。

下面这个是我用来构建Java小工具的Sample：
```
apply plugin: 'java'
apply plugin: 'eclipse'

def baseProjectName = 'testapp'
def shellCommandName = baseProjectName
def eclipseProjectName = baseProjectName
def eclipseProjectComment = 'my test application'
def jarManifestMainClass = 'me.hatter.tools.xxx.Main'
def installTargetBin = '/usr/local/bin/'

archivesBaseName = baseProjectName
sourceCompatibility = 1.5
targetCompatibility = 1.5

task initapp << {
  file('lib').mkdirs()
  file('src/main/java').mkdirs()
  file('src/main/resources').mkdirs()
  file('src/test/java').mkdirs()
  file('src/test/resources').mkdirs()
  println 'Init app finished [lib, src/main/java|resources, src/test/java|resources].'
}

task install << {
  def installBinDirLib = "${baseProjectName}_lib"
  if (!file("${installTargetBin}${installBinDirLib}").exists()) {
    file("${installTargetBin}${installBinDirLib}").mkdirs()
  }
  copy {
    from ('lib')
    into "${installTargetBin}${installBinDirLib}/"
    include '*.jar'
  }
  copy {
    from ('build/libs')
    into "${installTargetBin}${installBinDirLib}/"
    include '*.jar'
  }
  copy {
    from ('.')
    into "${installTargetBin}"
    include shellCommandName
  }
  ant.chmod(file: "${installTargetBin}${shellCommandName}", perm: 'a+x')
}

task packall << {
  def packtempclasses = "packtempclasses"
  def packtempmanifest = "packtempmanifest"
  def libs = ant.path {
    fileset(dir: 'lib', includes: '*.jar')
    fileset(dir: 'build/libs', includes: '*.jar')
  }
  libs.list().each {
    ant.unzip(dest: packtempclasses, src: it) 
  }
  def antmanifest = ant.manifest(file: packtempmanifest) {
    attribute(name: 'Main-Class', value: jarManifestMainClass)
  }
  ant.jar(destfile: "${baseProjectName}all.jar", manifest: packtempmanifest) {
    fileset(dir: packtempclasses, includes: '**/*.*')
  }
  ant.delete(dir: packtempclasses)
  ant.delete(file: packtempmanifest)
}

dependencies {
    compile files(fileTree(dir: 'lib', includes: ['*.jar'], excludes: ['*-sources.jar', '*-javadoc.jar']))
    // compile files(file(System.properties['java.home'].replaceAll('\\/jre(\\/)?$', "") + "/lib/sa-jdi.jar"))
}

eclipse {
  project {
    name = eclipseProjectName
    comment = eclipseProjectComment
  }
  classpath {
    defaultOutputDir = file('classes')
  }
}

jar {
  manifest {
    attributes('Main-Class': jarManifestMainClass)
  }
}
```

_https://hatter-source-code.googlecode.com/svn/trunk/gradle/build.gradle_

跳过单元测试：
```
gradle build -x test 
```