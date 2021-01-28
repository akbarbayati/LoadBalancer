package LoadBalancing;

public interface IProvider {
    String get();
    boolean check();
    boolean isHealthy();
}
