package nl.tudelft.in4150;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Utils {
	
	private static final Properties Configuration = Utils.loadProperties();
	
	public static int[] getNetworkNodeIds() {
		String[] config = ((String)Configuration.get("nodes")).split(" ");
		int[] result = new int[config.length];
		
		for (int i = 0; i < config.length; i++)
			result[i] = Integer.parseInt(config[i]);

		return result;
	}
	
	private static Properties loadProperties() {
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(System.getProperty("user.dir") + "/config.properties"));

			for(String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return properties;

	}
	
}
