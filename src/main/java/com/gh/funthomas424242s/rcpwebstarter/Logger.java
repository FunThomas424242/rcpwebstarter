package com.gh.funthomas424242s.rcpwebstarter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;

public class Logger<T> {

	final static boolean DEBUG_MODE=false;
	
	final Class<T> callerClass;

	public Logger(final Class<T> callerClass) {
		this.callerClass = callerClass;
	}

	public ProgressMonitor createProgressMonitor(String message, int max) {
		ProgressMonitor pm = new ProgressMonitor(null, message, "", 0, max);
		pm.setMillisToDecideToPopup(100);
		pm.setMillisToPopup(500);

		return pm;
	}

	/*
	 * Simulate progress by showing a faked progress monitor
	 */
	public void simulateProgress(String text, int seconds) {
		final ProgressMonitor pm = this.createProgressMonitor(text,
				seconds * 1000 / 50);

		Timer timer = new Timer(50, new ActionListener() {
			private int progress = 0;

			public void actionPerformed(ActionEvent event) {
				if (pm.isCanceled()) {
					System.exit(0);
				}
				pm.setProgress(progress += 1);
			}
		});

		timer.start();
	}

	public void printDebugInfoBox(String title, String message) {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void beendeMitFehler(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

}
