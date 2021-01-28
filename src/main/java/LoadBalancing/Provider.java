package LoadBalancing;

import java.util.UUID;

public class Provider implements IProvider{
    private String uuid = UUID.randomUUID().toString();
    private boolean lastCheck = true;
    private boolean beforeLastCheck = true;

    public boolean getAlive() {
        return true;
    }

    @Override
    public String get() {
        return uuid;
    }

    @Override
    public boolean check() {
        beforeLastCheck = lastCheck;
        lastCheck = getAlive();
        return lastCheck;
    }

    @Override
    public boolean isHealthy() {
        return lastCheck && beforeLastCheck;
    }
}
