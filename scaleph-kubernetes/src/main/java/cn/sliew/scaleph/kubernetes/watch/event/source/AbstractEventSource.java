package cn.sliew.scaleph.kubernetes.watch.event.source;

import cn.sliew.milky.common.lifecycle.AbstractLifeCycle;
import cn.sliew.scaleph.kubernetes.watch.event.EventHandler;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEventSource extends AbstractLifeCycle implements EventSource {

    private EventHandler handler;
}
