package de.goldendeveloper.guildmanager;


public class Main {

    private static Discord discord;

    public static void main(String[] args) {
        discord = new Discord("OTU2OTEyMzkwNjczNDEyMTY2.Yj3IHA.BcfokCCajYi2jU_1tolo9xNErws");
    }

    public static Discord getDiscord() {
        return discord;
    }
}