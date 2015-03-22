清理MacOS X中右键菜单中的重复的应用名称：
```
/System/Library/Frameworks/CoreServices.framework/Frameworks/LaunchServices.framework/Support/lsregister -kill -r -domain local -domain system -domain user
```

VNC Screen Sharing on OS X gives a white screen:
```
In Finder, go to /System/Library/CoreServices/Screen Sharing.app
Right click & “Get Info” on the application
Check the box “Open in 32-bit mode”.
```

Make APP:
```
PNG 128*128
make app url: http://hatter-source-code.googlecode.com/svn/trunk/macosx/makeapp.sh
```


Mac OS X挂载ISO镜像:
```
hdiutil mount sample.iso
```