package me.hatter.tests.mapdb;

import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class MapDBTest {

    public static void main(String[] args) {
        DB db = DBMaker.newFileDB(new File("testdb"))
                .closeOnJvmShutdown()
                .encryptionEnable("password")
                .make();
        // TODO
    }
}
