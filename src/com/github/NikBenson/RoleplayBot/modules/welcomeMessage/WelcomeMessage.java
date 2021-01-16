package com.github.NikBenson.RoleplayBot.modules.welcomeMessage;

import com.github.NikBenson.RoleplayBot.modules.ModulesManager;
import com.github.NikBenson.RoleplayBot.modules.RoleplayBotModule;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WelcomeMessage extends ListenerAdapter implements RoleplayBotModule {
	private boolean initialized;

	private Map<Guild, WelcomeMessenger> messengers = new HashMap<>();

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
		if(ModulesManager.getActive(guild).contains(Player.class)) {
			if(!messengers.containsKey(guild)) {
				messengers.put(guild, new WelcomeMessenger(guild));
			}
		}
	}

	@Override
	public void unload(Guild guild) {
		if(messengers.containsKey(guild)) {
			messengers.remove(guild);
		}
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
