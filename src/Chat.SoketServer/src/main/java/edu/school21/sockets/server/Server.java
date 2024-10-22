package edu.school21.sockets.server;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Connection;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.ChatroomsService;
import edu.school21.sockets.services.MessagesService;
import edu.school21.sockets.services.UsersService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component("server")
public class Server {
    private final UsersService usersService;
    private final MessagesService messagesService;
    private final ChatroomsService chatroomsService;

    private static ServerSocket server; // серверсокет
    private final PasswordEncoder encoder;
    private List<Connection> connections;


    public Server(@Qualifier("usersService") UsersService usersService, @Qualifier("messagesService") MessagesService messagesService, @Qualifier("chatroomsService") ChatroomsService chatroomsService, PasswordEncoder encoder) {
        this.usersService = usersService;
        this.messagesService = messagesService;
        this.chatroomsService = chatroomsService;
        this.encoder = encoder;
        connections = new ArrayList<>();
    }

    public void run(ApplicationContext context, int port) {
        System.out.println("Starting server...");
        try {
            server = new ServerSocket(port);
            System.out.println("Waiting for client on port " + port + "...");
            while (true) {
                Socket clientSocket = server.accept();
                Thread thread = new ClientSocketThread(context, clientSocket);
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("Server start failed");
            e.printStackTrace();
        }
    }


    private class ClientSocketThread extends Thread {
        final private ApplicationContext context;
        final private Socket clientSocket;

        public ClientSocketThread(ApplicationContext context, Socket clientSocket) {
            this.context = context;
            this.clientSocket = clientSocket;
        }


        public void run() {
            try {
                String clientCommand = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                out.write(jsonConverter("Hello!", "server>", "true") + "\n");
                out.flush();
                printMenu("main", out);
                while (!clientCommand.equals("3")) {
                    out.write(jsonConverter("Enter command:", "server>", "true") + "\n");
                    out.flush();

                    clientCommand = getJSONMessage(in.readLine());

                    if (clientCommand.equals("1")) {
                        if (signUp(context, clientSocket)) {
                            out.write(jsonConverter("signUp successful", "server>", "true") + "\n");
                            out.flush();
                        } else {
                            out.write(jsonConverter("signUp not successful", "server>", "true") + "\n");
                            out.flush();
                        }
                        printMenu("main", out);
                    }
                    if (clientCommand.equals("2")) {
                        User user = signIn(context, clientSocket);
                        if (user != null) {
                            out.write(jsonConverter("signIn successful", "server>", "true") + "\n");
                            out.flush();
                            System.out.println(user.getName() + " connected");
                            chatroomMenu(context, user, in, out);
                        } else {
                            out.write(jsonConverter("signIn not successful", "server>", "true") + "\n");
                            out.flush();
                        }
                        printMenu("main", out);
                    }
                }
                out.write(jsonConverter("Session over!", "server>", "true") + "\n");
                out.flush();
                clientSocket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void chatroomMenu(ApplicationContext context, User user, BufferedReader in, BufferedWriter out) throws IOException {
        printMenu("chatrooms", out);
        String clientCommand = "";
        while (!(clientCommand = getJSONMessage(in.readLine())).equals("3")) {

            if (clientCommand.equals("1")) {
                out.write(jsonConverter("Enter name of room:", "server>", "true") + "\n");
                out.flush();
                if (chatroomsService.addChatroom(getJSONMessage(in.readLine()))) {
                    out.write(jsonConverter("Room created", "server>", "true") + "\n");
                    out.flush();
                } else {
                    out.write(jsonConverter("Room not created", "server>", "true") + "\n");
                    out.flush();
                }
            }
            if (clientCommand.equals("2")) {
                int exitPoint = 0;
                while (!clientCommand.equals(String.valueOf(exitPoint))) {
                    List<Chatroom> chatrooms = chatroomsService.getChatrooms();
                    out.write(jsonConverter("Rooms:", "server>", "true") + "\n");
                    out.flush();
                    for (Chatroom chatroom : chatrooms) {
                        out.write(jsonConverter(chatroom.getId() + ". " + chatroom.getName(), "server>", "true") + "\n");
                        out.flush();
                    }
                    exitPoint = chatrooms.size() + 1;
                    out.write(jsonConverter(exitPoint + ". Exit", "server>", "true") + "\n");
                    out.flush();
                    clientCommand = getJSONMessage(in.readLine());
                    if (!clientCommand.equals(String.valueOf(exitPoint))) {
                        connections.add(new edu.school21.sockets.models.Connection(user.getId(), Long.parseLong(clientCommand), out));
                        startChat(context, user, in, out, Long.parseLong(clientCommand));
                    }
                }
            }
            printMenu("chatrooms", out);
        }
    }


    private void printMenu(String type, BufferedWriter out) throws IOException {
        if (type.equals("main")) {
            out.write(jsonConverter("1. SignUp", "server>", "true") + "\n");
            out.write(jsonConverter("2. SignIn", "server>", "true") + "\n");
            out.write(jsonConverter("3. Exit", "server>", "true") + "\n");
            out.flush();
        }
        if (type.equals("chatrooms")) {
            out.write(jsonConverter("1. Create room", "server>", "true") + "\n");
            out.write(jsonConverter("2. Choose room", "server>", "true") + "\n");
            out.write(jsonConverter("3. Exit", "server>", "true") + "\n");
            out.flush();
        }
    }

    private JSONObject jsonConverter(String str, String username, String service) {
        JSONObject j = new JSONObject();
        j.put("service", service);
        j.put("chatid", "1");
        j.put("sender", username);
        j.put("message", str);
        return j;
    }

    private String getJSONMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject) new JSONParser().parse(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (String) jsonObject.get("message");

    }

    private void SendLastMessages(User user, BufferedWriter out, List<Message> lastMessages) throws IOException {
        boolean needSend = false;
        if (!lastMessages.isEmpty()) {
            for (Message message : lastMessages) {
                if (message.getSender().equals(user.getId())) {
                    needSend = true;
                }
            }
        }
        if (needSend) {
            for (Message message : lastMessages) {
                User currentSender = usersService.findUserById(message.getSender());
                out.write(jsonConverter(message.getMessage(), currentSender.getName(), "false") + "\n");
                out.flush();
            }
        }
    }

    private void startChat(ApplicationContext context, User user, BufferedReader in, BufferedWriter out, Long chatId) throws IOException {
        String incomingMessage = "";
        String outgoingMessage = "";
        out.write(jsonConverter("Welcome, " + user.getName(), "server>", "true") + "\n");
        out.flush();
        List<Message> lastMessages = messagesService.getLastMessages(chatId);
        SendLastMessages(user, out, lastMessages);


        while (!incomingMessage.equals("exit")) {
            if (in.ready()) {
                incomingMessage = getJSONMessage(in.readLine());
                messagesService.sendMessage(user.getId(), chatId, incomingMessage);
                System.out.println(user.getName() + " send: " + incomingMessage);
                for (Connection connection : connections) {
                    if (chatId.equals(connection.getChatId()) && (connection.getUserId() != user.getId())) {
                        connection.getOut().write(jsonConverter(incomingMessage, user.getName(), "false") + "\n");
                        connection.getOut().flush();
                        System.out.println(user.getName() + " sending:" + incomingMessage);
                    }
                }
            }
        }
        out.write(jsonConverter("Session over, " + user.getName(), "server>", "true") + "\n");
        out.flush();
        connections.removeIf(connection -> connection.getUserId() == user.getId());
        System.out.println("User " + user.getName() + " disconnected");
    }

    private boolean signUp(ApplicationContext context, Socket clientSocket) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        boolean result = false;
        out.write(jsonConverter("Enter username:", "server>", "true") + "\n");
        out.flush();
        String username = in.readLine();
        out.write(jsonConverter("Enter password:", "server>", "true") + "\n");
        out.flush();
        String password = in.readLine();
        try {
            usersService.signUp(getJSONMessage(username), getJSONMessage(password));
            result = true;
            System.out.println(username + " signed up successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private User signIn(ApplicationContext context, Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(jsonConverter("Enter username:", "server>", "true") + "\n");
        out.flush();
        String username = in.readLine();
        out.write(jsonConverter("Enter password:", "server>", "true") + "\n");
        out.flush();
        String password = in.readLine();
        return usersService.signIn(getJSONMessage(username), getJSONMessage(password));
    }
}
