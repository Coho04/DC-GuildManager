package de.goldendeveloper.guildmanager;


public class Main {

    private static Discord discord;
    private static Config config;

    public static void main(String[] args) {
        config = new Config();
        discord = new Discord(config.getDiscordToken());
    }

    public static Discord getDiscord() {
        return discord;
    }

    public static Config getConfig() {
        return config;
    }
}