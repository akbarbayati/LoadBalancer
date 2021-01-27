package LoadBalancing;

import java.util.Random;

public class RandomIvocation implements IInvocationStrategy{
    private int providersCount;
    Random random = new Random();

    public RandomIvocation(int providersCount) {
        this.providersCount = providersCount;
    }

    @Override
    public void setProvidersCount(int count) {
        this.providersCount = count;
    }

    @Override
    public int nextProvider() {
        return random.nextInt(providersCount);
    }

    @Override
    public void includeProvider(int providerIndex) {

    }

    @Override
    public void excludeProvider(int providerIndex) {

    }
}
