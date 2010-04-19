package bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Irc. This is the main runtime class, with methods for every action the bot can take. It has the runloop run()
 * and initializes all user specified settings.
 */
public class Irc {

	/**
	 * Instantiates a new irc.
	 *
	 * @param server the server
	 * @param port the port
	 * @param botnick the botnick
	 * @param login the login
	 * @param channel the channel
	 */
	public Irc(String server, String port, String botnick, String login,
			String channel) {
		this.server = server;
		this.port = port;
		this.botnick = botnick;
		this.login = login;
		this.channel = channel;
	}

	/** The irc server to connect to. */
	private String server;
	
	/** The port, this is most commonly 6667. */
	private String port;
	
	/** The botnick. Varies in max length between the various networks. To be safe, stay below 8 characters. */
	private String botnick;
	
	/** The login. This shows up in front of the host name when the bot has connected and serves as its "username" on IRC*/
	private String login;
	
	/** The channel The initial channel to Join. Additional channels can be joined using the JoinChannel() method */
	private String channel;
	
	/** This string contains the current line of text received from the IRC, and is the basis of all the parsing. */
	private String line;
	
	/** Variable used to store the current nickname being manipulated. */
	private String nick;
	
	/** The socket. Raw socket for connecting directly to the irc server*/
	private Socket socket;
	
	/** The reader. Buffered reads from the irc server*/
	private BufferedReader reader;
	
	/** The writer. Buffered writes to the irc server*/
	private BufferedWriter writer;
	
	/** An object of the xml reader class, used for grabbing weather data. */
	private XMLReader xml;
	
	/** An object of the google class, used for running google searches and returns the parsed result. */
	private Google google;
	
	/** An object of the TelefonKatalogen class, used for running phonebook searches and returns the parsed result. */
	private TelefonKatalogen tlf;
	
	/** A list of commands the box will accept. */
	private ArrayList<String> commands;
	
	/** A list of who get administrator rights on the bot. */
	private ArrayList<String> admins;
	
	/** An object of the DbConnection class, used for running its getPlayers() method. */
	private DbConnection players;


	/**
	 * Initialize.
	 * Instantiate all objects and set up user specific settings. Register admins and commands, connect to IRC and transfer control to the main run() loop.
	 * @throws Exception the exception
	 */
	public void initialize() throws Exception {
		System.setProperty("http.agent","Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.4.4");
		xml = new XMLReader();
		google = new Google();
		tlf = new TelefonKatalogen();
		commands = new ArrayList<String>();
		admins = new ArrayList<String>();
		players = new DbConnection("com.mysql.jdbc.Driver", "sql.alandfaraway.org", "alandsyu_live", "alandsyu_parser", "");
		admins.add("zicada");
        admins.add("b9");
		commands.add("!players");
		commands.add("!google");
		commands.add("!tlf");
		commands.add("!weather");
		commands.add("!join");
		commands.add("!part");
		try {
			connect();
			login();
			joinChannel(channel);
			run();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Connect.
	 * Set up a Socket and buffered connection
	 * @throws Exception IOException
	 */
	public void connect() throws Exception {
		try {
			socket = new Socket(server, Integer.parseInt(port));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println(e.getMessage());
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
	 * Checks if a command is valid, eg if it was registered during initialize().
	 *
	 * @param command the command
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
	 * @throws Exception IO Exception
	 */
	public void joinChannel(String channel) throws Exception {
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
	 * @throws Exception IO Exception
	 */
	public void partChannel(String channel) throws Exception {
		try {
			writer.write("PART " + channel + "\r\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Keep alive.
	 * Required to stay logged in. We have 250 seconds to reply with PONG whenever we recieve a PING from the server
	 * @throws Exception IO Exception
	 */
	public void keepAlive() throws Exception {
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
	 * Login.
	 * Register our nick and send the USER command to the server.
	 * We wait for the message of the day from the server to finish (376)
	 * @throws Exception IO Exception
	 */
	public void login() throws Exception {
		try {
			writer.write("NICK " + botnick + "\r\n");
			writer.write("USER " + login + " 8 * : Java IRC Bot Project\r\n");
			writer.flush();

			// Read lines from the server until it tells us we have connected.
			while ((line = reader.readLine()) != null) {
				if (line.indexOf("376") >= 0) {
					// We are now logged in.
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
	 * Write message.
	 * Writes a string to the current channel
	 *
	 * @param message the message to send
	 */
	public void writeMessage(String message) {
		try {
			writer.write("PRIVMSG " + getChannel() + " :" + message + "\r\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Reader.
	 * This method is used to catch actions that occur on the channels we are currently on.
	 * Here we look for commands the bot should recognize and do the actions they should perform.
	 * When ever we would look to check weather actions occur, we should use this method.
	 *
	 * @throws Exception IO Exception and Exception
	 */
	public void reader() throws Exception {

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
				writeMessage(tlf.getTlfData(getArgument()));
			}
			if (getCommand().matches("!google")) {
				writeMessage(google.search(getArgument()));
			}
			if (getCommand().matches("!weather")) {
				writeMessage(xml.parseData(getArgument()));
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Run.
	 * This is the main runloop that is passed execution from initialize() when we start the bot.
	 * keepAlive() and reader() are called here for each line the server sends us.
	 * @throws Exception IO Exception
	 */
	public void run() throws Exception {
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
