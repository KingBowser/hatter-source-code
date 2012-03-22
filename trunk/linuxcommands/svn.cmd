alias svnrmall="svn st | grep '^!' | awk '{print \$2}' | xargs svn rm"
alias svnaddall="svn st | grep '^?' | awk '{print \$2}' | xargs svn add"

