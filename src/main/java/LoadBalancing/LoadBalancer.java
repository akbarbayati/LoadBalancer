package LoadBalancing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoadBalancer implements ILoadBalancer {
    private static final int MAX_PROVIDER_COUNT = 10;
    private final List<IProvider> providers = new ArrayList<>();
    private InvocationStrategy invocationStrategy;
    private List<Integer> excludedProviders = new ArrayList<>();

    public LoadBalancer(InvocationStrategy strategy) {
        this.invocationStrategy = strategy;
    }
    @Override
    public Optional<String> get() {
        int tryCount = 1;
        int providerIndex = invocationStrategy.nextProvider();
        while (excludedProviders.contains(providerIndex) && tryCount <= providers.size()) {
            providerIndex = invocationStrategy.nextProvider();
            tryCount++;
        }

        return tryCount > providers.size() ? Optional.empty() : Optional.ofNullable(providers.get(providerIndex).get());
    }

    @Override
    public boolean subscribe(IProvider provider) {
        if (providers.size() < MAX_PROVIDER_COUNT) {
            providers.add(provider);
            invocationStrategy.setProvidersCount(providers.size());
            return true;
        }

        return false;
    }

    @Override
    public void checkProviders() {

    }

    @Override
    public void setInvocationStrategy(InvocationStrategy strategy) {
        this.invocationStrategy = strategy;
    }

    @Override
    public void includeProvider(int providerIndex) {
        int index = excludedProviders.indexOf(providerIndex);
        if (index != -1) {
            excludedProviders.remove(index);
        }
    }

    @Override
    public void excludeProvider(int providerIndex) {
        if (providerIndex >= 0 && providerIndex < providers.size()) {
            excludedProviders.add(providerIndex);
        }
    }

    @Override
    public boolean checkClusterCapacityLimit() {
        return false;
    }
}
