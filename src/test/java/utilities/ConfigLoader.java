package utilities;

import java.io.FileReader;
import java.util.Properties;

public class ConfigLoader {

	private static Properties properties;

	public static Properties loadProperties() {

		if (properties == null) {

			properties = new Properties();

			String path = System.getProperty("user.dir") + "/src/test/resources/Config.properties";

			try (FileReader file = new FileReader(path)) {

				properties.load(file);

			} catch (Exception e) {

				throw new RuntimeException("Failed to load Config.properties", e);
			}
		}

		return properties;
	}
}