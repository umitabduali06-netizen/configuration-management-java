import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigurationManager {

    private static ConfigurationManager instance;

    private final Map<String, String> configData = new ConcurrentHashMap<>();
    private final List<ConfigurationSource> sources = new ArrayList<>();
    private boolean initialized = false;

    private ConfigurationManager() {
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public synchronized void initialize() {
        if (initialized) {
            return;
        }

        sources.add(new FileConfigSource());
        sources.add(new EnvConfigSource());

        for (ConfigurationSource source : sources) {
            configData.putAll(source.load());
        }

        initialized = true;
    }

    public String getValue(String key) {
        return configData.get(key);
    }

    public synchronized void setValue(String key, String value) {
        String oldValue = configData.get(key);
        configData.put(key, value);
        ConfigAuditLogger.log(key, oldValue, value);
    }

    public synchronized void persist() {
        for (ConfigurationSource source : sources) {
            source.save(configData);
        }
    }
}
