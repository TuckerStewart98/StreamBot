/*
 * Discord - Stream Bot
 * Tucker Reed Stewart
 */

package bot.discord.StreamBot.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.SyncFailedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Preferences.java is a class used for storing and accessing system
 * preference information for each guild that StreamBot is connected to.
 * An instance of the preferences class should be created and then
 * initialized using init() before Stream Bot is constructed so that the
 * information can be accessed on start-up.
 *  
 * @author Tucker Stewart
 * @version 1.0
 */
public final class Preferences {
	/**
	 * A map of each guild with the ID of the default text channel.
	 */
	private final Map<String, GuildPreferences> myPreferences;
	
	/**
	 * Delimiter for separating each preference item.
	 */
	private static final String DELIMITER = ":";
	
	/**
	 * The location of the file that contains system preferences.
	 */
	private static final String PREFERENCES_FILE = "./src/main/java/bot/discord/StreamBot/system/preferences.txt";
	
	/**
	 * The default constructor of a Preferences object. Instantiates the
	 * Hash Map to store the system preference information.
	 */
	public Preferences() {
		myPreferences = new HashMap<String, GuildPreferences>();
	}
	
	/**
	 * Call this method whenever Stream Bot is started. It will read the text
	 * file "preferences.txt" and fills the map myPreferences for easy
	 * access to system preference information. Returns true if the file
	 * was successfully read and the map was filled. This should be called
	 * only when Stream Bot is being constructed.
	 * 
	 * @return whether myPreferences was successfully initialized.
	 */
	public boolean init() {
		// Scanner for reading the file for guild system preferences.
		Scanner fileReader = null;
		// Tells whether the file was open or not.
		boolean fileFound = false;
		// Tells whether the map was successfully initialized.
		boolean result = false;
		
		// Attempts to open the file.
		try {
			fileReader = new Scanner(new File(PREFERENCES_FILE));
			fileFound = true;
		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		
		
		/* If the file was opened then continue to read the file and fill
		   the myPreferences Hash Map. */
		if (fileFound) {
			while (fileReader.hasNextLine()) {
				final String[] pref = fileReader.nextLine().split(DELIMITER);
				myPreferences.put(pref[0],
						        new GuildPreferences(pref[0], pref[1]));
			}
			result = true;
		}
		
		// Return true if the map was successfully initialized.
		return result;
	}
	
	/**
	 * Adds an entry for a new guild. Sets the system preferences with some
	 * default information. This should be could when Stream Bot joins a
	 * new guild or if an IllegalArgumentException is thrown by any of the
	 * accessor or mutator methods
	 * 
	 * @param theGuild is the name of the guild this entry pertains to.
	 * @param theDefaultChannelID is the ID of the default channel.
	 * @throws SyncFailedException if changes were unable to be saved to
	 * "preferences.txt"
	 */
	public void addGuild(final String theGuild,
			             final String theDefaultChannelID)
			             throws SyncFailedException {
		myPreferences.put(theGuild, new GuildPreferences(theGuild,
				        theDefaultChannelID));

		// Save the contents again after adding the entry.
		try {
			save();
		} catch (final SyncFailedException ex) {
			throw new SyncFailedException("Unable to save changes to "
										  + "system preferences.");
		}
	}
	
	/**
	 * Sets the Default Channel of this guild. Saves the results to text
	 * file afterward. Only call this method if theGuild already exists
	 * as a key in myPreferences, otherwise an IllegalArgumentException.
	 * If an IllegalArgumentException is thrown, then in the catch block
	 * use addGuild to make an entry with some default information.
	 * 
	 * @param theGuild is the guild this setting pertains to.
	 * @param theDefaultChannelID the ID of the new default channel.
	 * @throws IllegalArgumentException if theGuild is not a key in
	 * myPreferences.
	 * @throws SyncFailedException if changes were unable to be saved to
	 * "preferences.txt"
	 */
	public void setDefaultChannel(final String theGuild,
			                      final String theDefaultChannelID)
			                      throws SyncFailedException {
		/* Throws an exception if the wasn't a previous entry with the
		   guild name theGuild. */
		if (!myPreferences.containsKey(theGuild)) {
			throw new IllegalArgumentException("Preferences does not have"
											   + "a key with 'theGuild'.");
		}
		
		myPreferences.get(theGuild).setDefaultChannelID(theDefaultChannelID);
		
		// Save the contents again after editing the entry.
		try {
			save();
		} catch (final SyncFailedException ex) {
			throw new SyncFailedException("Unable to save changes to "
                                          + "system preferences.");
		}
	}
	
	/**
	 * Returns the ID of the Default Channel for this guild. Throws an
	 * IllegalArguemntException if no such key, theGuild,
	 * exists in myPreferences. This method should only be called if
	 * an entry with theGuild exists in Preferences. If an
	 * IllegalArgumentException is thrown, then in the catch block use
	 * addGuild to make an entry with some default information.
	 * 
	 * @param theGuild is the guild you want the Channel ID of.
	 * @return the Default Channel ID of theGuild.
	 * @throws IllegalArgumentException if theGuild is not a key in
	 * myPreferences.
	 */
	public String getDefaultChannel(final String theGuild) {
		/* Throws an exception if the wasn't a previous entry with the
		   guild name theGuild. */
		if (!myPreferences.containsKey(theGuild)) {
			throw new IllegalArgumentException("Preferences does not have"
											   + "a key with 'theGuild'.");
		}
		
		return myPreferences.get(theGuild).getDefaultChannelID();
	}
	
	/**
	 * Returns true if there is an entry in myPrefernces for guilds with
	 * the name theGuild.
	 * 
	 * @param theGuild the name of the guild being checked for.
	 * @return whether there are preferences settings for this guild in
	 * myPreferences.
	 */
	public boolean hasGuild(final String theGuild) {
		return myPreferences.containsKey(theGuild);
	}
	
	/**
	 * This method should be called after editing the contents of one
	 * entry. Calls saveContents() to save the contents of myPreferences to
	 * the file "preferences.txt" for later use after Stream Bot shuts down.
	 * Will print that the save was unsuccessful if saveContents returns
	 * false.
	 * 
	 * @throws SyncFailedException if changes were unable to be saved to
	 * "preferences.txt"
	 */
	private void save() throws SyncFailedException {
		if (!this.saveContents()) {
			// Unable to open and save changes to "preferences.txt".
			throw new SyncFailedException("Unable to save changes to "
					                      + "system preferences.");
		}
	}
	
	/**
	 * This method should be called after editing the contents of one
	 * entry. This will save the contents of myPreferences in the text file
	 * "preferences.txt" to ensure that it accessed after Stream Bot shuts
	 * down. This method will return true if a save was successfully done.
	 * 
	 * @return whether the save was successful.
	 */
	private boolean saveContents() {
		// PrintStream for saving the contents of myPrefernces to the file.
		PrintStream stream = null;
		// Tells whether the file could be found.
		boolean fileFound = false;
		// Tells whether the save was successful.
		boolean result = false;
		
		
		// Try to set the print stream.
		try {
			stream = new PrintStream(new File(PREFERENCES_FILE));
			fileFound = true;
		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		
		/* If the file was found, write the contents of myPreferences to
		   "preferences.txt". */
		if (fileFound) {
			for (final String guild: myPreferences.keySet()) {
				final StringBuilder builder = new StringBuilder();
				GuildPreferences p = myPreferences.get(guild);
				
				builder.append(p.getGuildName());
				builder.append(DELIMITER);
				builder.append(p.getDefaultChannelID());
				stream.println(builder.toString());
			}
			result = true;
		}
		
		// Return whether the save was successful.
		return result;
	}
	
	/**
	 * GuildPreferences.java is an object that stores information pertaining to
	 * the preferences of a guild for how Stream Bot should interact with that
	 * particular guild.
	 * 
	 * @author Tucker Stewart
	 * @version 1.0
	 */
	private final class GuildPreferences {
		/**
		 * The name of the guild these preference settings pertains to.
		 */
		private final String myGuild;
		
		/**
		 * The ID of the selected default channel to be used by Stream Bot.
		 */
		private String myDefaultChannelID;
		
		/**
		 * The constructor of the GuildPreference object. Values are set to
		 * the ones sent in the parameters.
		 * 
		 * @param theGuild is the name of the guild this object pertains to.
		 * @param theDefaultChannelID is the ID of the default text channel.
		 */
		public GuildPreferences(final String theGuild,
				                final String theDefaultChannelID) {
			this.myGuild = theGuild;
			this.myDefaultChannelID = theDefaultChannelID;
		}
		
		/**
		 * Returns the name of the guild this object refers to.
		 * 
		 * @return the name of the guild this object refers to.
		 */
		public String getGuildName() {
			return this.myGuild;
		}
		
		/**
		 * Returns the ID of the Default Text Channel.
		 * 
		 * @return the ID of the Default Text Channel.
		 */
		public String getDefaultChannelID() {
			return this.myDefaultChannelID;
		}
		
		/**
		 * Sets this GuildPreference's guild name to the new 
		 * 
		 * @param theDefaultChannelID is the ID of the default text channel.
		 */
		public void setDefaultChannelID(final String theDefaultChannelID) {
			this.myDefaultChannelID = theDefaultChannelID;
		}
		
		/**
		 * Tests to see if this GuildPreferences is equal to theOther. This
		 * method will return true only if theOther is not null, it is a
		 * GuildPreference object, the myGuild names of this object and theOther
		 * are equal.
		 * 
		 * @return true if the
		 */
		@Override
		public boolean equals(final Object theOther) {
			boolean result = false;
			
			if (this == theOther) {
				result = true;
			} else if (theOther != null
					   && theOther.getClass().equals(this.getClass())) {
				GuildPreferences other = (GuildPreferences) theOther;
				result = this.myGuild.equals(other.myGuild);
			}
			
			return result;
		}
		
		/**
		 * Returns the hash code of this object.
		 * 
		 * @return the hash code of this object.
		 */
		@Override
		public int hashCode() {
			return Objects.hash(this.myGuild);
		}
		
		/**
		 * Returns the string representation of this GuildPreference object.
		 * <hr>this.myGuild:this.myDefault
		 * 
		 * @return the string representation of this GuildPreference object.
		 */
		@Override
		public String toString() {
			return this.myGuild + ":" + this.myDefaultChannelID;
		}
	}
}
