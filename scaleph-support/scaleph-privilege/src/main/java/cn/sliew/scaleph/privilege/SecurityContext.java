package cn.sliew.scaleph.privilege;

import cn.sliew.scaleph.common.exception.Rethrower;

import java.util.concurrent.Callable;

public enum SecurityContext {
    ;

    public static <T> T call(Callable<T> callable) {
        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new ScalephSecurityManager());
        T result = null;
        try {
            result = callable.call();

        } catch (Exception e) {
            Rethrower.throwAs(e);
        } finally {
            System.setSecurityManager(securityManager);
        }
        return result;
    }
}
