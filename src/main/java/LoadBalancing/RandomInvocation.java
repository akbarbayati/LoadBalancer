package LoadBalancing;

import java.util.Random;

public class RandomInvocation extends InvocationStrategy {
    Random random = new Random();

    public RandomInvocation(int providersCount) {
        super(providersCount);
    }

    @Override
    public int nextProvider() {
        return random.nextInt(providersCount);
    }
}
