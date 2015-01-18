package com.gh.funthomas424242s.rcpwebstarter.env;

import java.net.URL;

import javax.jnlp.BasicService;
import javax.jnlp.DownloadService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import com.gh.funthomas424242s.rcpwebstarter.Logger;

public class WebStartUmgebung {

	final static Logger<WebStartUmgebung> LOGGER = new Logger<WebStartUmgebung>(WebStartUmgebung.class);
	
	final protected BasicService basicService;
	final protected DownloadService downloadService;

	protected WebStartUmgebung(final BasicService basicService,
			final DownloadService downloadService) {
		this.basicService = basicService;
		this.downloadService = downloadService;
	}

	public BasicService getBasicService() {
		return basicService;
	}

	public DownloadService getDownloadService() {
		return downloadService;
	}

	public String getBaseURL() {
		return basicService.getCodeBase().toString();
	}

	public static WebStartUmgebung erzeugeWebStartUmgebung() {

		final BasicService basicService = holeBasicService();
		final DownloadService downloadService = holeDownloadService();
		final WebStartUmgebung umgebung = new WebStartUmgebung(basicService,
				downloadService);
		return umgebung;
	}

	protected static BasicService holeBasicService() {
		BasicService service = null;
		try {
			service = (BasicService) ServiceManager
					.lookup("javax.jnlp.BasicService");
			return service;
		} catch (UnavailableServiceException ex) {
			LOGGER.beendeMitFehler("WebStart Service Error",
					"Service javax.jnlp.BasicService unvailable: " + ex);
		}
		return service;
	}

	protected static DownloadService holeDownloadService() {
		DownloadService service = null;
		try {
			service = (DownloadService) ServiceManager
					.lookup("javax.jnlp.DownloadService");
		} catch (UnavailableServiceException ex) {
			LOGGER.beendeMitFehler("WebStart Service Error",
					"Service javax.jnlp.DownloadService unvailable: " + ex);
		}
		return service;
	}


	public URL getResource(final String resourcePath) {
		final ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		final URL resourceURL = classLoader.getResource(resourcePath);
		return resourceURL;
	}
	
}
