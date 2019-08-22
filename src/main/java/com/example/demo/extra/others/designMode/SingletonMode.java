package com.example.demo.extra.others.designMode;

/**
 * 单例模式
 */
public class SingletonMode {

    public static void main(String[] args) {
        //饿汉模式
        HungryMode.Instance.test();
        //懒汉模式
        LarzyMode.getInstance().test();
        //静态内部类 单例模式
        StaticInnerClassMode.getInstance().test();
    }

}

/**
 * 饿汉模式
 */
class HungryMode {
    public static HungryMode Instance = new HungryMode();

    public HungryMode() {
    }

    public void test() {
        System.out.println("饿汉模式");
    }
}

/**
 * 懒汉模式
 */
class LarzyMode {
    public static LarzyMode Instance = null;

    public LarzyMode() {
    }

    public static LarzyMode getInstance() {
        if (Instance == null) {
            synchronized (LarzyMode.class) {
                if (Instance == null) {
                    Instance = new LarzyMode();
                }
            }
        }
        return Instance;
    }

    public void test() {
        System.out.println("懒汉模式");
    }
}

/**
 * 静态内部类 单例模式
 */
class StaticInnerClassMode {
    public StaticInnerClassMode() {
    }


    public static StaticInnerClassMode getInstance() {
        return SingletonHolder.Instance;
    }

    private static class SingletonHolder {
        private static final StaticInnerClassMode Instance = new StaticInnerClassMode();
    }

    public void test() {
        System.out.println("静态内部类单例模式");
    }
}
