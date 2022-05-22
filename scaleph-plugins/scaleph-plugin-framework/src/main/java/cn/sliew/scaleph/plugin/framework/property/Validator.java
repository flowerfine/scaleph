package cn.sliew.scaleph.plugin.framework.property;

@FunctionalInterface
public interface Validator<T> {

    Validator INVALID = (subject, input) -> new ValidationResult.Builder().subject(subject).explanation(String.format("'%s' is not a supported property or has no Validator associated with it", subject)).input(input).build();
    Validator VALID = (subject, input) -> new ValidationResult.Builder().subject(subject).input(input).valid(true).build();

    ValidationResult validate(String subject, T input);
}
