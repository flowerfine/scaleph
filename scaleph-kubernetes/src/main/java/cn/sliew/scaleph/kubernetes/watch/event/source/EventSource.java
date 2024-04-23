package cn.sliew.scaleph.kubernetes.watch.event.source;

import cn.sliew.milky.common.lifecycle.LifeCycle;
import cn.sliew.scaleph.kubernetes.watch.event.health.EventSourceHealthIndicator;

public interface EventSource extends LifeCycle, EventSourceHealthIndicator {

}
