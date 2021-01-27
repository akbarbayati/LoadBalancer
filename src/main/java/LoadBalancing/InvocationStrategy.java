package LoadBalancing;

public interface IInvocationStrategy {
    void setProvidersCount(int count);
    int nextProvider();
    void includeProvider(int providerIndex);
    void excludeProvider(int providerIndex);
}
