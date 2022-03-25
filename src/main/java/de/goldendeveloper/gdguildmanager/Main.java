package de.goldendeveloper.gdguildmanager;


public class Main {

    private static Discord discord;

    public static void main(String[] args) {
        discord = new Discord("OTMxMTQxMzU5MTcwMTA5NDUx.YeAG9w.fRlgo2B9Luufjg2a0jfS3K27GkU");
    }

    public static Discord getDiscord() {
        return discord;
    }
}