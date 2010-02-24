package bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Main {

	public static void main(String[] args) throws Exception {

		// The server to connect to and our details.
		String server = "irc.homelien.no";
		int port = 6667;
		String nick = "zicbot";
		String login = "zicbot";

		// The channel which the Main will join.
		String channel = "#zictest";

		// Connect directly to the IRC server over socket, using buffered reads
		// and writes.
		Socket socket = new Socket(server, port);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));

		// Log on to the server.
		writer.write("NICK " + nick + "\r\n");
		writer.write("USER " + login + " 8 * : Java IRC Bot Project\r\n");
		writer.flush();

		// Read lines from the server until it tells us we have connected.
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.indexOf("004") >= 0) {
				// We are now logged in.
				break;
			} else if (line.indexOf("433") >= 0) {
				System.out.println("Nickname is already in use.");
				return;
			}
		}

		// Join the channel.
		writer.write("JOIN " + channel + "\r\n");
		writer.flush();

		// Keep reading lines from the server.
		// We should call all functionality from this loop.
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("PING ")) {
				// We must respond to PINGs to avoid being disconnected.
				writer.write("PONG " + line.substring(5) + "\r\n");
				// writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
				writer.flush();
			} else {
				// Print the raw line received by the Main.
				System.out.println(line);
			}
			// Close the socket and break out of the loop if told so
			// probably might want to comment this out in production ;)
			//if (line.contains("PRIVMSG " + channel + " :!die")) { 
			//	socket.close();
			//	break;
			//}
			if (line.contains("PRIVMSG " + channel + " :!weather")) {
				XMLReader xml = new XMLReader();
				String parm = line.substring(line.indexOf("weather") + 8)
						.replaceAll(" ", "%20");
				String[] result = xml.parseData(parm);
				writer.write("PRIVMSG " + channel + " :Location: " + result[0]
						+ ", Temp: " + result[1] + "C" + ", " + result[2]
						+ "\r\n");
				writer.flush();
			}
		}
	}

}
