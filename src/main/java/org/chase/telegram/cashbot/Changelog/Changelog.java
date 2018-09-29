package org.chase.telegram.cashbot.Changelog;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Changelog {

	private Version version;
	private String description;
	private List<String> added = new ArrayList<>();
	private List<String> removed = new ArrayList<>();
	private List<String> changed = new ArrayList<>();
	private List<String> fixed = new ArrayList<>();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("*%s*", version)).append(System.lineSeparator());
		if(description != null) builder.append(description).append(System.lineSeparator());

		if (added.size() > 0) {
			builder.append("[Added]").append(System.lineSeparator());
			for (String s : added)
				builder.append('-').append(s).append(System.lineSeparator());
			builder.append(System.lineSeparator());
		}

		if (removed.size() > 0) {
			builder.append("[Removed]").append(System.lineSeparator());
			for (String s : removed)
				builder.append('-').append(s).append(System.lineSeparator());
			builder.append(System.lineSeparator());
		}

		if (changed.size() > 0) {
			builder.append("[Changed]").append(System.lineSeparator());
			for (String s : changed)
				builder.append('-').append(s).append(System.lineSeparator());
			builder.append(System.lineSeparator());
		}

		if (fixed.size() > 0) {
			builder.append("[Fixed]").append(System.lineSeparator());
			for (String s : fixed)
				builder.append('-').append(s).append(System.lineSeparator());
		}
		return builder.toString();
	}

}
