
```
$ git clone <repo> -b gh-pages
```


```
$ cd repository

$ git checkout --orphan gh-pages
# Creates our branch, without any parents (it's an orphan!)
# Switched to a new branch 'gh-pages'

$ git rm -rf .
# Remove all files from the old working tree
# rm '.gitignore'
```

```
$ git config user.name 'Hatter Jiang'
$ git config user.email 'jht5945@gmail.com'
```


```
$ echo "My GitHub Page" > index.html
$ git add index.html
$ git commit -a -m "First pages commit"
$ git push origin gh-pages
```


```
$ git branch --set-upstream-to=origin/gh-pages
```

## [Setting up a custom domain with GitHub Pages](https://help.github.com/articles/setting-up-a-custom-domain-with-github-pages) ##

## http://githubrank.com/ ##

## http://jlord.github.io/git-it/ ##


### 参考资料 ###
`[1].` https://help.github.com/articles/creating-project-pages-manually<br>
<code>[2].</code> <a href='https://help.github.com/articles/setting-up-a-custom-domain-with-pages'>https://help.github.com/articles/setting-up-a-custom-domain-with-pages</a><br>