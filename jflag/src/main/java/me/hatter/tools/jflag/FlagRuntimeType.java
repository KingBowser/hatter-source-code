package me.hatter.tools.jflag;

public enum FlagRuntimeType {

    _product, _lp64_product, _product_pd, _product_rw,

    _manageable, _diagnostic,

    _develop, _develop_pd,

    _notproduct, _experimental;

    public String getName() {
        return this.name().substring(1);
    }

    public String toString() {
        return this.getName();
    }
}
