package org.chase.telegram.cashbot;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class ConfigurableFormatter extends SimpleFormatter {

	private final Date dat = new Date();
	 
	 private String format;

	/**
	 * @param format
	 */
	public ConfigurableFormatter(String format) {
		super();
		this.format = format;
	}

	@Override
	public synchronized String format(LogRecord record) {
		dat.setTime(record.getMillis());
		String source;
		if (record.getSourceClassName() != null) {
			source = record.getSourceClassName();
			if (record.getSourceMethodName() != null) {
				source += " " + record.getSourceMethodName();
			}
		} else {
			source = record.getLoggerName();
		}
		String message = formatMessage(record);
		String throwable = "";
		if (record.getThrown() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println();
			record.getThrown().printStackTrace(pw);
			pw.close();
			throwable = sw.toString();
		}
		return String.format(format, dat, source, record.getLoggerName(), record.getLevel().getLocalizedName(),
				message, throwable);
	}
	
	 /**
		 * @return the format
		 */
		public String getFormat() {
			return format;
		}

		/**
		 * @param format the format to set
		 */
		public void setFormat(String format) {
			this.format = format;
		}
	
	
}
