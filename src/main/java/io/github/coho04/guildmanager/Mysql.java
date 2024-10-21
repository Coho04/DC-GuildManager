package io.github.coho04.guildmanager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.sentry.Sentry;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class Mysql {

    private final HikariDataSource source;

    public Mysql()  {
        this.source = getConfig();
        try {
            Statement statement = this.source.getConnection().createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS `" + Main.getCustomConfig().getMysqlDatabase() +"`;");
            statement.execute("USE `" + Main.getCustomConfig().getMysqlDatabase() + "`;");
            statement.execute("CREATE TABLE IF NOT EXISTS settings (id INT AUTO_INCREMENT NOT NULL PRIMARY KEY, guild_id LONG NOT NULL, welcome_channel LONG NULL, join_role LONG NULL);");
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            Sentry.captureException(exception);
        }
        System.out.println("[MYSQL] Initialized MySQL!");
    }

    private static HikariDataSource getConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + Main.getCustomConfig().getMysqlHostname() + ":" + Main.getCustomConfig().getMysqlPort());
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(30));
        config.setIdleTimeout(TimeUnit.MINUTES.toMillis(10));
        config.setMaxLifetime(TimeUnit.MINUTES.toMillis(30));
        config.setInitializationFailTimeout(0);
        config.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(60));
        config.setUsername(Main.getCustomConfig().getMysqlUsername());
        config.setPassword(Main.getCustomConfig().getMysqlPassword());
        config.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(config);
    }

    public HikariDataSource getSource() {
        return source;
    }
}
