alias javalistsource="find . -name '*.java' -print > java.list"
alias javalistclass="find . -name '*.class' -print > classes.list"
alias usagejavac="echo 'usage: javac -d classes @java.list'"
alias usagejar="echo 'usage: jar cf my.jar @classes.list'"

