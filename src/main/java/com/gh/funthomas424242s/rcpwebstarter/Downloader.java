package com.gh.funthomas424242s.rcpwebstarter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.jnlp.BasicService;
import javax.jnlp.DownloadService;
import javax.swing.ProgressMonitor;

import com.gh.funthomas424242s.rcpwebstarter.env.WebStartUmgebung;

public class Downloader {
	
	final static Logger<Downloader> LOGGER = new Logger<Downloader>(Downloader.class);
	final protected BasicService basicService;
	final protected DownloadService downloadService;
	
	public Downloader(final WebStartUmgebung webstartUmgebung){
		this.basicService=webstartUmgebung.getBasicService();
		this.downloadService=webstartUmgebung.getDownloadService();
	}
	
	
	/*
	 * Download a file from an url and store it at destFile. A progress monitor
	 * will be shown.
	 */
	protected void downloadFile(URL url, File destFile) {
		try {

			destFile.getParentFile().mkdirs();
			LOGGER.printInfoBox("DestFile2 ", destFile.getAbsolutePath());
			OutputStream out = new FileOutputStream(destFile);

			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();

			int totalSize = conn.getContentLength(), downloadedSize = 0, size;
			byte[] buffer = new byte[32768];

			ProgressMonitor pm = LOGGER.createProgressMonitor("Downloading " + url,
					totalSize);
			boolean canceled = false;

			while ((size = in.read(buffer)) > 0
					&& !(canceled = pm.isCanceled())) {
				out.write(buffer, 0, size);
				pm.setProgress(downloadedSize += size);
				// pm.setNote(downloadedSize / totalSize + "% finished");
			}

			in.close();
			out.close();

			if (canceled) {
				destFile.delete(); // Delete uncomplete file
				LOGGER.beendeMitFehler("Starting canceled",
						"Downloading canceled. Exiting...");
			}

			pm.close();
		} catch (IOException ex) {
			final StringBuilder builder = new StringBuilder();
			final StackTraceElement[] arr = ex.getStackTrace();
			for (StackTraceElement s : arr) {
				builder.append(s.toString());
			}
			LOGGER.beendeMitFehler("Download Error", "Couldn't download file: "
					+ builder.toString());
		}
	}
	
	

	
	

}
