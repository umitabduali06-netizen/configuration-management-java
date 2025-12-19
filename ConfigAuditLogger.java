import java.time.LocalDateTime;

public class ConfigAuditLogger {

    public static void log(String key, String oldValue, String newValue) {
        System.out.println(
                "[" + LocalDateTime.now() + "] " +
                "CONFIG CHANGE: " + key +
                " | old=" + oldValue +
                " | new=" + newValue
        );
    }
}
