/*
 *     This file is part of zicbot.

    zicbot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    zicbot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with zicbot.  If not, see <http://www.gnu.org/licenses/>.

 */
package bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Class Irc. This is the main runtime class, with methods for every action
 * the bot can take. It has the run loop run() and initializes all user specified
 * settings.
 */
public class Irc {

	private String server;
	private String port;
	private String botnick;
	private String login;
	private String channel;
	private String line;
	private String nick;
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private XMLReader xml;
	private HtmlParser html;
	private ArrayList<String> commands;
	private ArrayList<String> channels;
	private ArrayList<String> admins;
	private DbConnection players;
    private Dice dice;
	
	/**
	 * Instantiates a new irc object and the other action objects, registers
	 * commands.
	 *
	 * @param server The irc server to connect to
	 * @param botnick The botnick. Varies in max length between the various
	 * networks. To be safe, stay below 8 characters
	 * @param port The port, this is most commonly 6667
	 * @param login The login. This shows up in front of the host name when the
	 * bot has connected and serves as its "username" on IRC
	 */
	public Irc(String server, String botnick, String port, String login) {
		// Set USER_AGENT string allowed by Google
		System.setProperty("http.agent","Opera/9.80 (Windows NT 6.0; U; en) Presto/2.2.15 Version/10.00");
		this.server = server;
		this.port = port;
		this.botnick = botnick;
		this.login = login;

		xml = new XMLReader();
		html = new HtmlParser();
		admins = new ArrayList<String>();
		channels = new ArrayList<String>();
		commands = new ArrayList<String>();
        dice = new Dice();
		commands.add("!players");
		commands.add("!google");
		commands.add("!tlf");
		commands.add("!weather");
		commands.add("!join");
		commands.add("!part");
		commands.add("!bash");
		commands.add("!help");
        commands.add("!roll");
	}

	/**
	 * Initialize. Connect to IRC, join the channels, and transfer control to the main run() loop.
	 * Instantiates main to get the db-data from the config-file.
	 * 
	 */
	public void initialize() {

		Main db = new Main();
		players = db.getDbConnection();
		Iterator<String> it = channels.iterator();
		connect();
		login();
		while(it.hasNext()) {
			joinChannel(it.next());
		}
		run();
	}

	/**
	 * Adds an admin to the list
	 * 
	 * @param admin
	 *            The nickname of the admin to add
	 */
	public void setAdmin(String admin){
		admins.add(admin);
	}
	
	/**
	 * Adds a channel to the list
	 * 
	 * @param channel
	 *            The name of the channel to add
	 */
	public void setChannel(String channel){
		channels.add(channel);
	}
	
	/**
	 * Connect. Set up a Socket and buffered connection
	 *
	 */
	public void connect() {
		try {
			socket = new Socket(server, Integer.parseInt(port));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Could not connect to server: "+server);
			System.exit(0);
		}
	}

	/**
	 * Returns the argument following a command: !command argument.
	 * 
	 * @return the argument
	 */
	public String getArgument() {
		String arg = line.substring(
				line.lastIndexOf("!") + getCommand().length()).trim();
		return arg;
	}

	/**
	 * Gets the current channel.
	 * 
	 * @return the current channel
	 */
	public String getChannel() {
		if (line.contains("PRIVMSG #")) {
			channel = line.substring(line.indexOf("PRIVMSG")+7,
					line.lastIndexOf(":")).trim();
		} else if (line.contains("JOIN :#")) {
			channel = line.substring(line.indexOf("JOIN :#")+6);
		} else if (line.contains("PART #")) {
			channel = line.substring(line.indexOf("PART #")+5);
		}
		return channel;
	}
	
	/**
	 * Gets the current nick.
	 * 
	 * @return the current nick
	 */
	public String getNick() {
		if (line.contains("PRIVMSG #") || line.contains("JOIN :#") || line.contains("PART #")) {
			nick = line.substring(line.indexOf(":")+1,line.indexOf("!")).trim();
		}
		return nick;
	}
	
	/**
	 * Gets the current command.
	 *
	 * @return the current command
	 */
	public String getCommand() {
		String[] command = new String[2];
		if (line.contains("PRIVMSG " + getChannel() + " :!")) {
			command = line.substring(line.lastIndexOf("!")).split(" ");
		} else {
			command[0] = "";
		}
		if (isValidCommand(command[0])) {
			return command[0];
		} else {
			return "";
		}
	}

	/**
	 * Checks if a command is valid, eg if it was registered during
	 * initialize().
	 * 
	 * @param command
	 *            the command
	 * @return Boolean true/false
	 */
	public Boolean isValidCommand(String command) {
		if (commands.contains(command)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Join channel.
	 *
	 * @param channel the channel to join
	 */
	public void joinChannel(String channel) {
		try {
			writer.write("JOIN " + channel + "\r\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	
	/**
	 * Part channel.
	 *
	 * @param channel the channel to part
	 */
	public void partChannel(String channel) {
		try {
			writer.write("PART " + channel + "\r\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Keep alive. Required to stay logged in. We have 250 seconds to reply with
	 * PONG whenever we recieve a PING from the server
	 * If it does not get a PING, it simply writes the line to stdout.
	 *
	 */
	public void keepAlive() {
		try {
			if (line.startsWith("PING ")) {
				writer.write("PONG " + line.substring(5) + "\r\n");
				writer.flush();
			} else {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Login. Register our nick and send the USER command to the server. We wait
	 * for the message of the day from the server to finish (376)
	 *
	 */
	public void login() {
		try {
			writer.write("NICK " + botnick + "\r\n");
			writer.write("USER " + login + " 8 * : Java IRC Bot Project\r\n");
			writer.flush();

			while ((line = reader.readLine()) != null) {
				if (line.indexOf("376") >= 0) {
					System.out.println("Connected!");
					break;
				} else if (line.indexOf("433") >= 0) {
					System.out.println("Nickname is already in use.");
					return;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Write message. Writes a string to the current channel
	 * 
	 * @param message
	 *            the message to send
	 */
	public void writeMessage(String message) {
		try {
			writer.write("PRIVMSG " + getChannel() + " :" + message + "\r\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

	/**
	 * Reader. This method is used to catch actions that occur on the channels
	 * we are currently on. Here we look for commands the bot should recognize
	 * and do the actions they should perform. When ever we would look to check
	 * wether actions occur, we should use this method.
	 *
	 */
	public void reader() {

		try {
			if (getCommand().matches("!players")) {
				writeMessage(players.getPlayers());
			}
			if (getCommand().matches("!join") && admins.contains(getNick())) {
				joinChannel(getArgument());
			}
			if (getCommand().matches("!part") && admins.contains(getNick())) {
				partChannel(getArgument());
			}
			if (getCommand().matches("!tlf")) {
				writeMessage(html.getTlfData(getArgument()));
			}
			if (getCommand().matches("!google")) {
				writeMessage(html.googleSearch(getArgument()));
			}
			if (getCommand().matches("!weather")) {
				writeMessage(xml.parseData(getArgument()));
			}
			if (getCommand().matches("!bash")) {
				writeMessage(html.getBashQuote());
			}
            if (getCommand().matches("!roll")) {
				writeMessage(dice.rollPrint(getArgument()));
			}
			if (getCommand().matches("!help")) {
				writer.write("PRIVMSG " + getChannel() + " :Available commands: ");
				for (String command: commands) {
					writer.write(command + " ");
				}
				writer.write("\r\n");
				writer.flush();
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Run. This is the main runloop that is passed execution from initialize()
	 * when we start the bot. keepAlive() and reader() are called here for each
	 * line the server sends us.
	 *
	 */
	public void run() {
		// this is the main run loop
		try {
			while ((line = reader.readLine()) != null) {
				keepAlive();
				reader();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
}