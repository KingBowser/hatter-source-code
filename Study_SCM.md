# SCM #
SCM (Source Control Management)
| **Name** | **Cmd** | **URL** | **Description** |
|:---------|:--------|:--------|:----------------|
| Subversion | `svn` | http://subversion.apache.org/ | Subversion (often abbreviated SVN, after the command name svn) is a software versioning and revision control system distributed under an open source license |
| Git | `git` | http://git-scm.com/ | Git is a distributed revision control and source code management (SCM) system with an emphasis on speed |
| Mercurial | `hg` | http://mercurial.selenic.com/ | Mercurial is a free, distributed source control management tool |
| CVS | `cvs` | http://www.nongnu.org/cvs/ | CVS is a version control system, an important component of Source Configuration Management (SCM) |

# `svn, hg, git` 命令对照 <sup>[1]</sup> #
| <font color='blue'><b>第一次下载，包括源码和版本库</b></font> |
|:----------------------------------------------------------------------------|
| `svn checkout http://path/to/repo repo_name` |
| `hg clone http://path/to/repo  repo_name` |
| `git glone http://path/to/repo repo_name, git glone git://path/to/repo repo_name` |
| <font color='blue'><b>下载服务器上最新的更新</b></font> |
| `svn update` |
| `hg pull && hg update -C` |
| `git pull` |
| <font color='blue'><b>检出某个修订版本</b></font> |
| `svn checkout -r <rev>` |
| `hg update -C -r <rev>` |
| `git reset –hard -r <rev>` |
| <font color='blue'><b>新增被跟踪文件</b></font> |
| `svn add /path/to/file` |
| `hg add /path/to/file` |
| `git add /path/to/file` |
| <font color='blue'><b>移除被跟踪文件</b></font> |
| `svn rm /path/to/file` |
| `hg remove /path/to/file` |
| `git rm /path/to/file` |
| <font color='blue'><b>生成补丁</b></font> |
| `svn diff  >patch_file` |
| `hg diff >patch_file` |
| `git diff >patch_file` |
| <font color='blue'><b>提交更改</b></font> |
| `svn commit` |
| `hg commit && hg push` |
| `git commit && git push` |
| <font color='blue'><b>查看当前状态</b></font> |
| `svn info` |
| `hg status` |
| `git status` |
| <font color='blue'><b>查看修订记录</b></font> |
| `svn log` |
| `hg log` |
| `git log` |
| <font color='blue'><b>启动服务器</b></font> |
| `svnserve -d` |
| `hg serve -p 8002 &` |
| `git daemon –base-path=/path/to/repo –export-all &` |

# SCM托管平台 #
| **Bitbucket** | https://bitbucket.org/ |
|:--------------|:-----------------------|
| **GitHub** | https://github.com/ |
| **Google Code** | https://code.google.com/hosting/ |
| **SourceForge** | http://sourceforge.net/ |
| **开源中国社区** | https://git.oschina.net/ |
| **GitCafe** | https://gitcafe.com/ |

# SCM托管软件 #
| **GitLab: Self Hosted Git Management Application** | http://gitlab.org/ |
|:---------------------------------------------------|:-------------------|

### 参考资料 ###
`[1].` http://www.1uu2.com/archives/992<br>