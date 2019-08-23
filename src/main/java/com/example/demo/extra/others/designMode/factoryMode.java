package com.example.demo.extra.others.designMode;

/**
 * 工厂模式
 */
public class factoryMode {

    public static void main(String[] args) {
        CarFactory carA = new BikeFactory();
        carA.getCar().goTOWork();

        CarFactory carB = new BusFactory();
        carB.getCar().goTOWork();
    }

}

interface Car {
    void goTOWork();
}

class ByBike implements Car {
    @Override
    public void goTOWork() {
        System.out.println("骑车上班");
    }
}

class ByBus implements Car {
    @Override
    public void goTOWork() {
        System.out.println("坐车上班");
    }
}

interface CarFactory {
    Car getCar();
}

class BikeFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new ByBike();
    }
}

class BusFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new ByBus();
    }
}