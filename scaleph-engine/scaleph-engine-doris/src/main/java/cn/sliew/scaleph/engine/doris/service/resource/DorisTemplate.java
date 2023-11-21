package cn.sliew.scaleph.engine.doris.service.resource;

import cn.sliew.scaleph.engine.doris.operator.spec.DorisClusterSpec;
import cn.sliew.scaleph.engine.doris.operator.status.DorisClusterStatus;
import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.resource.Resource;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Group(Constant.SCALEPH_GROUP)
@Version(Constant.SCALEPH_VERSION)
@EqualsAndHashCode
@JsonPropertyOrder({"apiVersion", "kind", "metadata", "spec", "status"})
public class DorisTemplate extends CustomResource<DorisClusterSpec, DorisClusterStatus> implements Resource {
}
