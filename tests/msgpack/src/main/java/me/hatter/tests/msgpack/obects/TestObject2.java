package me.hatter.tests.msgpack.obects;

import java.io.Serializable;

public class TestObject2 implements Serializable {

    private static final long serialVersionUID = -4464065182228014275L;
    private int               id;
    private long              id2;
    private float             id3;
    private double            id4;
    private String            id5;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public Float getId3() {
        return id3;
    }

    public void setId3(Float id3) {
        this.id3 = id3;
    }

    public Double getId4() {
        return id4;
    }

    public void setId4(Double id4) {
        this.id4 = id4;
    }

    public String getId5() {
        return id5;
    }

    public void setId5(String id5) {
        this.id5 = id5;
    }
}
