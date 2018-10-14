/*
 * Discord - Stream Bot
 * Tucker Reed Stewart
 */

package bot.discord.StreamBot.listeners;

import java.io.SyncFailedException;

import bot.discord.StreamBot.executables.Notifications;
import bot.discord.StreamBot.system.Preferences;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * GuildJoinListener.java is a listener for whenever StreamBot joins a
 * server. This will initialize the system preferences for the new guild
 * and sends an introductory message to inform the members of this new
 * guild of how to use StreamBot.
 *  
 * @author Tucker Stewart
 * @version 1.0
 */
public final class GuildJoinListener extends ListenerAdapter {
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
	public GuildJoinListener(final Preferences thePref) {
		super();
		myPreferences = thePref;
	}
	
	/**
	 * When Stream Bot joins the server, it will initialize the system
	 * preferences with default values and send an introductory message.
	 * 
	 * @param theEvent is the event Stream Bot joins the server.
	 */
	@Override
	public void onGuildJoin(final GuildJoinEvent theEvent) {
		Guild guild = theEvent.getGuild();
		
		/* Adds the entry to myPrefences, if it failed to save the
		   information then it will send a message to the guild, informing
		   them that an error has occurred. */
		try {
			myPreferences.addGuild(guild.getName(),
								   guild.getSystemChannel().getId());
		} catch (final SyncFailedException ex) {
			Notifications.failedSave(guild, myPreferences);
		}
		
		Notifications.sendIntroduction(theEvent.getGuild(), myPreferences);
	}
}
