package LoadBalancing;

import java.util.*;
import java.util.stream.Collectors;

public class LoadBalancer implements ILoadBalancer {
    private static final int MAX_PROVIDER_COUNT = 10;
    private final List<IProvider> providers = new ArrayList<>();
    private InvocationStrategy invocationStrategy;
    private List<Boolean> includedProviders = new ArrayList<>();

    public LoadBalancer(InvocationStrategy strategy, int healthCheckInterval) {
        this.invocationStrategy = strategy;
        Timer healthCheckTimer = new Timer("Providers Health Check");
        TimerTask healthCheck = new TimerTask() {
            @Override
            public void run() {
                checkProviders();
            }
        };
        healthCheckTimer.scheduleAtFixedRate(healthCheck, healthCheckInterval, healthCheckInterval);
    }
    @Override
    public synchronized Optional<String> get() {
        int tryCount = 1;
        int providerIndex = invocationStrategy.nextProvider();
        while ((!includedProviders.get(providerIndex) || !providers.get(providerIndex).get().isPresent()) &&
                tryCount <= providers.size()) {
            providerIndex = invocationStrategy.nextProvider();
            tryCount++;
        }

        return tryCount > providers.size() ? Optional.empty() : providers.get(providerIndex).get();
    }

    @Override
    public synchronized boolean subscribe(IProvider provider) {
        if (providers.size() < MAX_PROVIDER_COUNT) {
            providers.add(provider);
            includedProviders.add(true);
            invocationStrategy.setProvidersCount(providers.size());
            return true;
        }

        return false;
    }

    @Override
    public synchronized void checkProviders() {
        includedProviders = providers.stream()
                .map(provider -> provider.check() && provider.isHealthy())
                .collect(Collectors.toList());
    }

    @Override
    public void setInvocationStrategy(InvocationStrategy strategy) {
        this.invocationStrategy = strategy;
    }

    @Override
    public void includeProvider(int providerIndex) {
        if (providerIndex >= 0 && providerIndex < providers.size()) {
            includedProviders.set(providerIndex, true);
        }
    }

    @Override
    public void excludeProvider(int providerIndex) {
        if (providerIndex >= 0 && providerIndex < providers.size()) {
            includedProviders.set(providerIndex, false);
        }
    }

    @Override
    public boolean checkClusterCapacityLimit() {
        return false;
    }
}
