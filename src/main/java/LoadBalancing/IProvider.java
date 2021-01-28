package LoadBalancing;

import java.util.Optional;

public interface IProvider {
    Optional<String> get();
    boolean check();
    boolean isHealthy();
}
