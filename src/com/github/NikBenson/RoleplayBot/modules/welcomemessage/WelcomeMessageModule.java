package com.github.nikbenson.RoleplayBot.modules.welcomemessage;

import com.github.nikbenson.roleplaybot.modules.player.PlayerModule;
import com.github.nikbenson.roleplaybot.configurations.ConfigurationManager;
import com.github.nikbenson.roleplaybot.modules.ModulesManager;
import com.github.nikbenson.roleplaybot.modules.RoleplayBotModule;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class WelcomeMessageModule implements RoleplayBotModule {
	private final Map<Guild, WelcomeMessenger> messengers = new HashMap<>();

	public WelcomeMessageModule() {}

	@Override
	public boolean isActive(Guild guild) {
		return messengers.containsKey(guild);
	}

	@Override
	public void load(Guild guild) {
		PlayerModule playerModule = (PlayerModule) ModulesManager.require("com.github.NikBenson.RoleplayBot.modules.player.PlayerModule");

		if (!messengers.containsKey(guild)) {
			WelcomeMessenger welcomeMessenger = new WelcomeMessenger(guild);
			ConfigurationManager configurationManager = ConfigurationManager.getInstance();

			playerModule.getPlayerManager(guild).registerEventListener(welcomeMessenger);

			configurationManager.registerConfiguration(welcomeMessenger);
			try {
				configurationManager.load(welcomeMessenger);
			} catch (Exception ignored) {}

			messengers.put(guild, welcomeMessenger);
		}
	}

	@Override
	public void unload(Guild guild) {
		messengers.remove(guild);
	}

	@Override
	public Guild[] getLoaded() {
		return messengers.keySet().toArray(new Guild[0]);
	}
}
