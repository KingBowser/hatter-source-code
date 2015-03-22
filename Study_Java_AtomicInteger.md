JDK 8 - AtomicInteger
```
public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, valueOffset, 1);
}
```
JDK7 - AtomicInteger
```
 public final int getAndIncrement() {
        for (;;) {
            int current = get();
            int next = current + 1;
            if (compareAndSet(current, next))
                return current;
        }
    }
```


---


JDK 8
```
  0x0000000002cf49c7: mov    %rbp,0x10(%rsp)
  0x0000000002cf49cc: mov    $0x1,%eax
  0x0000000002cf49d1: lock xadd %eax,0xc(%rdx)  ;*invokevirtual getAndAddInt
                                                ; - java.util.concurrent.atomic.AtomicInteger::incrementAndGet@8 (line 186)
```

JDK 7
```
  0x0000000002c207f5: lock cmpxchg %r8d,0xc(%rdx)
  0x0000000002c207fb: sete   %r11b
  0x0000000002c207ff: movzbl %r11b,%r11d        ;*invokevirtual compareAndSwapInt
                                                ; - java.util.concurrent.atomic.AtomicInteger::compareAndSet@9 (line 135)
                                                ; - java.util.concurrent.atomic.AtomicInteger::incrementAndGet@12 (line 206)
```



http://ashkrit.blogspot.com/2014/02/atomicinteger-java-7-vs-java-8.html