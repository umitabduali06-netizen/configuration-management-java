import java.util.Map;

public interface ConfigurationSource {
    Map<String, String> load();
    void save(Map<String, String> data);
}
