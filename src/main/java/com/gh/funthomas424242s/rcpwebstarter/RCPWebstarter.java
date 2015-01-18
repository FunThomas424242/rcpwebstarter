/*******************************************************************************
 * Copyright (c) 2013 WeigleWilczek GmbH formerly iMedic GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Contributors:
 *   WeigleWilczek GmbH [http://www.w11k.com] - initial API and implementation
 *******************************************************************************/

package com.gh.funthomas424242s.rcpwebstarter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Policy;
import java.util.Locale;

import com.gh.funthomas424242s.rcpwebstarter.env.OSUmgebung;
import com.gh.funthomas424242s.rcpwebstarter.env.WebStartUmgebung;
import com.gh.funthomas424242s.rcpwebstarter.env.WebStarterProperties;
import com.w11k.webrcp.AllPermissionPolicy;
import com.w11k.webrcp.Shortcut;
import com.w11k.webrcp.UnpackThread;

/**
 * WebRCP - Web Start Application which acts as loader for an Eclipse RCP
 * application.
 * 
 * @author by Daniel Mendler <mendler@imedic.de>
 */
public class RCPWebstarter implements Runnable {

	final static Logger<RCPWebstarter> LOGGER = new Logger<RCPWebstarter>(RCPWebstarter.class);
	
	
	public static void main(String[] args) {
		final Runnable starter = new RCPWebstarter();
		starter.run();
	}

	public RCPWebstarter() {

	}

	public void run() {
		final WebStartUmgebung webstartUmgebung = WebStartUmgebung
				.erzeugeWebStartUmgebung();
		final WebStarterProperties webstarterProperties = WebStarterProperties
				.erzeugeWebstarterProperties();

		// Bestimme und merke BaseURL
		final String baseURL = webstartUmgebung.getBaseURL();
		webstarterProperties.setAsSystemProperty(
				WebStarterProperties.PROPERTY_BASEURL, baseURL);

		// Sichere SingleInstance Mode falls gefordert
		if (webstarterProperties.shouldBeSingleInstance()) {
			final int port = webstarterProperties.getSingleInstancePort();
			ensureSingleInstance(port);
		}

		// Hole erforderliche Properties
		final String appName = webstarterProperties.getAppName();
		final String appVersion = webstarterProperties.getAppVersion();
		final String[] archive = webstarterProperties.getArchives();

		// Get application/product to launch
		String launcherArg = webstarterProperties.getLaunchApp();

		if (launcherArg == null) {
			launcherArg = webstarterProperties.getLaunchProduct();

			if (launcherArg == null) {
				LOGGER.beendeMitFehler("Missing System Property",
						WebStarterProperties.PROPERTY_LAUNCHAPP + " or "
								+ WebStarterProperties.PROPERTY_LAUNCHPRODUCT
								+ " are required");
			} else {
				launcherArg = " -product " + launcherArg;
			}
		} else {
			launcherArg = " -application " + launcherArg;
		}

		final File tempDir = new File(webstarterProperties.getTempDir(),
				appName);
		tempDir.mkdirs();

		File unpackDestDir = new File(tempDir, "unpacked");

		// Check for new version
		boolean override = newVersionAvailable(appVersion, new File(tempDir,
				"version"));

		// Start background thread for unpacking
		UnpackThread unpackThread = new UnpackThread(unpackDestDir, override);

		// Download and unpack system-independant archives
		final Downloader downloader=new Downloader(webstartUmgebung);
		for (String element : archive) {

			URL archiveURL = null;
			try {
				archiveURL = new URL(element);
			} catch (MalformedURLException ex) {
				throw new RuntimeException(ex);
			}

			final String message = archiveURL == null ? "null" : archiveURL
					.toString();
			LOGGER.printDebugInfoBox("URL " + element, message);

			final String fileName = archiveURL.getFile();
			LOGGER.printDebugInfoBox("DestFile1" + element, fileName);
			final File destFile = new File(tempDir, fileName);

			if (!destFile.exists() || override) {
				if (archiveURL != null) {
					downloader.downloadFile(archiveURL, destFile);
				}
			}
			unpackThread.addNextFile(destFile);
		}

		// Show a progress monitor which "simulates" the startup of
		// the application.
		LOGGER.simulateProgress("Loading " + appName + "...", 3);

		// Wait for the unpacking thread to complete
		unpackThread.finish();

		// create dekstop shortcut
		String executable = webstarterProperties.getExcecuteable();

		// try {
		// Then start the launcher!
		// startLauncher(unpackDestDir.toURI().toURL(), os, arch, launcherArg);
		final OSUmgebung osUmgebung = OSUmgebung.erzeugeOSUmgebung();
		final String shortcutDirName = osUmgebung.getShortcutDirName();
		createDesktopShortcutToExe(webstarterProperties,shortcutDirName, unpackDestDir.getPath()
				+ File.separator + executable, appName);
		startLauncher(webstarterProperties, osUmgebung,
				unpackDestDir.getPath(), launcherArg);
		// } catch (MalformedURLException ex) {
		// // This shouldn't happen.
		// throw new RuntimeException(ex);
		// }
	}

	/*
	 * Absicherung das nur eine Instanz läuft
	 */
	protected void ensureSingleInstance(int port) {
		try {
			new ServerSocket(port);
		} catch (Exception ex) {
			LOGGER.beendeMitFehler("Already running.",
					"There's already an instance running.");
		}
	}

	/*
	 * Check for new eclipse application version by comparing the value in the
	 * version file and the version argument. The version value can be an
	 * arbitrary string.
	 */
	protected boolean newVersionAvailable(String newVersion,
			File versionFile) {
		String oldVersion = null;

		try {
			// Read old version
			BufferedReader in = new BufferedReader(new FileReader(versionFile));
			oldVersion = in.readLine();
			in.close();
		} catch (IOException ex) {
			// No error. File doesn't already exists.
		}

		try {
			// Write new version
			Writer out = new FileWriter(versionFile);
			out.write(newVersion);
			out.close();

			return oldVersion == null || !newVersion.equals(oldVersion);
		} catch (IOException ex) {
			// Not too bad. We continue.
			return true;
		}
	}



	/*
	 * Load and start eclipse launcher org.eclipse.core.launcher.Main
	 */
	protected void startLauncher(
			final WebStarterProperties webstarterProperties,
			final OSUmgebung osUmgebung, String url, String arg) {

		final String arch = osUmgebung.getArchitektur();
		final String os = osUmgebung.getBetriebssystem();
		final String win = osUmgebung.getFenstersystem();

		URLClassLoader urlClassLoader = null;
		try {
			// Reload new policy which allows all to all codebases
			// because the default policy doesn't apply to the code loaded from
			// startup.jar!!!
			Policy.setPolicy(new AllPermissionPolicy());

			final String path = "file:" + url + File.separator
					+ webstarterProperties.getLauncherJar();
			System.out.println("launcher jar: " + path);
			final String launcherClassName = webstarterProperties
					.getLauncherClass();
			// TODO
			LOGGER.printDebugInfoBox("Launcher Jar", path);
			LOGGER.printDebugInfoBox("Launcher Class", launcherClassName);

			urlClassLoader = new URLClassLoader(new URL[] { new URL(path) });
			Class<?> launcher = urlClassLoader.loadClass(launcherClassName);
			final Method launcherMain = launcher
					.getMethod("main", String.class);

			/*
			 * Start launcher with aurguments -os <operating-system> -ws
			 * <window-system> -arch <system architecture> -install
			 * <installation directory> -data <workspace directory> -user
			 * <workspace directory> -nl <locale> <-application|product name>
			 * The default workspace directory is put under the installation
			 * directory.
			 */
			final String exeCommand = "-os " + os + " -ws " + win + " -arch "
					+ arch + " -install " + url + "/eclipse/" + " -data " + url
					+ "/workspace/ -user " + url + "/workspace/ -nl "
					+ Locale.getDefault() + arg;

			LOGGER.printDebugInfoBox("Launcher Command", exeCommand == null ? "null"
					: exeCommand);

			launcherMain.invoke(null, exeCommand);

		} catch (InvocationTargetException ex) {
			LOGGER.beendeMitFehler("Startup Error",
					"Invocation failed: " + ex.getCause());
		} catch (IllegalAccessException ex) {
			LOGGER.beendeMitFehler("Startup Error", "Invocation failed: " + ex);
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			LOGGER.beendeMitFehler("Startup Error", "Invalid Eclipse Launcher: " + ex);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			LOGGER.beendeMitFehler("Startup Error", "Eclipse Launcher not found: "
					+ ex);
		} catch (MalformedURLException ex) {
			// This shouldn't happen.
			throw new RuntimeException(ex);
		} finally {
			if (urlClassLoader != null) {
				try {
					urlClassLoader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	
	
	

	/**
	 * Creates a shortcut on the users desktop to the passed target
	 * 
	 * @param shortcutTarget
	 * @param appName
	 */
	private static void createDesktopShortcutToExe(final WebStarterProperties webstarterProperties,
			final String shortcutDirName, final String shortcutTarget,
			final String appName) {
		Shortcut scut;
		try {
			scut = new Shortcut(new File(shortcutTarget));

			final String userHomeDir = webstarterProperties.getHomeDir();

			final String shortCutFileName = userHomeDir + File.separator
					+ shortcutDirName + File.separator + appName + ".lnk";
			
			OutputStream outStream = new FileOutputStream(shortCutFileName);
			outStream.write(scut.getBytes());
			outStream.flush();
			outStream.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOGGER.beendeMitFehler("Error while creating Desktop Shortcut",
					"Wrong Shortcut target specified" + e.getLocalizedMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.beendeMitFehler("Error while creating Desktop Shortcut",
					"File not found" + e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.beendeMitFehler("Error while creating Desktop Shortcut",
					e.getLocalizedMessage());
		}
	}

	
}