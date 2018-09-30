package org.chase.telegram.cashbot.changelog;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class Version implements Comparable<Version>{
    private int major;
    private int minor;
    private int patch;

    public static boolean Validate(String versionString) {
        return Pattern.matches("^\\d+\\.\\d+(?:\\.\\d+)?$", versionString);
    }

    public Version(String versionString) throws FormatException {
        fromString(versionString);
    }

    public void fromString(String versionString) throws FormatException {
        if (!Validate(versionString))
            throw new FormatException(
                    "The given String is not a valid Version number in Format major.minor.patch or major.minor");
        String[] parts = versionString.split("\\.");
        major = Integer.parseInt(parts[0]);
        minor = Integer.parseInt(parts[1]);
        patch = parts.length == 3 ? Integer.parseInt(parts[2]) : 0;
    }

    @Override
    public String toString() {
        return String.join(".", Integer.toString(major), Integer.toString(minor), Integer.toString(patch));
    }

    @Override
    public int compareTo(final Version o) {
        if (o.getMajor() == major) {
            if (o.getMinor() == minor) {
                if (o.getPatch() == patch) {
                    return 0;
                } else {
                    return Integer.compare(patch, o.getPatch());
                }
            } else {
                return Integer.compare(minor, o.getMinor());
            }
        } else {
            return Integer.compare(major, o.getMajor());
        }
    }
}
