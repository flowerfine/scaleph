package cn.sliew.scaleph.plugin.framework.property;

import lombok.Getter;

@Getter
public class ValidationResult<T> {

    private final String subject;
    private final T input;
    private final String explanation;
    private final boolean valid;

    protected ValidationResult(final Builder<T> builder) {
        this.subject = builder.subject;
        this.input = builder.input;
        this.explanation = builder.explanation;
        this.valid = builder.valid;
    }

    public static final class Builder<T> {

        private boolean valid = false;
        private T input = null;
        private String explanation = "";
        private String subject = "";

        public Builder valid(final boolean valid) {
            this.valid = valid;
            return this;
        }

        public Builder input(final T input) {
            if (null != input) {
                this.input = input;
            }
            return this;
        }

        public Builder explanation(final String explanation) {
            if (null != explanation) {
                this.explanation = explanation;
            }
            return this;
        }
        
        public Builder subject(final String subject) {
            if (null != subject) {
                this.subject = subject;
            }
            return this;
        }

        public ValidationResult build() {
            return new ValidationResult(this);
        }
    }
}
