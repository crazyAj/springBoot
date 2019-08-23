package com.example.demo.extra.others.designMode;

/**
 * 命令模式
 */
public class CommandMode {

    public static void main(String[] args) {
        Controller controllerA = new Controller();
        controllerA.setCommand(new CommandA());
        controllerA.run();

        Controller controllerB = new Controller();
        controllerB.setCommand(new CommandB());
        controllerB.run();
    }

}

interface Command{
    void execute();
}

class CommandA implements Command{
    @Override
    public void execute() {
        System.out.println("执行 CommandA ...");
    }
}

class CommandB implements Command{
    @Override
    public void execute() {
        System.out.println("执行 CommandB ...");
    }
}

class Controller{
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void run() {
        this.command.execute();
    }
}