package LoadBalancer;

import IInvocationStrategy.IInvocationStrategy;

public class LoadBalancer implements ILoadBalancer {
    @Override
    public String get() {
        return null;
    }

    @Override
    public boolean subscribe(IProvider provider) {
        return false;
    }

    @Override
    public void checkProviders() {

    }

    @Override
    public void setInvocationStrategy(IInvocationStrategy strategy) {

    }

    @Override
    public void includeProvider(int providerIndex) {

    }

    @Override
    public void excludeProvider(int providerIndex) {

    }

    @Override
    public boolean checkClusterCapacityLimit() {
        return false;
    }
}
