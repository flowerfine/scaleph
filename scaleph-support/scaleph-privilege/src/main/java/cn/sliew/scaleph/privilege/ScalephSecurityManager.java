package cn.sliew.scaleph.privilege;

import java.security.Permission;

public class ScalephSecurityManager extends SecurityManager {

    @Override
    public void checkExit(int status) {
        super.checkExit(status);
        throw new SecurityException();
    }

    @Override
    public void checkPermission(Permission perm) {
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
    }
}
