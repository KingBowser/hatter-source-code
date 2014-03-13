# Hatter Jiang's linux commands.

# maven
alias mci='mvn clean install -Dmaven.test.skip'
alias mcp='mvn clean package -Dmaven.test.skip'
alias mct='mvn clean test'
alias mee='mvn eclipse:clean eclipse:eclipse'

# svn
alias svnrmall="svn st | grep '^!' | awk '{print \$2}' | xargs svn rm"
alias svnaddall="svn st | grep '^?' | awk '{print \$2}' | xargs svn add"

# java
alias javalistsource="find . -name '*.java' -print > java.list"
alias javalistclass="find . -name '*.class' -print > classes.list"
alias usagejavac="echo 'usage: javac -d classes @java.list'"
alias usagejar="echo 'usage: jar cf my.jar @classes.list'"

