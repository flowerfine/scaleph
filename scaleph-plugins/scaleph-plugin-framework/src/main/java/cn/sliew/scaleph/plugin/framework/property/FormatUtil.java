package cn.sliew.scaleph.plugin.framework.property;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.join;

public enum FormatUtil {

    ;
    private static final String UNION = "|";

    // for Data Sizes
    private static final double BYTES_IN_KILOBYTE = 1024;
    private static final double BYTES_IN_MEGABYTE = BYTES_IN_KILOBYTE * 1024;
    private static final double BYTES_IN_GIGABYTE = BYTES_IN_MEGABYTE * 1024;
    private static final double BYTES_IN_TERABYTE = BYTES_IN_GIGABYTE * 1024;

    // for Time Durations
    private static final String NANOS = join(UNION, "ns", "nano", "nanos", "nanosecond", "nanoseconds");
    private static final String MILLIS = join(UNION, "ms", "milli", "millis", "millisecond", "milliseconds");
    private static final String SECS = join(UNION, "s", "sec", "secs", "second", "seconds");
    private static final String MINS = join(UNION, "m", "min", "mins", "minute", "minutes");
    private static final String HOURS = join(UNION, "h", "hr", "hrs", "hour", "hours");
    private static final String DAYS = join(UNION, "d", "day", "days");
    private static final String WEEKS = join(UNION, "w", "wk", "wks", "week", "weeks");

    private static final String VALID_TIME_UNITS = join(UNION, NANOS, MILLIS, SECS, MINS, HOURS, DAYS, WEEKS);
    public static final String TIME_DURATION_REGEX = "([\\d.]+)\\s*(" + VALID_TIME_UNITS + ")";
    public static final Pattern TIME_DURATION_PATTERN = Pattern.compile(TIME_DURATION_REGEX);
    private static final List<Long> TIME_UNIT_MULTIPLIERS = Arrays.asList(1000L, 1000L, 1000L, 60L, 60L, 24L);
}
