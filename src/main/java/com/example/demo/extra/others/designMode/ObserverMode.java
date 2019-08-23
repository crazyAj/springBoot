package com.example.demo.extra.others.designMode;

import lombok.Data;

import java.util.*;

/**
 * 观察者模式
 * 从JDK提供的支持库里，我们能够找到四个对象：
 * Observable、Observer、EventObject、EventListener
 */
public class ObserverMode {

    public static void main(String[] args) {
        // Observable、Observer
        Event event = new Event();
        Source source = new Source();
        source.addObserver(event);
        source.start();

        System.out.println("------- 分割线 -------");

        // EventObject、EventListener
        EventFactory eventFactory = new EventFactory();
        eventFactory.addStatusListener(evt -> System.out.println(evt.getStatus()));
        eventFactory.onClick();
        eventFactory.onMove();
    }

}

/**************************** Observer Start *****************************/

/**
 * 事件源
 */
class Source extends Observable {
    public void start() {
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                setChanged();
                notifyObservers(4 - i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/**
 * 监听事件源，触发事件
 */
class Event implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("倒计时：" + arg);
    }
}
/************************* Observer End *********************************/
/*                              分                                      */
/*                              隔                                      */
/*                              线                                      */
/************************* EventListener Start **************************/
class EventFactory {
    private List<StatusListener> statusListeners = new ArrayList<>();

    public void addStatusListener(StatusListener statusListener) {
        statusListeners.add(statusListener);
    }

    public void notifyListener(ChangeEvent event) {
        statusListeners.forEach(statusListener -> {
            if (statusListeners != null)
                statusListener.changeStatus(event);
        });
    }

    public void onClick() {
        ChangeEvent event = new ChangeEvent(this);
        event.setStatus("click");
        notifyListener(event);
    }

    public void onMove() {
        ChangeEvent event = new ChangeEvent(this);
        event.setStatus("move");
        notifyListener(event);
    }
}

@Data
class ChangeEvent extends EventObject {
    private String status;

    public ChangeEvent(Object event) {
        super(event);
    }
}

interface StatusListener extends EventListener {
    void changeStatus(ChangeEvent event);
}

/************************* EventListener End ***************************/