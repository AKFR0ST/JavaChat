package edu.school21.sockets.client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Component("client")
public class Client {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static BufferedReader reader;
    int port = 0;

    public Client() {

    }

    private JSONObject jsonConverter(String str, String username) {
        JSONObject j = new JSONObject();
        j.put("chatid", "1");
        j.put("sender", username);
        j.put("message", str);
        return j;
    }

    public void run(int port) {
        try {
            System.out.println("Connecting to server with port " + port);
            clientSocket = new Socket("localhost", port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connection Successful");
            String serverWord = "";
            String clientWord = "";

            while (!serverWord.equals("{\"chatid\":\"1\",\"sender\":\"server>\",\"service\":\"true\",\"message\":\"Session over!\"}")) {
                if (in.ready()) {
                    serverWord = in.readLine();
                    Object o = new JSONParser().parse(serverWord);
                    JSONObject j = (JSONObject) o;
                    if (j.get("service").equals("true")) {
                        System.out.println(j.get("message"));
                    } else {
                        System.out.println(j.get("sender") + ": " + j.get("message"));
                    }
                }
                if (reader.ready()) {
                    clientWord = reader.readLine();
                    out.write(jsonConverter(clientWord, "Sender") + "\n");
                    out.flush();
                }

            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
