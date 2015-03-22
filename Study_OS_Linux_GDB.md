# GDB: The GNU Project Debugger #
http://www.gnu.org/software/gdb/

GDB, the GNU Project debugger, allows you to see what is going on `inside' another program while it executes -- or what another program was doing at the moment it crashed.




gdb pstack

```
gdb -p pid

thread apply all bt


gdb \
    -ex "set pagination 0" \
    -ex "thread apply all bt" \
    --batch -p $(pidof mysqld)
```