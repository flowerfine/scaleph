package cn.sliew.scaleph.kubernetes.watch.event;

public interface EventHandler {

    void handleEvent(Event event);
}
