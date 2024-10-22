package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.school21.sockets.client.Client;

public class Main {
    public static void main(String[] args) {
        String prefix = "--server-port=";
        if (args.length != 1 || !args[0].startsWith(prefix)) {
            System.err.println("Number of server port required");
            return;
        }
        int port = Integer.parseInt(args[0].substring(prefix.length()));
        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        Client client = context.getBean("client", Client.class);
        client.run(port);
    }
}