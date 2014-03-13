package me.hatter.tools.xmpprobots.entity;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;

@Table(name = "user", defaultAllFields = true)
public class User {

    @Field(pk = true)
    private Long   id;
    private String account;
    private String name;
    private String businesses;
}
