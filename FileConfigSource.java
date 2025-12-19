import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileConfigSource implements ConfigurationSource {

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

