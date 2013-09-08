package me.hatter.tools.resourceproxy.dbutils.dataaccess;

public interface RecordProcessor<T> {

    void process(int index, T record) throws Exception;
}
