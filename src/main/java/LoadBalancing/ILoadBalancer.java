package ILoadBalancer;

import IInvocationStrategy.IInvocationStrategy;

public interface ILoadBalancer {
    String get();
    boolean subscribe(IProvider provider);
    void checkProviders();
    void setInvocationStrategy(IInvocationStrategy strategy);
    void includeProvider(int providerIndex);
    void excludeProvider(int providerIndex);
    boolean checkClusterCapacityLimit();
}
