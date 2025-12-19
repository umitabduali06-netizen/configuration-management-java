import java.util.HashMap;
import java.util.Map;

public class EnvConfigSource implements ConfigurationSource {

    @Override
    public Map<String, String> load() {
        return new HashMap<>(System.getenv());
    }

    @Override
    public void save(Map<String, String> data) {
        // Environment variables сақталмайды
    }
}
