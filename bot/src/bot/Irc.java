package bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Irc {

	private String server;
	private String port;
	private String nick;
	private String login;
	private String channel;
	private String line;
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private XMLReader xml;
	private Google google;
	private TelefonKatalogen tlf;
	private ArrayList<String> commands;

	public void initialize() throws Exception {
		System.setProperty("http.agent",
				"Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.4.4");
		xml = new XMLReader();
		google = new Google();
		tlf = new TelefonKatalogen();
		commands = new ArrayList<String>();

		commands.add("!google");
		commands.add("!tlf");
		commands.add("!weather");

		connect();
		login();
		joinChan(channel);
		joinChan("#zicTest2");
		run();
	}

	public void setConfig(String server, String port, String nick,
			String login, String channel) {
		this.server = server;
		this.port = port;
		this.nick = nick;
		this.login = login;
		this.channel = channel;
	}

	public void connect() throws Exception {
		socket = new Socket(server, Integer.parseInt(port));
		writer = new BufferedWriter(new OutputStreamWriter(socket
				.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));
	}

	public void login() throws Exception {
		writer.write("NICK " + nick + "\r\n");
		writer.write("USER " + login + " 8 * : Java IRC Bot Project\r\n");
		writer.flush();

		// Read lines from the server until it tells us we have connected.
		while ((line = reader.readLine()) != null) {
			if (line.indexOf("004") >= 0) {
				// We are now logged in.
				break;
			} else if (line.indexOf("433") >= 0) {
				System.out.println("Nickname is already in use.");
				return;
			}
		}
	}

	public void joinChan(String channel) throws Exception {
		writer.write("JOIN " + channel + "\r\n");
		writer.flush();
	}

	public void keepAlive() throws Exception {
		if (line.startsWith("PING ")) {
			// We must respond to PINGs to avoid being disconnected.
			writer.write("PONG " + line.substring(5) + "\r\n");
			writer.flush();
		} else {
			// Print the raw line received by the Main.
			System.out.println(line);
		}
	}

	public void run() throws Exception {
		// this is the main run loop
		while ((line = reader.readLine()) != null) {
			keepAlive();
			reader();
		}
	}

	public Boolean isValid(String command) {
		if (commands.contains(command)) {
			return true;
		} else {
			return false;
		}
	}

	public String getChan() {
		if (line.contains("PRIVMSG #")) {
			channel = line.substring(line.indexOf("PRIVMSG") + 7,
					line.lastIndexOf(":")).trim();
		}
		return channel;
	}

	public String getCommand() {
		String[] command = new String[2];
		if (line.contains("PRIVMSG " + getChan() + " :")) {
			command = line.substring(line.lastIndexOf("!")).split(" ");
		} else {
			command[0] = "";
		}
		if (isValid(command[0])) {
			return command[0];
		} else {
			return "";
		}
	}

	public String getArgument() {
		String arg = line.substring(
				line.lastIndexOf("!") + getCommand().length()).trim();
		return arg;
	}

	public void reader() throws Exception {
		if (getCommand().matches("!tlf")) {
			writer.write("PRIVMSG " + getChan() + " :"
					+ tlf.getTlfData(getArgument()) + "\r\n");
		}
		if (getCommand().matches("!google")) {
			writer.write("PRIVMSG " + getChan() + " :"
					+ google.search(getArgument()) + "\r\n");
		}
		if (getCommand().matches("!weather")) {
			String[] result = xml.parseData(getArgument());
			writer.write("PRIVMSG " + getChan() + " :Location: " + result[0]
					+ ", Temp: " + result[1] + "C" + ", " + result[2] + " "
					+ result[3] + "\r\n");
		}
		writer.flush();
	}
}
