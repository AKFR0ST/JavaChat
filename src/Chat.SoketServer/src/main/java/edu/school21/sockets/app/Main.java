package edu.school21.sockets.app;

import com.zaxxer.hikari.HikariDataSource;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import edu.school21.sockets.services.ChatroomsService;
import edu.school21.sockets.services.MessagesService;
import edu.school21.sockets.services.UsersService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringJoiner;

public class Main {
    public static void main(String[] args) {
        String prefix = "--port=";
        if (args.length != 1 || !args[0].startsWith(prefix)) {
            System.err.println("Number of port required");
            return;
        }
        int port = Integer.parseInt(args[0].substring(prefix.length()));
        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        resetDB(context);
        UsersService usersService = (UsersService) context.getBean("usersService", UsersService.class);
        ChatroomsService chatroomsService = (ChatroomsService) context.getBean("chatroomsService", ChatroomsService.class);
        MessagesService messagesService = (MessagesService) context.getBean("messagesService", MessagesService.class);
        usersService.signUp("u", "p");
        usersService.signUp("u1", "p1");
        chatroomsService.addChatroom("Room_1");
        chatroomsService.addChatroom("Room_2");
        messagesService.sendMessage(1L, 1L, "Test_message");

        Server server = context.getBean("server", Server.class);
        server.run(context, port);
    }

    private static void resetDB(ApplicationContext context) {
        DataSource dataSource = context.getBean("hikariDataSource", HikariDataSource.class);
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            StringJoiner sjUsers = new StringJoiner(" ");
            sjUsers.add("DROP TABLE IF EXISTS users;");
            sjUsers.add("CREATE TABLE IF NOT EXISTS users (");
            sjUsers.add("id BIGSERIAL PRIMARY KEY,");
            sjUsers.add("username VARCHAR(255) NOT NULL,");
            sjUsers.add("password VARCHAR(255) NOT NULL);");
            statement.executeUpdate(sjUsers.toString());

            StringJoiner sjMessages = new StringJoiner(" ");
            sjMessages.add("DROP TABLE IF EXISTS messages;");
            sjMessages.add("CREATE TABLE IF NOT EXISTS messages (");
            sjMessages.add("id BIGSERIAL PRIMARY KEY,");
            sjMessages.add("sender BIGSERIAL NOT NULL,");
            sjMessages.add("message VARCHAR(255) NOT NULL,");
            sjMessages.add("timestamp TIMESTAMP NOT NULL,");
            sjMessages.add("chatid BIGSERIAL NOT NULL);");
            statement.executeUpdate(sjMessages.toString());

            StringJoiner sjChatRooms = new StringJoiner(" ");
            sjChatRooms.add("DROP TABLE IF EXISTS chatrooms;");
            sjChatRooms.add("CREATE TABLE IF NOT EXISTS chatrooms (");
            sjChatRooms.add("id BIGSERIAL PRIMARY KEY,");
            sjChatRooms.add("chatname VARCHAR(255) NOT NULL);");
            statement.executeUpdate(sjChatRooms.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


