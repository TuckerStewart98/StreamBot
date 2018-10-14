/*
 * Discord - Stream Bot
 * Tucker Reed Stewart
 */

package bot.discord.StreamBot.listeners;

import bot.discord.StreamBot.executables.Commands;
import bot.discord.StreamBot.executables.Notifications;
import bot.discord.StreamBot.system.Preferences;
import bot.discord.StreamBot.util.References;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * CommandListener.java is a listener that listens for input commands from
 * users and either executes the corresponding commands
 *  
 * @author Tucker Stewart
 * @version 1.0
 */
public final class CommandListener extends ListenerAdapter {
	/**
	 * The model that stores system preferences information.
	 */
	private final Preferences myPreferences;
	
	/**
	 * The default constructor for this listener. Saves a reference to
	 * Stream Bot's system preferences model for further access.
	 * 
	 * @param thePref is model that stores the system preferences.
	 */
	public CommandListener(final Preferences thePref) {
		super();
		myPreferences = thePref;
	}
	
	/**
	 * Messages are read for commands. Any commands found will execute the
	 * corresponding function. Messages must start with the command prefix,
	 * References.COMMAND_PREFIX, for it to be a command.
	 * 
	 * @param theEvent is the message event sent to a text channels.
	 */
	@Override
	public void onMessageReceived(final MessageReceivedEvent theEvent) {
		if (theEvent.getMessage().getContentRaw().startsWith(References.COMMAND_PREFIX)) {
			String command = theEvent.getMessage().getContentRaw().substring(2);
			if ("help".equals(command)) {
				Notifications.sendHelp(theEvent);
			} else if ("ping".equals(command)) {
				Notifications.sendPing(theEvent);
			} else if ("set_default_channel".equals(command)) {
				Commands.setDefaultChannel(theEvent, myPreferences);
			}
		}
	}
}
