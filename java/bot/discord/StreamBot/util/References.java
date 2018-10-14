/*
 * Discord - Stream Bot
 * Tucker Reed Stewart
 */

package bot.discord.StreamBot.util;

/**
 * References.java is a utility class for useful variables.
 *  
 * @author Tucker Stewart
 * @version 1.0
 */
public final class References {
	/**
	 * The token for uploading this bot to Discord's servers.
	 */
	public static final String TOKEN = process.env.BOT_TOKEN;
	
	/**
	 * The prefix of commands usable by StreamBot.
	 */
	public static final String COMMAND_PREFIX = "> ";
}
