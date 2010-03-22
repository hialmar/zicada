package bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Main {

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

    public void initialize() throws Exception {
        connect();
        login();
        joinChan(channel);
        run();
    }

    public static void main(String[] args) throws Exception {
        // start an instance of ourselves so we're not static anymore
        Main main = new Main();
        if (args.length == 0) {
            System.err.println("Using default arguments \n " +
                    "Usage: java -jar bot servername port nickname username channel");

            main.setConfig("irc.homelien.no", "6667", "zicbot", "zicbot", "#zictest");
        } else {
            main.setConfig(args[0], args[1], args[2], args[3], "#" + args[4]);
        }
        main.initialize();
    }

    private void setConfig(String server, String port, String nick, String login, String channel) {
        this.server = server;
        this.port = port;
        this.nick = nick;
        this.login = login;
        this.channel = channel;
    }

    public void connect() throws Exception {
        socket = new Socket(server, Integer.parseInt(port));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

    public void weather() throws Exception {
        if (line.contains("PRIVMSG " + channel + " :!weather")) {
            xml = new XMLReader();
            String location = line.substring(line.indexOf("weather") + 7);
            String[] result = xml.parseData(location);
            writer.write("PRIVMSG " + channel + " :Location: " + result[0]
                    + ", Temp: " + result[1] + "C" + ", " + result[2]
                    + " " + result[3] + "\r\n");
            writer.flush();
        }
    }

    public void google() throws Exception {
        System.setProperty("http.agent", "Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.4.4");
        if (line.contains("PRIVMSG " + channel + " :!google")) {
            google = new Google();
            String query = line.substring(line.indexOf("google") + 6);
            writer.write("PRIVMSG " + channel + " :" + google.search(query) + "\r\n");
            writer.flush();
        }
    }

    public void telefonKatalogen() throws Exception {
        if (line.contains("PRIVMSG " + channel + " :!tlf")) {
            tlf = new TelefonKatalogen();
            String query = line.substring(line.indexOf("tlf") + 3);
            writer.write("PRIVMSG " + channel + " :" + tlf.getTlfData(query) + "\r\n");
            writer.flush();
        }
    }

    public void run() throws Exception {
        // this is the main run loop
        while ((line = reader.readLine()) != null) {
            keepAlive();
            weather();
            google();
            telefonKatalogen();
        }
    }
}
