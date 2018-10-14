/*
 * Discord - Stream Bot
 * Tucker Reed Stewart
 */

package bot.discord.StreamBot;

import javax.security.auth.login.LoginException;
import bot.discord.StreamBot.listeners.CommandListener;
import bot.discord.StreamBot.listeners.GuildJoinListener;
import bot.discord.StreamBot.listeners.StreamListener;
import bot.discord.StreamBot.system.Preferences;
import bot.discord.StreamBot.util.References;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

/**
 * StreamBot.java is driver class for constructing the Stream Bot. Stream
 * Bot is a bot designed for Discord, using JDA. StreamBot will watch for
 * when users start streaming and send a notification to either the system
 * channel of that server or the channel that is selected as the default. 
 *  
 * @author Tucker Stewart
 * @version 1.0
 */
public final class StreamBot {
	/**
	 * The main method of this program. Constructs the JDA with StreamBot's
	 * token ID and adds all necessary listeners. 
	 * 
	 * @param theArgs used for command line arguments.
	 */
    public static void main(final String[] theArgs) throws Exception {
    	// The JDA for Stream Bot.
    	@SuppressWarnings("unused")
		JDA jda;
    	
    	// The builder that will create the JDA.
    	JDABuilder builder =  new JDABuilder(AccountType.BOT);
        builder.setToken(References.TOKEN);
        builder.setAutoReconnect(true);
        
        // Attempts to build the JDA and add all listeners.
        try {
        	// Initializes the preferences settings for later access.
        	Preferences pref = new Preferences();
        	pref.init();
        	
        	// Adds the listeners to the builder.
        	builder.addEventListener(new CommandListener(pref));
        	builder.addEventListener(new GuildJoinListener(pref));
        	builder.addEventListener(new StreamListener(pref));
        	
        	// Constructs the JDA.
        	jda = builder.buildBlocking();
        } catch (final LoginException ex) {
        	ex.printStackTrace();
        } catch (final InterruptedException ex) {
        	ex.printStackTrace();
        }
    }
}
