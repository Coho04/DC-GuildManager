package de.goldendeveloper.guildmanager;

import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.MysqlTypes;
import de.goldendeveloper.mysql.entities.Table;

public class Main {

    private static Discord discord;
    private static Config config;
    private static MYSQL mysql;

    public static String dbName = "GDGuildManager";
    public static String settingsTName = "Settings";
    public static String colmGuild = "Guild";
    public static String colmWChannel = "WelcomeChannel";
    public static String colmJRole = "JoinRole";

    public static void main(String[] args) {
        config = new Config();
        discord = new Discord(config.getDiscordToken());
        createMYSQl(config.getMysqlHostname(), config.getMysqlUsername(), config.getMysqlPassword(), config.getMysqlPort());
    }

    public static void createMYSQl(String hostname, String username, String password, int port) {
        mysql = new MYSQL(hostname, username, password, port);
        if (!mysql.existsDatabase(dbName)) {
            mysql.createDatabase(dbName);
        }
        Database db = mysql.getDatabase(dbName);
        if (!db.existsTable(settingsTName)) {
            db.createTable(settingsTName);
        }
        Table tb = db.getTable(settingsTName);
        if (!tb.existsColumn(colmGuild)) {
            tb.addColumn(colmGuild, MysqlTypes.VARCHAR, 80);
        }
        if (!tb.existsColumn(colmWChannel)) {
            tb.addColumn(colmWChannel, MysqlTypes.VARCHAR, 80);
        }
        if (!tb.existsColumn(colmJRole)) {
            tb.addColumn(colmJRole, MysqlTypes.VARCHAR, 80);
        }
        System.out.println("MYSQL Finished");
    }

    public static Discord getDiscord() {
        return discord;
    }

    public static Config getConfig() {
        return config;
    }

    public static MYSQL getMysql() {
        return mysql;
    }
}