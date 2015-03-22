## perftools安装、分析过程 ##
  1. http://download.savannah.gnu.org/releases/libunwind/libunwind-0.99-beta.tar.gz
```
configure; make; sudo make install
```
  1. http://google-perftools.googlecode.com/files/google-perftools-1.8.1.tar.gz
```
configure –prefix=/home/user/perftools; make; sudo make install
```
  1. export
```
export LD_PRELOAD=/home/hadoop/perftools/lib/libtcmalloc.so
export HEAPPROFILE=/home/user/perftools/test
```
  1. 修改 `lc_config:sudo vi /etc/ld.so.conf.d/usr_local_lib.conf`，加入/usr/local/lib(libunwind的lib所在目录)
  1. 执行sudo /sbin/ldconfig，使libunwind生效
  1. 重启应用程序，应用程序会在/home/user/perftools/生成heap，名字包含pid
  1. `pprof -text $JAVA_HOME/bin/java /home/admin/perftools/test_23106` <br>(注意如果配置crontab则ppof需要全路径去执行)<br>
<ol><li>去发现那个占用内存最多类和方法名</li></ol>

<b>Performance analysis tools based on Linux perf_events (aka perf) and ftrace - <a href='https://github.com/brendangregg/perf-tools'>https://github.com/brendangregg/perf-tools</a></b>


<h3>参考资料</h3>
<code>[1].</code> <a href='http://impzx.com/%E5%A0%86%E5%A4%96%E5%86%85%E5%AD%98%E6%B3%84%E9%9C%B2%E7%9A%84%E5%88%86%E6%9E%90%E8%BF%87%E7%A8%8B/'>http://impzx.com/%E5%A0%86%E5%A4%96%E5%86%85%E5%AD%98%E6%B3%84%E9%9C%B2%E7%9A%84%E5%88%86%E6%9E%90%E8%BF%87%E7%A8%8B/</a><br>