package LoadBalancing;

import java.util.UUID;

public class Provider implements IProvider{
    String uuid = UUID.randomUUID().toString();

    @Override
    public String get() {
        return uuid;
    }

    @Override
    public boolean check() {
        return false;
    }
}
