package com.example.demo.extra.others.designMode;

/**
 * 桥接模式
 */
public class BridgeMode {

    public static void main(String[] args) {
        HuaWei huaWei = new HuaWei();
        huaWei.setMemory(new Add4G());
        huaWei.buyPhone();

        Mi mi = new Mi();
        mi.setMemory(new Add8G());
        mi.buyPhone();
    }

}

interface Memory {
    void addMemory();
}

class Add4G implements Memory {
    @Override
    public void addMemory() {
        System.out.print("购入4G内存");
    }
}

class Add8G implements Memory {
    @Override
    public void addMemory() {
        System.out.print("购入8G内存");
    }
}

abstract class Phone {
    public Memory memory;

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public abstract void buyPhone();
}

class HuaWei extends Phone {
    @Override
    public void buyPhone() {
        memory.addMemory();
        System.out.println("华为");
    }
}

class Mi extends Phone {
    @Override
    public void buyPhone() {
        memory.addMemory();
        System.out.println("小米");
    }
}