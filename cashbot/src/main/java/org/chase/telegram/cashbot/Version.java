package org.chase.telegram.cashbot;

import java.awt.FontFormatException;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {
	private int major;
	private int minor;
	private int fix;

	public static boolean Validate(String versionString) {
		return Pattern.matches("^\\d+\\.\\d+(?:\\.\\d+)?$", versionString);
	}

	/**
	 * @param major
	 * @param minor
	 * @param fix
	 */
	public Version(int major, int minor, int fix) {
		super();
		this.major = major;
		this.minor = minor;
		this.fix = fix;
	}

	public Version(String versionString) throws FormatException {
		fromString(versionString);
	}

	public void fromString(String versionString) throws FormatException {
		if (!Validate(versionString))
			throw new FormatException(
					"The given String is not a valid Version number in Format major.minor.fix or major.minor");
		String[] parts = versionString.split("\\.");
		major = Integer.parseInt(parts[0]);
		minor = Integer.parseInt(parts[1]);
		fix = parts.length == 3 ? Integer.parseInt(parts[2]) : 0;
	}

	@Override
	public String toString() {
		return String.format("%d.%d.%d", major, minor, fix);
	}

	@Override
	public int compareTo(Version o) {
		int compare = Integer.compare(major, o.major);
		if (compare != 0) {
			return compare;
		} else {
			compare = Integer.compare(minor, o.minor);
			if (compare != 0) {
				return compare;
			} else {
				compare = Integer.compare(fix, o.fix);
				if (compare != 0) {
					return compare;
				} else {
					return 0;
				}
			}
		}

	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Version) {
			Version vers = (Version) obj;
			return major == vers.major && minor == vers.minor && fix == vers.fix;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		int hashMultiplier = 59;
		
		hash = hash * hashMultiplier + major;
		hash = hash * hashMultiplier + minor;
		hash = hash * hashMultiplier + fix;
		
		return hash;
	}
}
