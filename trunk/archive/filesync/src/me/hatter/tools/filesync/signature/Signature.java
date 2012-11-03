package me.hatter.tools.filesync.signature;

public interface Signature<T> {

    String sign(T object);
}
