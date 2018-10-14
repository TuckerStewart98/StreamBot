/*
 * Discord - Stream Bot
 * Tucker Reed Stewart
 */

package bot.discord.StreamBot.executables;

import java.io.SyncFailedException;

import bot.discord.StreamBot.system.Preferences;
import bot.discord.StreamBot.util.References;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;

/**
 * Notifications.java holds functions for sending notifications and
 * messages to the appropriate guild(s).
 *  
 * @author Tucker Stewart
 * @version 1.0
 */
public final class Notifications {
	/**
	 * Informs all users of this guild that Stream Bot has failed to save
	 * changes. Should be called a SyncFailedException is caught.
	 * 
	 * @param theGuild is the guild that changed system preferences but
	 * failed to save those changes.
	 * @param thePref is model that stores the system preferences.
	 */
	public static void failedSave(final Guild theGuild,
			                      final Preferences thePref) {
		// Builds the message.
		final StringBuilder builder = new StringBuilder();
		builder.append("An error has occurred when attempting to save ");
		builder.append("system preferences. ");
		builder.append("You may use this command again to save these ");
		builder.append("changes, or if this error has multiple times ");
		builder.append("or occurred in the event ");
		builder.append("of adding Stream Bot to your server then please ");
		builder.append("remove Stream Bot and attempt to add Stream Bot ");
		builder.append("again.");
		
		// Sends the messages to the default channel.
		sendMessageToDefaultChannel(theGuild, thePref, builder.toString());
	}
	
	/**
	 * Sends a message listing all the commands and their purpose to the
	 * channel that "> help" was called in.
	 * 
	 * @param theEvent is the event the message "> help" is sent.
	 */
	public static void sendHelp(final MessageReceivedEvent theEvent) {
		// Builds the message.
		final StringBuilder builder = new StringBuilder();
		builder.append("**COMMANDS:**\n\n");
		builder.append("`");
		builder.append(References.COMMAND_PREFIX);
		builder.append("ping`\n");
		builder.append("This command pings Stream Bot. ");
		builder.append("StreamBot will respond with \"Hello @{username}\". ");
		builder.append("Use this to test connection to SteamBot.\n");
		builder.append("`");
		builder.append(References.COMMAND_PREFIX);
		builder.append("set_default_channel`\n");
		builder.append("The channel this command is used in will become ");
		builder.append("the default channel. ");
		builder.append("The default channel is the channel used for ");
		builder.append("messages unprovoked by commands. ");
		builder.append("You must have permission to \"Manage Channels\" to ");
		builder.append("use this command.");
		
		// Sends the message to the channel that the command was used in.
		theEvent.getChannel().sendMessage(builder.toString()).queue();
	}
	
	/**
	 * Sends an introductory message to the default channel of the server.
	 * 
	 * @param theGuild is the guild Stream Bot just joined.
	 * @param thePref is model that stores the system preferences.
	 */
	public static void sendIntroduction(final Guild theGuild,
										final Preferences thePref) {
		// Builds the message.
		final StringBuilder builder = new StringBuilder();
		builder.append("Thank you for adding StreamBot to your server. ");
		builder.append("\nThe command prefix for Stream Bot is `");
		builder.append(References.COMMAND_PREFIX);
		builder.append("`. ");
		builder.append("Stream Bot is a bot designed for streamers. ");
		builder.append("If a user has connected their streaming service,");
		builder.append(" such as Twitch or Youtube, with Discord then ");
		builder.append("Stream Bot will send a notification to the ");
		builder.append("server informing other users when that user ");
		builder.append("starts streaming. ");
		builder.append("For a list of commands, use `");
		builder.append(References.COMMAND_PREFIX);
		builder.append("help`.");
		
		// Sends the messages to the default channel.
		sendMessageToDefaultChannel(theGuild, thePref, builder.toString());
	}
	
	/**
	 * Sends a notification message in the default channel of the server
	 * informing all other users that the user that triggered this event
	 * has begun streaming along with the URL to their streaming service.
	 * Should only be called if this user has started streaming.
	 * 
	 * @param theEvent is an event where a user has started streaming.
	 * @param thePref is model that stores the system preferences.
	 */
	public static void sendNotificationStream(final UserUpdateGameEvent theEvent,
											  final Preferences thePref) {
		final Game game = theEvent.getNewGame();
		
		// Builds the message.
		final StringBuilder builder = new StringBuilder();
		builder.append("@everyone, ");
		builder.append(theEvent.getEntity().getName());
		builder.append(" has started streaming " + game.getName() + ".\n");
		// Checks to see if the user has an associated URL.
		if (game.getUrl() != null) {
			builder.append(game.getUrl());
		}
		
		// Sends the messages to the default channel.
		sendMessageToDefaultChannel(theEvent.getGuild(), thePref,
				                    builder.toString());
	}
	
	/**
	 * A command used for testing to see if Stream Bot responding.
	 * Call this function when a user uses the "ping" command.
	 * 
	 * @param theEvent is the event the message "> ping" is sent.
	 */
	public static void sendPing(final MessageReceivedEvent theEvent) {
		theEvent.getChannel().sendMessage("Hello "
				+ theEvent.getAuthor().getAsMention()).queue();
	}
	
	/**
	 * Send the desired message, theMessage, to the default channel of
	 * theGuild.
	 * 
	 * @param theGuild is the guild the message is being sent to.
	 * @param thePref is model that stores the system preferences.
	 * @param theMessage is the message being sent.
	 */
	private static void sendMessageToDefaultChannel(final Guild theGuild,
			                                        final Preferences thePref,
			                                        final String theMessage) {
		try {
			// Try sending the message to the default channel of the guild.
			theGuild.getTextChannelById(thePref.getDefaultChannel(
					                    theGuild.getName())).sendMessage(
					                    theMessage).queue();
		} catch (final IllegalArgumentException ex) {
			/* If an IllegalArgumentException is thrown then it means
			   there wasn't an entry for this guild. Attempt to add an
			   entry for this guild with default values. */
			try {
				thePref.addGuild(theGuild.getName(),
		                  		 theGuild.getSystemChannel().getId());
				theGuild.getTextChannelById(thePref.getDefaultChannel(
		                 					theGuild.getName())).sendMessage(
		                 					theMessage).queue();
			} catch (final SyncFailedException ex2) {
				Notifications.failedSave(theGuild, thePref);
			} catch (final IllegalArgumentException ex2 ) {
				/* This shouldn't happen as the system preferences was set
				   in response to the first IllegalArgumentException
				   thrown. */
				ex2.printStackTrace();
			}
		}
	}
}
