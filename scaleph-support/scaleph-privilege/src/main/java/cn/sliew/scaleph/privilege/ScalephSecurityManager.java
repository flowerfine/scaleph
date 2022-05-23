package cn.sliew.scaleph.privilege;

public class ScalephSecurityManager extends SecurityManager {

    @Override
    public void checkExit(int status) {
        super.checkExit(status);
        throw new SecurityException();
    }
}
