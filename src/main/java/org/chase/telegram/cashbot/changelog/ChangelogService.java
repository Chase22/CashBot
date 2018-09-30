package org.chase.telegram.cashbot.changelog;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ChangelogService {

    @Getter
    private Map<Version, Changelog> changelogs;

    ChangelogService(@Value("${changelog.path}")final String changelogPath) {
        changelogs = new HashMap<>();

        InputStream xml = Changelog.class.getResourceAsStream(changelogPath);
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
                        case "description":
                            log.setDescription(currentCont.getTextContent());
                            break;
                        case "added":
                            log.getAdded().add(currentCont.getTextContent());
                            break;

                        case "changed":
                            log.getChanged().add(currentCont.getTextContent());
                            break;

                        case "removed":
                            log.getRemoved().add(currentCont.getTextContent());
                            break;

                        case "fixed":
                            log.getFixed().add(currentCont.getTextContent());
                            break;
                    }
                }
                log.setVersion(new Version(current.getAttributes().getNamedItem("Version").getTextContent()));

                changelogs.put(log.getVersion(), log);
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException | FormatException e) {
            log.error("Error initializing changelog", e);
        }
    }

    public Changelog getLatest() {
        return changelogs.get(getLatestVersion());
    }

    public Version getLatestVersion() {
        return changelogs.keySet().stream().max(Version::compareTo).orElseThrow(NullPointerException::new);
    }

    public Changelog get(Version version) {
        return changelogs.get(version);
    }
}
