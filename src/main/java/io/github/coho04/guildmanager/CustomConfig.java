package io.github.coho04.guildmanager;

import io.github.coho04.dcbcore.Config;

public class CustomConfig extends Config {

    public int getMysqlPort() {
        return Integer.parseInt(dotenv.get("MYSQL_PORT"));
    }

    public String getMysqlHostname() {
        return dotenv.get("MYSQL_HOSTNAME");
    }

    public String getMysqlPassword() {
        return dotenv.get("MYSQL_PASSWORD");
    }

    public String getMysqlUsername() {
        return dotenv.get("MYSQL_USERNAME");
    }

    public String getMysqlDatabase() {
        return dotenv.get("MYSQL_DATABASE");
    }
}