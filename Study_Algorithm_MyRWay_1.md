![http://latex.codecogs.com/gif.latex?cos(\theta)=\frac{\sum_{i=1}^{n}Ai*Bi}{\sqrt{\sum_{i=1}^{n}(Ai)^{2}}*\sqrt{\sum_{i=1}^{n}(Bi)^{2}}}&__.gif](http://latex.codecogs.com/gif.latex?cos(\theta)=\frac{\sum_{i=1}^{n}Ai*Bi}{\sqrt{\sum_{i=1}^{n}(Ai)^{2}}*\sqrt{\sum_{i=1}^{n}(Bi)^{2}}}&__.gif)

```
a <- c(1,3,5)
b <- c(2,4,6)
x <- sum(a*b)/(sqrt(sum(a^2))*sqrt(sum(b^2)))
```

```
## 用户,Item,Score
x <- read.csv(textConnection("
  1,101,5.0
  1,102,3.0
  1,103,2.5
  2,101,2.0
  2,102,2.5
  2,103,5.0
  2,104,2.0
  3,101,2.5
  3,104,4.0
  3,105,4.5
  3,107,5.0
  4,101,5.0
  4,103,3.0
  4,104,4.5
  4,106,4.0
  5,101,4.0
  5,102,3.0
  5,103,2.0
  5,104,4.0
  5,105,3.5
  5,106,4.0  
"), header = FALSE)
```


```
rn <- sort(unique(x$V1))
cn <- sort(unique(x$V2))
library(Matrix)
y <- sparseMatrix(i = match(x$V1, rn), j = match(x$V2, cn), x = x$V3)    
h <- as.matrix(dist(t(y), diag = TRUE)^2)
h <- 1/(1 + h)
diag(h) <- 0
## 推荐引擎的四个参数：相似矩阵、购买了什么，得分是多少，推荐几个
recommend <- function(h = h, k = c(3, 5), score = c(4, 5), m = 1) {
  if(length(k) > 1) {
    v <- colSums(h[k,] * score)/colSums(h[k,])
  } else {
    v <- h[k,]
  }
  v[k] <- 0
  od <- order(v, decreasing=T)[1:m]
  return(list(colnames(h)[od], v[od]))
}
recommend(h, k = c(1,3), score = c(1, 5),  m = 2)
```



### 参考资料 ###
`[1].` http://www.bjt.name/2013/06/recommendation-system/<br>
<code>[2].</code> <a href='http://zh.wikipedia.org/wiki/%E4%BD%99%E5%BC%A6%E7%9B%B8%E4%BC%BC%E6%80%A7'>http://zh.wikipedia.org/wiki/%E4%BD%99%E5%BC%A6%E7%9B%B8%E4%BC%BC%E6%80%A7</a><br>