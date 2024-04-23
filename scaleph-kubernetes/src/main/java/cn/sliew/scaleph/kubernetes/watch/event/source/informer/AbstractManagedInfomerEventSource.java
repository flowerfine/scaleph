package cn.sliew.scaleph.kubernetes.watch.event.source.informer;

import cn.sliew.scaleph.kubernetes.watch.event.source.AbstractResourceEventSource;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;

public abstract class AbstractManagedInfomerEventSource<R extends HasMetadata, P extends HasMetadata>
        extends AbstractResourceEventSource<R, P> implements ResourceEventHandler<R> {

    private InformerManager<R> manager;
    protected MixedOperation client;

    public AbstractManagedInfomerEventSource(Class<R> resourceClass, MixedOperation client) {
        super(resourceClass);
        this.client = client;
    }

    @Override
    public void onAdd(R resource) {

    }

    @Override
    public void onUpdate(R olsResource, R newResource) {

    }

    @Override
    public void onDelete(R resource, boolean deletedFinalStateUnknown) {

    }

    @Override
    protected void doStart() {
        this.manager = new InformerManager<>(client, this);
        this.manager.start();
    }

    @Override
    protected void doStop() {
        this.manager.stop();
    }
}
