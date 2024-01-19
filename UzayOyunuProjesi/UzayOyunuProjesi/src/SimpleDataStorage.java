import java.io.*;
import java.util.Properties;

public class SimpleDataStorage {
    private static final String FILE_PATH = "data.properties";

    public static void main(String[] args) {
        // Veriyi kaydetme
        saveData("score", "100");

        // Veriyi okuma
        String score = loadData("score");

        // Sonuçları görüntüleme
        System.out.println("Score: " + score);
    }

    public static void saveData(String key, String value) {
        try (OutputStream output = new FileOutputStream(FILE_PATH)) {
            Properties properties = new Properties();
            properties.setProperty(key, value);
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadData(String key) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(FILE_PATH)) {
            properties.load(input);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}