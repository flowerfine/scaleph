package cn.sliew.scaleph.kubernetes.watch.event;

import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.Getter;

@Getter
public class ResourceID {

    private final String name;
    private final String namespace;

    public ResourceID(String name) {
        this(name, null);
    }

    public ResourceID(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    public static ResourceID fromResource(HasMetadata resource) {
        return new ResourceID(resource.getMetadata().getName(),
                resource.getMetadata().getNamespace());
    }
}
