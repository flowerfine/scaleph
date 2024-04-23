package cn.sliew.scaleph.kubernetes.watch.event.source;

import io.fabric8.kubernetes.api.model.HasMetadata;

public interface ResourceEventSource<R, P extends HasMetadata> extends EventSource {

    Class<R> getResourceType();
}
