**Preface: Over the 10+ years since this was initially written, many processor and language memory model specifications and issues have become clearer and better understood. And many have not. While this guide is maintained to remain accurate, it is incomplete about some of these evolving details. For more extensive coverage, see especially the work of Peter Sewell and the [!Cambridge Relaxed Memory Concurrency Group](http://www.cl.cam.ac.uk/~pes20/weakmemory/index.html)**
**前言：从最初开始写这个文件以来的过去10几年里，很多处理器和语言内存模型标准和问题已经被描述的很清晰且更容易被读懂。**


This is an unofficial guide to implementing the new Java Memory Model (JMM) specified by JSR-133 . It provides at most brief backgrounds about why various rules exist, instead concentrating on their consequences for compilers and JVMs with respect to instruction reorderings, multiprocessor barrier instructions, and atomic operations. It includes a set of recommended recipes for complying to JSR-133. This guide is "unofficial" because it includes interpretations of particular processor properties and specifications. We cannot guarantee that the intepretations are correct. Also, processor specifications and implementations may change over time.


