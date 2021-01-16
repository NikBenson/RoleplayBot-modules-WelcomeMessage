package com.github.NikBenson.RoleplayBot.modules.welcomeMessage;

import com.github.NikBenson.RoleplayBot.commands.context.UserContext;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import com.github.NikBenson.RoleplayBot.messages.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;

public class WelcomeMessenger implements JSONConfigured {
	private MessageFormatter<UserContext> messageFormatter;
	private boolean enabled = false;

	public void sendTo(User user) {
		if(enabled) {
			user.openPrivateChannel().queue(channel -> {
				channel.sendMessage(messageFormatter.createMessage(new UserContext(user))).queue();
			});
		}
	}

	public WelcomeMessenger(Guild guild) {
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.WELCOME_MESSAGE_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		String message = (String) json.get("message");
		JSONArray valuesJSON = (JSONArray) json.get("values");
		String[] values = new String[valuesJSON.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = (String) valuesJSON.get(i);
		}

		messageFormatter = new MessageFormatter<>(message, values);

		enabled = true;
	}
}
