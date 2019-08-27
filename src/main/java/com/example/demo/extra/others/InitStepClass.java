package com.example.demo.extra.others;

import org.junit.Test;

public class InitStepClass {
    @Test
    public void temp() {
        new b();
    }
}

class a{
    static{
        System.out.println("a stataic{}");
    }
    {
        System.out.println("a {}");
    }
    public static String a = "1";
    static{
        System.out.println("a stataic = " + a);
    }
    public a(){
        System.out.println("a()");
    }
    public static void aa(){
        System.out.println("aa");
    }
}

class b extends com.example.demo.extra.others.a {
    static{
        System.out.println("b stataic{}");
    }
    {
        System.out.println("b {}");
    }
    public static String b = "2";
    static{
        System.out.println("b stataic = " + b);
    }
    public b(){
        System.out.println("b()");
    }
    public static void bb(){
        System.out.println("bb");
    }
}

