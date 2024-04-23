package cn.sliew.scaleph.kubernetes.watch.event.source;

import io.fabric8.kubernetes.api.model.HasMetadata;

public abstract class AbstractResourceEventSource<R, P extends HasMetadata> extends AbstractEventSource
        implements ResourceEventSource<R, P> {

    private final Class<R> resourceClass;

    public AbstractResourceEventSource(Class<R> resourceClass) {
        this.resourceClass = resourceClass;
    }

    @Override
    public Class<R> getResourceType() {
        return resourceClass;
    }
}
