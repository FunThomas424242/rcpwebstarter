package com.gh.funthomas424242s.rcpwebstarter.env;

import javax.swing.JOptionPane;

public class OSUmgebung {

	/*
	 * Von Eclipse RCP unterstützte Architekturen
	 */
	private static final String ARCH_X86 = "x86";
	private static final String ARCH_PA_RISC = "PA_RISC";
	private static final String ARCH_PPC = "ppc";
	private static final String ARCH_SPARC = "sparc";
	private static final String ARCH_AMD64 = "amd64";
	private static final String ARCH_X86_64 = "x86_64";
	private static final String ARCH_IA64 = "ia64";

	/*
	 * Von Eclipse RCP unterstützte grafische Benutzeroberflächen
	 */
	private static final String WS_WIN32 = "win32";
	private static final String WS_MOTIF = "motif";
	private static final String WS_GTK = "gtk";
	private static final String WS_PHOTON = "photon";
	private static final String WS_CARBON = "carbon";

	/*
	 * Von Eclipse RCP unterstützte Betriebssysteme
	 */
	private static final String OS_WIN32 = "win32";
	private static final String OS_LINUX = "linux";
	private static final String OS_AIX = "aix";
	private static final String OS_SOLARIS = "solaris";
	private static final String OS_HPUX = "hpux";
	private static final String OS_QNX = "qnx";
	private static final String OS_MACOSX = "macosx";

	protected String arch;
	protected String os;
	protected String win;
	protected String shortcutDirName;

	private OSUmgebung(final String architektur, final String betriebssystem) {
		this.arch = architektur;
		this.os = betriebssystem;
	}

	public static OSUmgebung erzeugeOSUmgebung() {
		final String architektur = System.getProperty("os.arch").toLowerCase();
		final String betriebssystem = System.getProperty("os.name")
				.toLowerCase();
		final OSUmgebung osUmgebung = new OSUmgebung(architektur,
				betriebssystem);
		osUmgebung.mappeVorliegendeArchitekturAufUnterstuetzteArchitektur();
		osUmgebung.mappeVorliegendesOSAufUnterstuetztesOS();
		osUmgebung.bestimmeWindowsystemAusBetriebssystem();
		osUmgebung.korrigiereBekannteFalschZuordnungen();
		osUmgebung.bestimmeShortcutDirName();
		return osUmgebung;
	}
	
	public String getArchitektur() {
		return arch;
	}

	public String getBetriebssystem() {
		return os;
	}

	public String getFenstersystem() {
		return win;
	}

	public String getShortcutDirName() {
		return shortcutDirName;
	}
	
	private void mappeVorliegendeArchitekturAufUnterstuetzteArchitektur() {

		if (arch.indexOf("x86") >= 0 || arch.matches("i.86")) {
			arch = ARCH_X86;

		} else if (arch.indexOf("ppc") >= 0 || arch.indexOf("power") >= 0) {
			arch = ARCH_PPC;

		} else if (arch.indexOf("x86_64") >= 0 || arch.indexOf("amd64") >= 0) {
			arch = ARCH_AMD64;

		} else if (arch.indexOf("ia64") >= 0) {
			arch = ARCH_IA64;

		} else if (arch.indexOf("risc") >= 0) {
			arch = ARCH_PA_RISC;

		} else if (arch.indexOf("sparc") >= 0) {
			arch = ARCH_SPARC;

		} else {

			beendeMitFehler("Unknown Architecture",
					"Your system has an unknown architecture: " + arch);
		}
	}

	protected void mappeVorliegendesOSAufUnterstuetztesOS() {

		if (os.indexOf("linux") >= 0) {
			os = OS_LINUX;
		} else if (os.indexOf("mac") >= 0) {
			os = OS_MACOSX;
		} else if (os.indexOf("windows") >= 0) {
			os = OS_WIN32;
		} else if (os.indexOf("hp") >= 0 && os.indexOf("ux") >= 0) {
			os = OS_HPUX;
		} else if (os.indexOf("solaris") >= 0) {
			os = OS_SOLARIS;
		} else if (os.indexOf("aix") >= 0) {
			os = OS_AIX;
		} else if (os.indexOf("qnx") >= 0) {
			os = OS_QNX;
		} else {
			beendeMitFehler("Unknown Operating System",
					"Your operating system is unknown: " + os);
		}
	}

	protected void bestimmeWindowsystemAusBetriebssystem() {
		if (os.equals(OS_WIN32)) {
			win = WS_WIN32;
		} else if (os.equals(OS_LINUX)) {
			win = WS_GTK;
		} else if (os.equals(OS_QNX)) {
			win = WS_PHOTON;
		} else if (os.equals(OS_MACOSX)) {
			win = WS_CARBON;
		} else {
			// OS_AIX, OS_HPUX, OS_SOLARIS
			win = WS_MOTIF;
		}
	}
	
	protected void bestimmeShortcutDirName(){

		if (os.equals(OS_LINUX)) {
			shortcutDirName = "Schreibtisch";
		}else{
			shortcutDirName = "Desktop";
		}
	}

	protected void korrigiereBekannteFalschZuordnungen() {
		// Sonderfall Ubuntu
		if (arch.equals(ARCH_AMD64) && os.equals(OS_LINUX)) {
			arch = ARCH_X86_64;
		}
	}

	protected void beendeMitFehler(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

}
