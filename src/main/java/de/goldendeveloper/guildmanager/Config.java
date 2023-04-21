package de.goldendeveloper.guildmanager;

import io.github.cdimascio.dotenv.Dotenv;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {

    private final String discordToken;
    private final String discordServer;
    private final String discordWebhook;
    private final String mysqlHostname;
    private final String mysqlUsername;
    private final String mysqlPassword;
    private final int mysqlPort;

    private final int serverPort;
    private final String serverHostname;
    private final String sentryDNS;

    public Config() {
        Dotenv dotenv = Dotenv.load();
        discordToken = dotenv.get("DISCORD_TOKEN");
        discordWebhook = dotenv.get("DISCORD_WEBHOOK");
        discordServer = dotenv.get("DISCORD_SERVER");
        mysqlHostname = dotenv.get("MYSQL_HOSTNAME");
        mysqlPort  = Integer.parseInt(dotenv.get("MYSQL_PORT"));
        mysqlUsername = dotenv.get("MYSQL_USERNAME");
        mysqlPassword = dotenv.get("MYSQL_PASSWORD");
        serverHostname = dotenv.get("SERVER_HOSTNAME");
        serverPort = Integer.parseInt(dotenv.get("SERVER_PORT"));
        sentryDNS = dotenv.get("SENTRY_DNS");
    }

    public String getProjektVersion() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("version");
    }

    public String getProjektName() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("name");
    }

    public String getSentryDNS() {
        return sentryDNS;
    }

    public String getDiscordWebhook() {
        return discordWebhook;
    }

    public int getMysqlPort() {
        return mysqlPort;
    }

    public String getDiscordServer() {
        return discordServer;
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public String getMysqlHostname() {
        return mysqlHostname;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public String getMysqlUsername() {
        return mysqlUsername;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerHostname() {
        return serverHostname;
    }
}