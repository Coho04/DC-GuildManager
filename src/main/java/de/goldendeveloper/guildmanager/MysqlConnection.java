package de.goldendeveloper.guildmanager;

import de.goldendeveloper.guildmanager.errors.CustomExceptionHandler;
import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.Table;

public class MysqlConnection {

    private final MYSQL mysql;
    public static final String dbName = "GD-GuildManager";
    public static final String settingsTName = "Settings";
    public static final String colmGuild = "Guild";
    public static final String colmWChannel = "WelcomeChannel";
    public static final String colmJRole = "JoinRole";

    public MysqlConnection(String hostname, String username, String password, int port) {
        mysql = new MYSQL(hostname, username, password, port, new CustomExceptionHandler());
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
