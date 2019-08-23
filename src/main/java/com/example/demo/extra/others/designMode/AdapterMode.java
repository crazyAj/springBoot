package com.example.demo.extra.others.designMode;

/**
 * 设计者模式
 */
public class AdapterMode implements PS2{

    private USB usb;

    public static void main(String[] args) {
        new AdapterMode(new User()).isPS2();
    }

    public AdapterMode(USB usb) {
        this.usb = usb;
    }

    @Override
    public void isPS2() {
        usb.isUSB();
    }
}

interface PS2{
    void isPS2();
}

interface USB{
    void isUSB();
}

class User implements USB{
    @Override
    public void isUSB() {
        System.out.println("USB");
    }
}