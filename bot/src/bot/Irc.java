package bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

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
	private Google google;
	private TelefonKatalogen tlf;
	private ArrayList<String> commands;
	private ArrayList<String> admins;

	public void initialize() throws Exception {
		System.setProperty("http.agent","Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.4.4");
		xml = new XMLReader();
		google = new Google();
		tlf = new TelefonKatalogen();
		commands = new ArrayList<String>();
		admins = new ArrayList<String>();
		admins.add("zicada");
		commands.add("!test");
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
	
	public void connect() throws Exception {
		try {
			socket = new Socket(server, Integer.parseInt(port));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public String getArgument() {
		String arg = line.substring(
				line.lastIndexOf("!") + getCommand().length()).trim();
		return arg;
	}

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
	
	public String getNick() {
		if (line.contains("PRIVMSG #") || line.contains("JOIN :#") || line.contains("PART #")) {
			nick = line.substring(line.indexOf(":")+1,line.indexOf("!")).trim();
		}
		return nick;
	}
	
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

	public Boolean isValidCommand(String command) {
		if (commands.contains(command)) {
			return true;
		} else {
			return false;
		}
	}

	public void joinChannel(String channel) throws Exception {
		try {
			writer.write("JOIN " + channel + "\r\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	
	public void partChannel(String channel) throws Exception {
		try {
			writer.write("PART " + channel + "\r\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public void keepAlive() throws Exception {
		try {
			if (line.startsWith("PING ")) {
				// We must respond to PINGs to avoid being disconnected.
				writer.write("PONG " + line.substring(5) + "\r\n");
				writer.flush();
			} else {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

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
	
	public void writeMessage(String message) throws Exception {
		writer.write("PRIVMSG " + getChannel() + " :" + message + "\r\n");
		writer.flush();
	}

	public void reader() throws Exception {

		try {

			if (line.contains("JOIN :#") && !getNick().matches(botnick)){
				writeMessage(getNick() + " came along");
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

	public void setConfig(String server, String port, String nick,
			String login, String channel) {
		this.server = server;
		this.port = port;
		this.botnick = nick;
		this.login = login;
		this.channel = channel;
	}
}
