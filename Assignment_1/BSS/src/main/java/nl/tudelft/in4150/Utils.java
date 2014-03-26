package nl.tudelft.in4150;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Utils {
	
	public static Long getDelay(Integer nodeId, Integer receiverId) {
		Properties Configuration = Utils.loadProperties(nodeId);
		return Long.parseLong((String) Configuration.get("d"+receiverId));
	}
	
	public static List<String> getConfiguration(int nodeId, String conf) {
		
		Properties Configuration = Utils.loadProperties(nodeId);
		
		String[] config = ((String)Configuration.get(conf)).split(" ");
		List<String> result = new ArrayList<String>();
		
		for (int i = 0; i < config.length; i++)
			result.add(config[i]);

		return result;
	}
	
	
	private static Properties loadProperties(int nodeId) {
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(System.getProperty("user.dir") + "/config" + nodeId + ".properties"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return properties;

	}
	
}
