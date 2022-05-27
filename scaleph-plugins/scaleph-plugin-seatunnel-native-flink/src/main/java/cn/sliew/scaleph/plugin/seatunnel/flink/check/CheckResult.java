package cn.sliew.scaleph.plugin.seatunnel.flink.check;

import org.apache.flink.configuration.ConfigOption;

public class CheckResult {

    private ConfigOption configOption;
    private boolean valid;
    private String explanation;

    protected CheckResult(final Builder builder) {
        this.configOption = builder.configOption;
        this.valid = builder.valid;
        this.explanation = builder.explanation;
    }

    public static final class Builder {
        private ConfigOption configOption;
        private boolean valid;
        private String explanation;

        public Builder configOption(final ConfigOption configOption) {
            if (configOption != null) {
                this.configOption = configOption;
            }
            return this;
        }

        public Builder valid(final boolean valid) {
            this.valid = valid;
            return this;
        }

        public Builder explanation(final String explanation) {
            if (explanation != null) {
                this.explanation = explanation;
            }
            return this;
        }

        public CheckResult build() {
            return new CheckResult(this);
        }
    }
}
