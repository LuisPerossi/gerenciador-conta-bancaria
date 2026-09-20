package config;

public class DatabaseConfig {
    public static final String URL = getRequiredEnvironmentVariable("URL");
    public static final String USER = getRequiredEnvironmentVariable("USER");
    public static final String PASSWORD = getRequiredEnvironmentVariable("PASSWORD");

    private static String getRequiredEnvironmentVariable(String variableName) {
        String value = System.getenv(variableName);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    String.format("A variável de ambiente %s não foi configurada.", variableName)
            );
        }

        return value;
    }
}
