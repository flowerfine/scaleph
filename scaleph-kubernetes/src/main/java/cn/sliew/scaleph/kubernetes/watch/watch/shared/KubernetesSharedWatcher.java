package cn.sliew.scaleph.kubernetes.watch.watch.shared;

import cn.sliew.scaleph.kubernetes.watch.watch.WatchCallbackHandler;

import java.util.concurrent.Executor;

/**
 * The interface for the Kubernetes shared watcher.
 */
public interface KubernetesSharedWatcher<T> extends AutoCloseable {

    Watch watch(String name, WatchCallbackHandler<T> handler, Executor executor);

    @Override
    void close();

    interface Watch extends AutoCloseable {
        @Override
        void close();
    }
}
