/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.plugin.framework.property;

import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import cn.sliew.milky.common.util.StringUtils;

public enum Validators {
    ;

    public static final Validator BOOLEAN_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            final boolean valid = "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
            final String explanation = valid ? null : "Value must be 'true' or 'false'";
            return new ValidationResult.Builder().subject(subject).input(value).valid(valid)
                .explanation(explanation).build();
        }
    };

    public static final Validator INTEGER_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                Integer.parseInt(value);
            } catch (final NumberFormatException e) {
                reason = "not a valid integer";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                .valid(reason == null).build();
        }
    };

    public static final Validator POSITIVE_INTEGER_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                final int intVal = Integer.parseInt(value);

                if (intVal <= 0) {
                    reason = "not a positive value";
                }
            } catch (final NumberFormatException e) {
                reason = "not a valid integer";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                .valid(reason == null).build();
        }
    };

    public static final Validator NON_NEGATIVE_INTEGER_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                final int intVal = Integer.parseInt(value);

                if (intVal < 0) {
                    reason = "value is negative";
                }
            } catch (final NumberFormatException e) {
                reason = "value is not a valid integer";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                .valid(reason == null).build();
        }
    };

    public static final Validator LONG_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                Long.parseLong(value);
            } catch (final NumberFormatException e) {
                reason = "not a valid Long";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                .valid(reason == null).build();
        }
    };

    public static final Validator POSITIVE_LONG_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                final long longVal = Long.parseLong(value);

                if (longVal <= 0) {
                    reason = "not a positive value";
                }
            } catch (final NumberFormatException e) {
                reason = "not a valid 64-bit integer";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                .valid(reason == null).build();
        }
    };

    public static final Validator DOUBLE_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                Double.parseDouble(value);
            } catch (NumberFormatException e) {
                reason = "not a valid Double";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                    .valid(reason == null).build();
        }
    };

    public static final Validator NUMBER_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                NumberFormat.getInstance().parse(value);
            } catch (ParseException e) {
                reason = "not a valid Number";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                .valid(reason == null).build();
        }
    };

    public static final Validator NON_EMPTY_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            return new ValidationResult.Builder().subject(subject).input(value)
                .valid(value != null && !value.isEmpty()).explanation(subject + " cannot be empty")
                .build();
        }
    };
    public static final Validator NON_BLANK_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            return new ValidationResult.Builder().subject(subject).input(value)
                .valid(value != null && !value.trim().isEmpty())
                .explanation(subject
                    + " must contain at least one character that is not white space").build();
        }
    };
    public static final Validator PORT_VALIDATOR = createLongValidator(1, 65535, true);
    /**
     * {@link Validator} that ensures that value is a non-empty comma separated list of hostname:port
     */
    public static final Validator HOSTNAME_PORT_LIST_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(String subject, String input) {
            // not empty
            ValidationResult nonEmptyValidatorResult = NON_EMPTY_VALIDATOR.validate(subject, input);
            if (!nonEmptyValidatorResult.isValid()) {
                return nonEmptyValidatorResult;
            }
            // check format
            final List<String> hostnamePortList = Arrays.asList(input.split(","));
            for (String hostnamePort : hostnamePortList) {
                String[] addresses = hostnamePort.split(":");
                // Protect against invalid input like http://127.0.0.1:9300 (URL scheme should not be there)
                if (addresses.length != 2) {
                    return new ValidationResult.Builder().subject(subject).input(input).explanation(
                            "Must be in hostname:port form (no scheme such as http://").valid(false)
                        .build();
                }

                // Validate the port
                String port = addresses[1].trim();
                ValidationResult portValidatorResult = PORT_VALIDATOR.validate(subject, port);
                if (!portValidatorResult.isValid()) {
                    return portValidatorResult;
                }
            }
            return new ValidationResult.Builder().subject(subject).input(input)
                .explanation("Valid cluster definition").valid(true).build();
        }
    };
    public static final Validator CHARACTER_SET_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                if (!Charset.isSupported(value)) {
                    reason = "Character Set is not supported by this JVM.";
                }
            } catch (final UnsupportedCharsetException uce) {
                reason = "Character Set is not supported by this JVM.";
            } catch (final IllegalArgumentException iae) {
                reason = "Character Set value cannot be null.";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                .valid(reason == null).build();
        }
    };
    public static final Validator URL_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String input) {
            try {
                new URL(input);
                return new ValidationResult.Builder().subject(subject).input(input)
                    .explanation("Valid URL").valid(true).build();
            } catch (final Exception e) {
                return new ValidationResult.Builder().subject(subject).input(input)
                    .explanation("Not a valid URL").valid(false).build();
            }
        }
    };
    public static final Validator URI_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(final String subject, final String input) {
            try {
                new URI(input);
                return new ValidationResult.Builder().subject(subject).input(input)
                    .explanation("Valid URI").valid(true).build();
            } catch (final Exception e) {
                return new ValidationResult.Builder().subject(subject).input(input)
                    .explanation("Not a valid URI").valid(false).build();
            }
        }
    };
    public static final Validator URI_LIST_VALIDATOR = new Validator() {
        @Override
        public ValidationResult validate(String subject, String input) {
            if (input == null || input.isEmpty()) {
                return new ValidationResult.Builder().subject(subject).input(input)
                    .explanation("Not a valid URI, value is missing or empty").valid(false).build();
            }

            Optional<ValidationResult> invalidUri = Arrays.stream(input.split(","))
                .filter(uri -> uri != null && !uri.trim().isEmpty())
                .map(String::trim)
                .map((uri) -> URI_VALIDATOR.validate(subject, uri)).filter((uri) -> !uri.isValid())
                .findFirst();

            return invalidUri.orElseGet(
                () -> new ValidationResult.Builder().subject(subject).input(input)
                    .explanation("Valid URI(s)").valid(true).build());
        }
    };
    public static final Validator FILE_EXISTS_VALIDATOR = new FileExistsValidator(true);
    public static final Validator REGULAR_EXPRESSION_VALIDATOR =
        createRegexValidator(0, Integer.MAX_VALUE);
    public static final Validator REGULAR_EXPRESSION_WITH_EL_VALIDATOR =
        createRegexValidator(0, Integer.MAX_VALUE);

    public static Validator createLongValidator(final long minimum, final long maximum,
                                                final boolean inclusive) {
        return new Validator() {
            @Override
            public ValidationResult validate(final String subject, final String input) {
                String reason = null;
                try {
                    final long longVal = Long.parseLong(input);
                    if (longVal < minimum ||
                        (!inclusive && longVal == minimum) | longVal > maximum ||
                        (!inclusive && longVal == maximum)) {
                        reason = "Value must be between " + minimum + " and " + maximum + " (" +
                            (inclusive ? "inclusive" : "exclusive") + ")";
                    }
                } catch (final NumberFormatException e) {
                    reason = "not a valid integer";
                }

                return new ValidationResult.Builder().subject(subject).input(input)
                    .explanation(reason).valid(reason == null).build();
            }

        };
    }

    public static Validator createDirectoryExistsValidator(final boolean createDirectoryIfMissing) {
        return new DirectoryExistsValidator(createDirectoryIfMissing);
    }

    public static Validator createListValidator(boolean trimEntries, boolean excludeEmptyEntries,
                                                Validator elementValidator) {
        return createListValidator(trimEntries, excludeEmptyEntries, elementValidator, false);
    }

    public static Validator createListValidator(boolean trimEntries, boolean excludeEmptyEntries,
                                                Validator validator,
                                                boolean ensureElementValidation) {
        return (subject, input) -> {
            try {
                if (input == null) {
                    return new ValidationResult.Builder().subject(subject).input(null)
                        .explanation("List must have at least one non-empty element").valid(false)
                        .build();
                }

                final String[] list =
                    ensureElementValidation ? input.split(",", -1) : input.split(",");
                if (list.length == 0) {
                    return new ValidationResult.Builder().subject(subject).input(null)
                        .explanation("List must have at least one non-empty element").valid(false)
                        .build();
                }

                for (String item : list) {
                    String itemToValidate = trimEntries ? item.trim() : item;
                    if (!StringUtils.isBlank(itemToValidate) || !excludeEmptyEntries) {
                        ValidationResult result = validator.validate(subject, itemToValidate);
                        if (!result.isValid()) {
                            return result;
                        }
                    }
                }
                return new ValidationResult.Builder().subject(subject).input(input)
                    .explanation("Valid List").valid(true).build();
            } catch (final Exception e) {
                return new ValidationResult.Builder().subject(subject).input(input)
                    .explanation("Not a valid list").valid(false).build();
            }
        };
    }

    public static Validator createRegexMatchingValidator(final Pattern pattern) {
        return createRegexMatchingValidator(pattern,
            "Value does not match regular expression: " + pattern.pattern());
    }

    public static Validator createRegexMatchingValidator(final Pattern pattern,
                                                         final String validationMessage) {
        return new Validator() {
            @Override
            public ValidationResult validate(final String subject, final String input) {
                String value = input;
                final boolean matches = value != null && pattern.matcher(value).matches();
                return new ValidationResult.Builder()
                    .input(input)
                    .subject(subject)
                    .valid(matches)
                    .explanation(matches ? null : validationMessage)
                    .build();
            }
        };
    }

    public static Validator createRegexValidator(final int minCapturingGroups,
                                                 final int maxCapturingGroups) {
        return new Validator() {
            @Override
            public ValidationResult validate(final String subject, final String value) {
                try {
                    final Pattern pattern = Pattern.compile(value);
                    final int numGroups = pattern.matcher("").groupCount();
                    if (numGroups < minCapturingGroups || numGroups > maxCapturingGroups) {
                        return new ValidationResult.Builder()
                            .subject(subject)
                            .input(value)
                            .valid(false)
                            .explanation("RegEx is required to have between " + minCapturingGroups +
                                " and " + maxCapturingGroups + " Capturing Groups but has " +
                                numGroups)
                            .build();
                    }

                    return new ValidationResult.Builder().subject(subject).input(value).valid(true)
                        .build();
                } catch (final Exception e) {
                    return new ValidationResult.Builder()
                        .subject(subject)
                        .input(value)
                        .valid(false)
                        .explanation("Not a valid Java Regular Expression")
                        .build();
                }

            }
        };
    }

    public static class StringLengthValidator implements Validator {

        private final int minimum;
        private final int maximum;

        public StringLengthValidator(int minimum, int maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }

        @Override
        public ValidationResult validate(final String subject, final String value) {
            if (value.length() < minimum || value.length() > maximum) {
                return new ValidationResult.Builder()
                    .subject(subject)
                    .valid(false)
                    .input(value)
                    .explanation(
                        String.format("String length invalid [min: %d, max: %d]", minimum, maximum))
                    .build();
            } else {
                return new ValidationResult.Builder()
                    .valid(true)
                    .input(value)
                    .subject(subject)
                    .build();
            }
        }
    }

    public static class FileExistsValidator implements Validator {

        private final boolean allowFileOnly;

        public FileExistsValidator(final boolean fileOnly) {
            this.allowFileOnly = fileOnly;
        }

        @Override
        public ValidationResult validate(final String subject, final String value) {
            final Path path = Paths.get(value);
            if (!Files.exists(path)) {
                return new ValidationResult.Builder().subject(subject).input(value).valid(false)
                    .explanation("File " + value + " does not exist").build();
            }
            if (allowFileOnly && Files.isDirectory(path)) {
                return new ValidationResult.Builder().subject(subject).input(value).valid(false)
                    .explanation(value + " is not a file").build();
            }
            return new ValidationResult.Builder().subject(subject).input(value).valid(true).build();
        }
    }

    public static class DirectoryExistsValidator implements Validator {

        private final boolean create;

        public DirectoryExistsValidator(final boolean create) {
            this.create = create;
        }

        @Override
        public ValidationResult validate(final String subject, final String value) {
            String reason = null;
            try {
                final Path path = Paths.get(value);
                if (Files.notExists(path)) {
                    if (!create) {
                        reason = "Directory does not exist";
                    } else if (Files.notExists(Files.createDirectories(path))) {
                        reason = "Directory does not exist and could not be created";
                    }
                } else if (!Files.isDirectory(path)) {
                    reason = "Path does not point to a directory";
                }
            } catch (final Exception e) {
                reason = "Value is not a valid directory name";
            }

            return new ValidationResult.Builder().subject(subject).input(value).explanation(reason)
                .valid(reason == null).build();
        }
    }
}
