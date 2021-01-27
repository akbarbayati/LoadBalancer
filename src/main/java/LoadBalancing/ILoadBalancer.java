package LoadBalancing;

import java.util.Optional;

public interface ILoadBalancer {
    Optional<String> get();
    boolean subscribe(IProvider provider);
    void checkProviders();
    void setInvocationStrategy(InvocationStrategy strategy);
    void includeProvider(int providerIndex);
    void excludeProvider(int providerIndex);
    boolean checkClusterCapacityLimit();
}
