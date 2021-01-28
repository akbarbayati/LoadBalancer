package LoadBalancing;

public class RoundRobinInvocation extends InvocationStrategy {
    private int nextProvider;

    public RoundRobinInvocation(int providersCount) {
        super(providersCount);
        this.nextProvider = -1;
    }

    @Override
    public synchronized int nextProvider() {
        nextProvider = (nextProvider + 1) % providersCount;
        return nextProvider;
    }
}
