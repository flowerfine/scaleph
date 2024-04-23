package cn.sliew.scaleph.kubernetes.watch.event.health;

public interface EventSourceHealthIndicator {

  Status getStatus();
}
