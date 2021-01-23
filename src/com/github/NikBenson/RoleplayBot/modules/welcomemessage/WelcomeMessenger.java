package com.github.NikBenson.RoleplayBot.modules.welcomemessage;

import com.github.nikbenson.roleplaybot.commands.context.UserContext;
import com.github.nikbenson.roleplaybot.configurations.ConfigurationManager;
import com.github.nikbenson.roleplaybot.configurations.ConfigurationPaths;
import com.github.nikbenson.roleplaybot.configurations.JSONConfigured;
import com.github.nikbenson.roleplaybot.messages.MessageFormatter;
import com.github.nikbenson.roleplaybot.modules.player.Player;
import com.github.nikbenson.roleplaybot.modules.player.PlayerEventListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;

public class WelcomeMessenger implements JSONConfigured, PlayerEventListener {
	private final Guild GUILD;

	private MessageFormatter<UserContext> messageFormatter;

	public WelcomeMessenger(Guild guild) {
		GUILD = guild;
	}

	@Override
	public void onPlayerCreate(Player player) {
		sendTo(player.getUser());
	}

	public void sendTo(User user) {
		user.openPrivateChannel().queue(channel -> channel.sendMessage(messageFormatter.createMessage(new UserContext(user))).queue());
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public @NotNull File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(GUILD), ConfigurationPaths.WELCOME_MESSAGE_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		String message = (String) json.get("message");
		JSONArray valuesJSON = (JSONArray) json.get("values");
		String[] values = new String[valuesJSON.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = (String) valuesJSON.get(i);
		}

		messageFormatter = new MessageFormatter<>(UserContext.class, message, values);
	}

	@Override
	public Guild getGuild() {
		return GUILD;
	}
}
