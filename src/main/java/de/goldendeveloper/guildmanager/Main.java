package de.goldendeveloper.guildmanager;

public class Main {

    private static Discord discord;
    private static Config config;
    private static CreateMysql createMYSQl;

    public static void main(String[] args) {
        config = new Config();
        createMYSQl = new CreateMysql(config.getMysqlHostname(), config.getMysqlUsername(), config.getMysqlPassword(), config.getMysqlPort());
        discord = new Discord(config.getDiscordToken());
    }

    public static Discord getDiscord() {
        return discord;
    }

    public static Config getConfig() {
        return config;
    }

    public static CreateMysql getCreateMysql() {
        return createMYSQl;
    }
}