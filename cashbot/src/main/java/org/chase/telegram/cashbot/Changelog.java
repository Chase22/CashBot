package org.chase.telegram.cashbot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.telegram.telegrambots.logging.BotLogger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Changelog {
	private static Map<Version, Changelog> changelogs;
	private static Version latest = new Version(-1, -1, -1);

	private Version version;
	private List<String> added = new ArrayList<>();
	private List<String> removed = new ArrayList<>();
	private List<String> changed = new ArrayList<>();
	private List<String> fixed = new ArrayList<>();

	public static void initialise(String path) {
		changelogs = new HashMap<>();

		InputStream xml = Changelog.class.getResourceAsStream(path);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);

		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(xml));

			NodeList version = doc.getElementsByTagName("Version");

			for (int i = 0; i < version.getLength(); i++) {
				Changelog log = new Changelog();
				Node current = version.item(i);

				NodeList contents = current.getChildNodes();
				for (int j = 0; j < contents.getLength(); j++) {
					Node currentCont = contents.item(j);
					if (currentCont == null)
						continue;
					switch (currentCont.getNodeName()) {
					case "added":
						log.added.add(currentCont.getTextContent());
						break;

					case "changed":
						log.changed.add(currentCont.getTextContent());
						break;

					case "removed":
						log.removed.add(currentCont.getTextContent());
						break;

					case "fixed":
						log.fixed.add(currentCont.getTextContent());
						break;
					}
				}
				log.version = new Version(current.getAttributes().getNamedItem("Version").getTextContent());
				if (latest.compareTo(log.version) < 1) {
					latest = log.version;
				}
				changelogs.put(log.version, log);
			}

		} catch (ParserConfigurationException | SAXException | IOException | DOMException | FormatException e) {
			BotLogger.error(Changelog.class.getSimpleName(), e);
		}
		return;
	}

	public static Map<Version, Changelog> getInstance() {
		return changelogs;
	}

	public static Changelog getLatest() {
		return changelogs.get(latest);
	}

	public static Version getLatestVersion() {
		return latest;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("*%s*", version)).append(System.lineSeparator());

		if (added.size() > 0) {
			builder.append("[Added]").append(System.lineSeparator());
			for (String s : added)
				builder.append('-').append(s).append(System.lineSeparator());
		}
		builder.append(System.lineSeparator());

		if (removed.size() > 0) {
			builder.append("[Removed]").append(System.lineSeparator());
			for (String s : removed)
				builder.append('-').append(s).append(System.lineSeparator());
		}
		builder.append(System.lineSeparator());

		if (changed.size() > 0) {
			builder.append("[Changed]").append(System.lineSeparator());
			for (String s : changed)
				builder.append('-').append(s).append(System.lineSeparator());
		}
		builder.append(System.lineSeparator());

		if (fixed.size() > 0) {
			builder.append("[Fixed]").append(System.lineSeparator());
			for (String s : fixed)
				builder.append('-').append(s).append(System.lineSeparator());
		}
		return builder.toString();
	}

	/**
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * @return the added
	 */
	public List<String> getAdded() {
		return added;
	}

	/**
	 * @return the removed
	 */
	public List<String> getRemoved() {
		return removed;
	}

	/**
	 * @return the changed
	 */
	public List<String> getChanged() {
		return changed;
	}

	/**
	 * @return the fixed
	 */
	public List<String> getFixed() {
		return fixed;
	}

}
