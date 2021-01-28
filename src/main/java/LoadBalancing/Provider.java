package LoadBalancing;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

public class Provider implements IProvider{
    private String uuid = UUID.randomUUID().toString();
    private boolean lastCheck = true;
    private boolean beforeLastCheck = true;
    private ThreadPoolExecutor executor;

    public Provider(int capacity) {
        BlockingQueue<Runnable> requestQueue = new ArrayBlockingQueue<>(capacity);
        executor = new ThreadPoolExecutor(
                4, 10, 20, TimeUnit.SECONDS, requestQueue);
    }
    public boolean getAlive() {
        return true;
    }

    @Override
    public Optional<String> get() {
        try {
            Future<String> result = executor.submit(() -> uuid);
            return Optional.ofNullable(result.get());
        } catch (Exception ex) {
            return Optional.empty();
        }
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
