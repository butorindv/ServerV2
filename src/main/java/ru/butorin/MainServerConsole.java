package ru.butorin;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServerConsole {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Parameters parameters = context.getBean("parameters", Parameters.class);

        try {
            ServerSocket serverSocket = new ServerSocket(parameters.getPortParam());
            System.out.println("Сервер работает! Порт: " + parameters.getPortParam());
            Socket socket = serverSocket.accept();
            Server server = context.getBean("server", Server.class);
            server.seance(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
