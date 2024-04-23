package cn.sliew.scaleph.kubernetes.watch.event.health;

public interface InformerHealthIndicator extends EventSourceHealthIndicator {

  boolean hasSynced();

  boolean isWatching();

  boolean isRunning();

  String getTargetNamespace();
}
