package LoadBalancing;

public abstract class InvocationStrategy {
    protected int providersCount;

    public InvocationStrategy(int providersCount) {
        this.providersCount = providersCount;
    }

    public void setProvidersCount(int count) {
        this.providersCount = count;
    }

    public abstract int nextProvider();
}
