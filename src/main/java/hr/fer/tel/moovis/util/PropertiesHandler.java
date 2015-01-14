package hr.fer.tel.moovis.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {
	
	private static final String PROP_FILE = "config.properties";
	public static final String HOST = "host";
	
	private static PropertiesHandler instance;
	
	static {
		instance = new PropertiesHandler();
	}
	
	private Properties properties;
	private InputStream input;
	
	private PropertiesHandler() {
		properties = new Properties();
		readPropValue();
	}
	
	public static PropertiesHandler getInstance() {
		return instance;
	}
	
	public String getPropValue(String propName) {
		return properties.getProperty(propName);
	}

	private void readPropValue() {
				
		try {
			input = new FileInputStream(PROP_FILE);
			properties.load(input);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
	}
	
	public static void main(String[] argv) {
		
		PropertiesHandler handler = PropertiesHandler.getInstance();
		System.out.println(handler.getPropValue(PropertiesHandler.HOST));
	}
}
