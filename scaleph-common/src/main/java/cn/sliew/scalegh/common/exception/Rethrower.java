package cn.sliew.scalegh.common.exception;

import java.util.function.Function;
import java.util.function.Supplier;

public class Rethrower {
    private Rethrower() {
        throw new AssertionError("No instance intended");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAs(Throwable t) throws T {
        throw (T) t;
    }

    public static void toIllegalArgument(final Procedure voidCallable) {
        castCheckedToRuntime(voidCallable, IllegalArgumentException::new);
    }

    private static void castCheckedToRuntime(final Procedure voidCallable, final Function<Exception, RuntimeException> exceptionFactory) {
        try {
            voidCallable.call();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw exceptionFactory.apply(e);
        }
    }

    @FunctionalInterface
    public interface Procedure {
        void call() throws Exception;
    }

    public static void checkArgument(boolean expression, Supplier<Object> errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage.get().toString());
        }
    }

    public static <T extends Object> T checkNotNull(T reference, Supplier<Object> errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage.get().toString());
        }
        return reference;
    }
}
