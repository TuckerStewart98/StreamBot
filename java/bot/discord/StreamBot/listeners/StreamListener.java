/*
 * Discord - Stream Bot
 * Tucker Reed Stewart
 */

package bot.discord.StreamBot.listeners;

import bot.discord.StreamBot.executables.Notifications;
import bot.discord.StreamBot.system.Preferences;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * StreamListener.java is a ListenerAdapter that watches for update events
 * and executes the corresponding function to notify user within a server
 * if a user has started streaming.
 *  
 * @author Tucker Stewart
 * @version 1.0
 */
public final class StreamListener extends ListenerAdapter {
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
	public StreamListener(final Preferences thePref) {
		super();
		myPreferences = thePref;
	}
	
	/**
	 * Handles UserUpdateGameEvents. If the new game is being streamed,
	 * then a notification is sent to inform that the user is streaming.
	 * 
	 * @param theEvent is the event of the user.
	 */
	@Override
	public void onUserUpdateGame(final UserUpdateGameEvent theEvent) {
		// Makes a notification if the user is streaming.
		if (theEvent.getNewGame() != null
			&& theEvent.getNewGame().getType().equals(GameType.STREAMING)) {
			Notifications.sendNotificationStream(theEvent, myPreferences);
		}
	}
}
