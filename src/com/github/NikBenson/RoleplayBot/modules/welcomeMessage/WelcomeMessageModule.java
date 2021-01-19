package com.github.NikBenson.RoleplayBot.modules.welcomeMessage;

import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.modules.ModulesManager;
import com.github.NikBenson.RoleplayBot.modules.RoleplayBotModule;
import com.github.NikBenson.RoleplayBot.modules.player.Player;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WelcomeMessageModule extends ListenerAdapter implements RoleplayBotModule {
	private boolean initialized;

	private final Map<Guild, WelcomeMessenger> messengers = new HashMap<>();

	@Override
	public boolean isActive(Guild guild) {
		return messengers.containsKey(guild);
	}

	@Override
	public void load(Guild guild) {
		if(!initialized) {
			guild.getJDA().addEventListener(this);
			initialized = true;
		}
		try {
			if (ModulesManager.getActive(guild).contains(Player.class)) {
				if (!messengers.containsKey(guild)) {
					WelcomeMessenger welcomeMessenger = new WelcomeMessenger(guild);
					ConfigurationManager configurationManager = ConfigurationManager.getInstance();

					configurationManager.registerConfiguration(welcomeMessenger);
					try {
						configurationManager.load(welcomeMessenger);
					} catch (Exception ignored) {}

					messengers.put(guild, welcomeMessenger);
				}
			}
		} catch (NoClassDefFoundError ignored) {}
	}

	@Override
	public void unload(Guild guild) {
		messengers.remove(guild);
	}

	@Override
	public Guild[] getLoaded() {
		return messengers.keySet().toArray(new Guild[0]);
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
		Guild guild = event.getGuild();
		if(messengers.containsKey(guild)) {
			WelcomeMessenger messenger = messengers.get(guild);

			messenger.sendTo(event.getUser());
		}
	}
}
