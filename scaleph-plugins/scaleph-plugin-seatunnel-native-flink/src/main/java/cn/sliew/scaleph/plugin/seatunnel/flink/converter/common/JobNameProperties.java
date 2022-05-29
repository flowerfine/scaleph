package cn.sliew.scaleph.plugin.seatunnel.flink.converter.common;

import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;

public enum JobNameProperties {
    ;

    public static final PropertyDescriptor<String> JOB_NAME =
        new PropertyDescriptor.Builder<String>()
            .name("job.name")
            .description("The pipeline name")
            .defaultValue(descriptor -> "seatunnel")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
