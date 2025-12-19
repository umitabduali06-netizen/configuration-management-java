import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


interface ConfigurationSource {
    Map<String, String> load();
    void save(Map<String, String> data);
}


class FileConfigSource implements ConfigurationSource {

    private final String filePath = "config.txt";

    @Override
    public Map<String, String> load() {
        Map<String, String> config = new HashMap<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return config;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    config.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    @Override
    public void save(Map<String, String> data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class EnvConfigSource implements ConfigurationSource {

    @Override
    public Map<String, String> load() {
        return new HashMap<>(System.getenv());
    }

    @Override
    public void save(Map<String, String> data) {
      
    }
}


class ConfigAuditLogger {

    public static void log(String key, String oldValue, String newValue) {
        System.out.println(
                "[" + LocalDateTime.now() + "] " +
                "CONFIG CHANGE: " + key +
                " | old=" + oldValue +
                " | new=" + newValue
        );
    }
}


class ConfigurationManager {

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


class Main {

    public static void main(String[] args) {

        ConfigurationManager config = ConfigurationManager.getInstance();
        config.initialize();

        config.setValue("app.name", "Configuration System");
        config.setValue("app.version", "1.0");

        System.out.println("App name: " + config.getValue("app.name"));
        System.out.println("Version: " + config.getValue("app.version"));

        config.persist();
    }
}
