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
        String channel = "#zictest";

        // Connect directly to the IRC server over socket, using buffered reads
        // and writes.
        Socket socket = new Socket(server, port);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

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
                writer.flush();
            } else {
                // Print the raw line received by the Main.
                System.out.println(line);
            }
            if (line.contains("PRIVMSG " + channel + " :!weather")) {
                XMLReader xml = new XMLReader();
                String location = line.substring(line.indexOf("weather") + 8).replaceAll(" ", "%20");
                String[] result = xml.parseData(location);
                writer.write("PRIVMSG " + channel + " :Location: " + result[0]
                        + ", Temp: " + result[1] + "C" + ", " + result[2]
                        + " " + result[3] + "\r\n");
                writer.flush();
            }
            if (line.contains("PRIVMSG " + channel + " :!google")) {
                Google google = new Google();
                String query = line.substring(line.indexOf("google") + 7);
                writer.write("PRIVMSG " + channel + " :" + google.search(query) + "\r\n");
                writer.flush();
            }
                if (line.contains("PRIVMSG " + channel + " :!tlf")) {
                TelefonKatalogen tlf = new TelefonKatalogen();
                String query = line.substring(line.indexOf("tlf") + 4);
                writer.write("PRIVMSG " + channel + " :" + tlf.getNumber(query) + "\r\n");
                writer.flush();
            }
                if (line.contains("PRIVMSG " + channel + " :!addr")) {
                TelefonKatalogen tlf = new TelefonKatalogen();
                String query = line.substring(line.indexOf("addr") + 5);
                writer.write("PRIVMSG " + channel + " :" + tlf.getAddress(query) + "\r\n");
                writer.flush();
            }
        }
    }
}
