package com.gh.funthomas424242s.rcpwebstarter.env;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import com.gh.funthomas424242s.rcpwebstarter.Logger;

public class WebStarterProperties {

	final static Logger<WebStarterProperties> LOGGER = new Logger<WebStarterProperties>(WebStarterProperties.class);
	
	private static final String SYSTEM_PROPERTY_USER_HOME = "user.home";
	private static final String SYSTEM_PROPERTY_JAVA_IO_TMPDIR = "java.io.tmpdir";
	
	/*
	 * Kompatible zum webrcp Projekt
	 */
	public static final String PROPERTY_APPNAME = "jnlp.WebRCP.appName";
	public static final String PROPERTY_APPVERSION = "jnlp.WebRCP.appVersion";
	public static final String PROPERTY_LAUNCHAPP = "jnlp.WebRCP.launchApp";
	public static final String PROPERTY_LAUNCHPRODUCT = "jnlp.WebRCP.launchProduct";

	public static final String PROPERTY_BASEURL = "jnlp.WebRCP.baseURL";
	public static final String PROPERTY_ARCHIVES = "jnlp.WebRCP.archives";
	public static final String PROPERTY_SINGLEINST = "jnlp.WebRCP.singleInstance";
	public static final String PROPERTY_SINGLEINST_PORT="jnlp.WebRCP.singlePort";

	// Eclipse launcher setup
	public static final String LAUNCHER_CLASS = "jnlp.WebRCP.launcherclass";
	public static final String LAUNCHER_JAR = "jnlp.WebRCP.launcherjar";
	public static final String PROPERTY_EXECUTABLE = "jnlp.WebRCP.executable";

	public static WebStarterProperties erzeugeWebstarterProperties() {
		final WebStarterProperties webstarterProperties = new WebStarterProperties();
		webstarterProperties.removeCustomPrefixFromSystemProperties();
		return webstarterProperties;
	}

	protected void removeCustomPrefixFromSystemProperties() {
		// http://stackoverflow.com/questions/19407102/java-7-update-45-broke-my-web-start-swt-application
		Properties properties = System.getProperties();
		// copy properties to avoid ConcurrentModificationException
		Properties copiedProperties = new Properties();
		copiedProperties.putAll(properties);
		Set<Object> keys = copiedProperties.keySet();
		for (Object key : keys) {
			if (key instanceof String) {
				String keyString = (String) key;
				if (keyString.startsWith("jnlp.custom.")) {
					// re set all properties starting with the
					// jnlp.custom-prefix
					// and set them without the prefix
					String property = System.getProperty(keyString);
					String replacedKeyString = keyString.replaceFirst(
							"jnlp.custom.", "");

					System.setProperty(replacedKeyString, property);
				}
			}
		}
	}


	protected void printSystemProperties() {
		final Properties p = System.getProperties();
		final Enumeration<?> keys = p.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) p.get(key);
			//TODO
			System.out.println(key + ": " + value);
		}
	}

	

	protected String getOptionalProperty(final String key,final String vorbelegung){
		return System.getProperty(key,vorbelegung);
	}
	
	protected String getMandatoryProperty(String key) {
		final String value = getOptionalProperty(key,null);
		if (value == null) {
			LOGGER.beendeMitFehler("Missing System Property", key + " is required");
		}
		return value;
	}

	public void setAsSystemProperty(final String key, final String value) {
		// Speichere Properties zur Übergabe an die RCP App
		System.setProperty(PROPERTY_BASEURL, value);
	}

	public String getTempDir(){
		return getOptionalProperty(SYSTEM_PROPERTY_JAVA_IO_TMPDIR,null);
	}
	
	public String getHomeDir(){
		return getOptionalProperty(SYSTEM_PROPERTY_USER_HOME,null);
	}
	
	
	
	public String getAppName() {
		return getMandatoryProperty(PROPERTY_APPNAME);
	}

	public String getAppVersion() {
		return getMandatoryProperty(PROPERTY_APPVERSION);
	}

	public String[] getArchives() {
		return getMandatoryProperty(PROPERTY_ARCHIVES).split("\\s*,\\s*");
	}
	
	public String getLauncherJar(){
		return getMandatoryProperty(LAUNCHER_JAR);
	}
	
	public String getLauncherClass(){
		return getMandatoryProperty(LAUNCHER_CLASS);
	}
	
	public String getLaunchApp(){
		return getOptionalProperty(PROPERTY_LAUNCHAPP,null);
	}
	
	public String getLaunchProduct(){
		return getOptionalProperty(PROPERTY_LAUNCHPRODUCT,null);
	}
	
	public String getExcecuteable(){
		return getOptionalProperty(PROPERTY_EXECUTABLE,"eclipse.exe");
	}
	
	public int getSingleInstancePort(){
		final String portNummer= getOptionalProperty(PROPERTY_SINGLEINST_PORT, "25975");
		final int port= Integer.parseInt(portNummer);
		return port;
	}
	
	public boolean shouldBeSingleInstance(){
		return Boolean.getBoolean(PROPERTY_SINGLEINST);
	}
	
	

}
