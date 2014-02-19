!!EXPLAIN
Reorderings:::Reorderings(指令重排)

For a compiler writer, the JMM mainly consists of rules disallowing reorderings of certain instructions that access fields (where "fields" include array elements) as well as monitors (locks).
对于写编译器的人来说，Java内存模型，......


!!#volatile-and-monitors# Volatiles and Monitors

The main JMM rules for volatiles and monitors can be viewed as a matrix with cells indicating that you cannot reorder instructions associated with particular sequences of bytecodes. This table is not itself the JMM specification; it is just a useful way of viewing its main consequences for compilers and runtime systems.



