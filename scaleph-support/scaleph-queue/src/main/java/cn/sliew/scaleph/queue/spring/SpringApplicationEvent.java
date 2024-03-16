package cn.sliew.scaleph.queue.spring;

import cn.sliew.scaleph.queue.Event;
import org.springframework.context.ApplicationEvent;

public class SpringApplicationEvent extends ApplicationEvent implements Event {

    public SpringApplicationEvent(Object source) {
        super(source);
    }
}
