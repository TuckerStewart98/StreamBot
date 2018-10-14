/*
 * Discord - Stream Bot
 * Tucker Reed Stewart
 */

package bot.discord.StreamBot.executables;

import java.io.SyncFailedException;

import bot.discord.StreamBot.system.Preferences;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Commands.java is class that holds executable functions for Stream Bot to
 * use.
 *  
 * @author Tucker Stewart
 * @version 1.0
 */
public final class Commands {
	/**
	 * Sets the channel that the "set_default_channel" command was called
	 * in, as the default channel for messages that Stream Bot will send.
	 * Call this function when a user uses the command
	 * "set_default_channel".
	 * 
	 * @param theEvent is the message event sent to the text channels.
	 * @param thePref is model that stores the system preferences.
	 */
	public static void setDefaultChannel(final MessageReceivedEvent theEvent,
										 final Preferences thePref) {
		/* String Builder for sending a message stating success or
		   failure to set the default channel. */
		final StringBuilder builder = new StringBuilder();
		
		/* Checks to see if this user has permission to manage channels.
		   User must have the permission to "Manage Channels". */
		boolean permission = false;
		for (final Role role: theEvent.getMember().getRoles()) {
			if (role.hasPermission(Permission.MANAGE_CHANNEL)) {
				permission = true;
			}
		}
		
		
		
		if (permission) {
			// If this user has permission to use this command then set.
			try {
				thePref.setDefaultChannel(theEvent.getGuild().getName(),
						                  theEvent.getChannel().getId());
			} catch (final IllegalArgumentException ex) {
				/* If an IllegalArgumentException is thrown then attempt to
				   add the guild to preferences with the desired values and
				   any default values if necessary. */
				try {
					thePref.addGuild(theEvent.getGuild().getName(),
			                  		 theEvent.getChannel().getId());
				} catch (final SyncFailedException ex2) {
					Notifications.failedSave(theEvent.getGuild(), thePref);
				}
			} catch (final SyncFailedException ex) {
				Notifications.failedSave(theEvent.getGuild(), thePref);
			}
			
			/* Inform the user that the default channel was successfully
			   set to the current channel. */
			builder.append(theEvent.getAuthor().getAsMention());
			builder.append(" This channel will now be the default ");
			builder.append("channel. All messages sent by StreamBot ");
			builder.append("that are not provoked by user messages ");
			builder.append("will be sent to this channel.");
		} else {
			/* If the user does not have permission to use this command
			   then inform the user that Stream Bot failed to set the
			   default channel and what permission is required to use
			   this action. */
			builder.append("You do not have permission to set the ");
			builder.append("default channel ");
			builder.append(theEvent.getAuthor().getAsMention());
			builder.append(". You need to have permission to manage ");
			builder.append("channels.");
		}
		
		
		
		/* Sends the user a message of whether they were successful or
		   unsuccessful in setting the default channel. */
		theEvent.getChannel().sendMessage(builder.toString()).queue();
	}
}
