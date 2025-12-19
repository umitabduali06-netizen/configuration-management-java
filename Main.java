public class Main {

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
