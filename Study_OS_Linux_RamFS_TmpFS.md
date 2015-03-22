Using ramfs or tmpfs you can allocate part of the physical memory to be used as a partition. You can mount this partition and start writing and reading files like a hard disk partition. Since you’ll be reading and writing to the RAM, it will be faster.

When a vital process becomes drastically slow because of disk writes, you can choose either ramfs or tmpfs file systems for writing files to the RAM.


Both tmpfs and ramfs mount will give you the power of fast reading and writing files from and to the primary memory. When you test this on a small file, you may not see a huge difference. You’ll notice the difference only when you write large amount of data to a file with some other processing overhead such as network.

# 1. How to mount Tmpfs #
```
# mkdir -p /mnt/tmp

# mount -t tmpfs -o size=20m tmpfs /mnt/tmp
```
The last line in the following df -k shows the above mounted /mnt/tmp tmpfs file system.
```
# df -k
Filesystem      1K-blocks  Used     Available Use%  Mounted on
/dev/sda2       32705400   5002488  26041576  17%   /
/dev/sda1       194442     18567    165836    11%   /boot
tmpfs           517320     0        517320    0%    /dev/shm
tmpfs           20480      0        20480     0%    /mnt/tmp
```

# 2. How to mount Ramfs #
```
# mkdir -p /mnt/ram

# mount -t ramfs -o size=20m ramfs /mnt/ram
```
The last line in the following mount command shows the above mounted /mnt/ram ramfs file system.
```
# mount
/dev/sda2 on / type ext3 (rw)
proc on /proc type proc (rw)
sysfs on /sys type sysfs (rw)
devpts on /dev/pts type devpts (rw,gid=5,mode=620)
/dev/sda1 on /boot type ext3 (rw)
tmpfs on /dev/shm type tmpfs (rw)
none on /proc/sys/fs/binfmt_misc type binfmt_misc (rw)
sunrpc on /var/lib/nfs/rpc_pipefs type rpc_pipefs (rw)
fusectl on /sys/fs/fuse/connections type fusectl (rw)
tmpfs on /mnt/tmp type tmpfs (rw,size=20m)
ramfs on /mnt/ram type ramfs (rw,size=20m)
```
You can mount ramfs and tmpfs during boot time by adding an entry to the /etc/fstab.

# 3. Ramfs vs Tmpfs #

Primarily both ramfs and tmpfs does the same thing with few minor differences.

Ramfs will grow dynamically.  So, you need control the process that writes the data to make sure ramfs doesn’t go above the available RAM size in the system. Let us say you have 2GB of RAM on your system and created a 1 GB ramfs and mounted as /tmp/ram. When the total size of the /tmp/ram crosses 1GB, you can still write data to it.  System will not stop you from writing data more than 1GB. However, when it goes above total RAM size of 2GB, the system may hang, as there is no place in the RAM to keep the data.
Tmpfs will not grow dynamically. It would not allow you to write more than the size you’ve specified while mounting the tmpfs. So, you don’t need to worry about controlling the process that writes the data to make sure tmpfs doesn’t go above the specified limit. It may give errors similar to “No space left on device”.
Tmpfs uses swap.
Ramfs does not use swap.

# 4. Disadvantages of Ramfs and Tmpfs #

Since both ramfs and tmpfs is writing to the system RAM, it would get deleted once the system gets rebooted, or crashed. So, you should write a process to pick up the data from ramfs/tmpfs to disk in periodic intervals. You can also write a process to write down the data from ramfs/tmpfs to disk while the system is shutting down. But, this will not help you in the time of system crash.

Table: Comparison of ramfs and tmpfs

| **Experimentation** | **Tmpfs** | **Ramfs** |
|:--------------------|:----------|:----------|
| Fill maximum space and continue writing | Will display error | Will continue writing |
| Fixed Size | Yes | No |
| Uses Swap | Yes | No |
| Volatile Storage | Yes | Yes |

If you want your process to write faster, opting for tmpfs is a better choice with precautions about the system crash.


### 参考资料 ###
`[1].` http://www.thegeekstuff.com/2008/11/overview-of-ramfs-and-tmpfs-on-linux/<br>