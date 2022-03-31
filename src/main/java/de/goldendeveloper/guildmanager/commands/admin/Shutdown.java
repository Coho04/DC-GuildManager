package de.goldendeveloper.guildmanager.commands.admin;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import de.goldendeveloper.guildmanager.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Shutdown {

    public Shutdown(SlashCommandInteractionEvent e) {
        User _Coho04_ = e.getJDA().getUserById("513306244371447828");
        User zRazzer = e.getJDA().getUserById("428811057700536331");
        if (e.getUser() == zRazzer || e.getUser() == _Coho04_) {
            WebhookEmbedBuilder embed = new WebhookEmbedBuilder();
            embed.setAuthor(new WebhookEmbed.EmbedAuthor(Main.getDiscord().getBot().getSelfUser().getName(), Main.getDiscord().getBot().getSelfUser().getAvatarUrl(), "https://Golden-Developer.de"));
            embed.addField(new WebhookEmbed.EmbedField(false, "[Status]", "OFFLINE"));
            embed.setColor(0xFF0000);
            embed.setFooter(new WebhookEmbed.EmbedFooter("@Golden-Developer", Main.getDiscord().getBot().getSelfUser().getAvatarUrl()));
            new WebhookClientBuilder(Main.getConfig().getDiscordWebhook()).build().send(embed.build());
            e.getInteraction().reply("Der Bot wird nun heruntergefahren").queue();
            e.getJDA().shutdown();
        } else {
            e.getInteraction().reply("Dazu hast du keine Rechte du musst für diesen Befehl der Bot inhaber sein!").queue();
        }
    }
}
