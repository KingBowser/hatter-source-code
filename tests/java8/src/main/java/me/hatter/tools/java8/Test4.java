package me.hatter.tools.java8;

/**
 * Created by hatterjiang on 4/3/14.
 */
public class Test4 {

    @FunctionalInterface
    public static interface Execute {
        void exec();

        default public Execute than(Execute _exec) {
            return () -> {
                this.exec();
                _exec.exec();
            };
        }
    }

    public static void main(String[] args) {
        Execute e = () -> System.out.println("Hello");
        e.exec();
        System.out.println("----------");
        e.than(() -> System.out.println("World")).exec();
        System.out.println("----------");
        e.than(() -> System.out.println("1111")).than(() -> System.out.println("2222")).exec();
        System.out.println("----------");
        e.than(() -> System.out.println("1111")).than(System.out::println).than(() -> System.out.println("2222")).exec();
    }
}
