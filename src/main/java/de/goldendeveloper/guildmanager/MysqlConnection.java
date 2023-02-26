package de.goldendeveloper.guildmanager;

import de.goldendeveloper.guildmanager.errors.ExceptionHandler;
import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.Table;

public class MysqlConnection {

    private final MYSQL mysql;
    public static String dbName = "GDGuildManager";
    public static String settingsTName = "Settings";
    public static String colmGuild = "Guild";
    public static String colmWChannel = "WelcomeChannel";
    public static String colmJRole = "JoinRole";

    public MysqlConnection(String hostname, String username, String password, int port) {
        mysql = new MYSQL(hostname, username, password, port, new ExceptionHandler());
        if (!mysql.existsDatabase(dbName)) {
            mysql.createDatabase(dbName);
        }
        Database db = mysql.getDatabase(dbName);
        if (!db.existsTable(settingsTName)) {
            db.createTable(settingsTName);
        }
        Table tb = db.getTable(settingsTName);
        if (!tb.existsColumn(colmGuild)) {
            tb.addColumn(colmGuild);
        }
        if (!tb.existsColumn(colmWChannel)) {
            tb.addColumn(colmWChannel);
        }
        if (!tb.existsColumn(colmJRole)) {
            tb.addColumn(colmJRole);
        }
        System.out.println("MYSQL Finished");
    }

    public MYSQL getMysql() {
        return mysql;
    }
}
