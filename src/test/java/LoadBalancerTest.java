import LoadBalancing.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class LoadBalancerTest {
    static class FakeProvider extends Provider {
        private final int id;
        private boolean alive = true;

        public FakeProvider(int id, int capacity) {
            super(capacity);
            this.id = id;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }

        @Override
        public boolean getAlive() {
            return alive;
        }

        @Override
        public Optional<String> get() {
            return Optional.of("Provider" + id);
        }
    }

    @Test
    public void roundRobinStrategyShouldInvokeProvidersInCircularSequence() {
        List<IProvider> providers = Arrays.asList(
                new FakeProvider(1, 2),
                new FakeProvider(2, 3),
                new FakeProvider(3, 4));

        InvocationStrategy roundRobinInvocation = new RoundRobinInvocation(providers.size());

        ILoadBalancer loadBalancer = new LoadBalancer(roundRobinInvocation, 1000);
        for (IProvider provider : providers) {
            assertTrue(loadBalancer.subscribe(provider));
        }

        for (int round=0; round<2; round++) {
            for (int i=0; i<providers.size(); i++) {
                assertEquals(loadBalancer.get().orElse(""), "Provider"+(i+1));
            }
        }
    }

    @Test
    public void randomStrategyShouldInvokeProvidersRandomly() {
        List<IProvider> providers = Arrays.asList(
                new FakeProvider(1, 2),
                new FakeProvider(2, 3),
                new FakeProvider(3, 4));

        InvocationStrategy randomInvocation = new RandomInvocation(providers.size());

        ILoadBalancer loadBalancer = new LoadBalancer(randomInvocation, 1000);
        for (IProvider provider : providers) {
            assertTrue(loadBalancer.subscribe(provider));
        }

        List<String> invocations = IntStream.range(0, 100)
                .mapToObj(i -> loadBalancer.get())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        assertTrue(invocations.contains("Provider1"));
        assertTrue(invocations.contains("Provider2"));
        assertTrue(invocations.contains("Provider3"));
    }

    @Test
    public void differentProvidersShouldProvideDifferentUUID() {
        assertEquals(IntStream.range(0, 1000).mapToObj(i -> new Provider(5).get()).distinct().count(), 1000);
    }

    @Test
    public void loadBalancerShouldAcceptMaximum10Providers() {
        List<Provider> providers = IntStream.range(0, 10).mapToObj(i -> new Provider(5)).collect(Collectors.toList());
        InvocationStrategy randomInvocation = new RandomInvocation(providers.size());

        ILoadBalancer loadBalancer = new LoadBalancer(randomInvocation, 1000);
        for (IProvider provider : providers) {
            assertTrue(loadBalancer.subscribe(provider));
        }

        assertFalse(loadBalancer.subscribe(new Provider(5)));
    }

    @Test
    public void loadBalancerShouldBeAbleToIncludeOrExcludeASpecifiedProvider() {
        List<IProvider> providers = IntStream.range(0, 10)
                .mapToObj(i -> new FakeProvider(i + 1, 5)).collect(Collectors.toList());

        InvocationStrategy roundRobinInvocation = new RoundRobinInvocation(providers.size());

        ILoadBalancer loadBalancer = new LoadBalancer(roundRobinInvocation, 1000);
        for (IProvider provider : providers) {
            assertTrue(loadBalancer.subscribe(provider));
        }

        for (int i=0; i<providers.size(); i++) {
            assertEquals(loadBalancer.get().orElse(""), "Provider"+(i+1));
        }

        loadBalancer.excludeProvider(5);

        assertTrue(IntStream.range(0, providers.size())
                .mapToObj(i -> loadBalancer.get().orElse("")).noneMatch("Provider6"::equals));

        loadBalancer.includeProvider(5);

        assertTrue(IntStream.range(0, providers.size())
                .mapToObj(i -> loadBalancer.get().orElse("")).anyMatch("Provider6"::equals));
    }

    @Test
    public void loadBalancerShouldExcludeUnhealthyProviders() throws InterruptedException {
        List<FakeProvider> providers = Arrays.asList(
                new FakeProvider(1, 5),
                new FakeProvider(2, 5));

        InvocationStrategy roundRobinInvocation = new RoundRobinInvocation(2);

        ILoadBalancer loadBalancer = new LoadBalancer(roundRobinInvocation, 10);
        for (IProvider provider : providers) {
            assertTrue(loadBalancer.subscribe(provider));
        }

        assertEquals(loadBalancer.get().orElse(""), "Provider1");
        assertEquals(loadBalancer.get().orElse(""), "Provider2");

        providers.get(0).setAlive(false);

        Thread.sleep(100);
        assertEquals(loadBalancer.get().orElse(""), "Provider2");
        assertEquals(loadBalancer.get().orElse(""), "Provider2");

        providers.get(0).setAlive(true);
        Thread.sleep(100);
        assertEquals(loadBalancer.get().orElse(""), "Provider1");
        assertEquals(loadBalancer.get().orElse(""), "Provider2");
    }
}
