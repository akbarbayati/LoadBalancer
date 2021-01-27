package RoundRobinInvocation;

import IInvocationStrategy.IInvocationStrategy;

public class RoundRobinInvocation implements IInvocationStrategy {
    private int providersCount;
    private int nextProvider;

    public RoundRobinInvocation(int providersCount) {
        this.providersCount = providersCount;
        this.nextProvider = -1;
    }

    @Override
    public void setProvidersCount(int count) {
        this.providersCount = count;
    }

    @Override
    public int nextProvider() {
        nextProvider = (nextProvider + 1) % providersCount;
        return nextProvider;
    }

    @Override
    public void includeProvider(int providerIndex) {

    }

    @Override
    public void excludeProvider(int providerIndex) {

    }
}
