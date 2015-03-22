# Introduction #

Find invoke method in class/jar files.


# Usage #

  1. Install
    * Check out
> > > svn co http://hatter-source-code.googlecode.com/svn/trunk/javatools/invokefind/
    * Compile & Install
> > > ant install
    * Download invokefind.jar and run is also ok.
```
   wget https://hatter-source-code.googlecode.com/svn/trunk/javatools/invokefind/invokefind.jar
   java -jar invokefind.jar
```
  1. Usage
```
Usage:
  invokefind [flags] <args>
  java -jar invokefind [flags] <args>
  java -cp invokefind.jar invokefind [flags] <args>
    -t <dir>       target dir[default user.dir]
    -o <file>      output file
    -ig <str>      ignore[contains]
    -igc <str>     ignore class[contains]
    -igm <str>     ignore method[equals]
    --vf           print visit file
    --vc           print visit class
    --vm           print visit method
    --vs           print summary
    --noins        print no instructions methods
Sample:
  invokefind --vc "xstream.<init>" string.intern
```