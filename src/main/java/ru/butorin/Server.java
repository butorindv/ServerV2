package ru.butorin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

@Component
public class Server {
    private static final Logger log = Logger.getLogger(Server.class);

    public void seance(Socket socket) {


        try {
            System.out.println("Клиент " + socket.getInetAddress().getHostAddress() + " подключился.");
            log.info("Клиент " + socket.getInetAddress().getHostAddress() + " подключился.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS request");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS request (name VARCHAR NOT NULL, " +
                    "secondName VARCHAR NOT NULL, body VARCHAR, timestamp VARCHAR);");
            while (true) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);      //!!!Включить сериализацию Рут поля
                objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);  //!!!Отключить Рут поле при десериализации
                ClientJson clientJson = objectMapper.readValue(reader.readLine(), ClientJson.class);

                statement.executeUpdate("INSERT INTO request (name, secondName, body, timestamp) VALUES " +
                        "('" + clientJson.getClient().getName() + "', '" + clientJson.getClient().getSecondName() + "'," +
                        " '" + clientJson.getMessage().getBody() + "', '" + clientJson.getMessage().getTimestamp() + "');");
                //Выходим из цикла, если в сообщении присутствует \exit.
                if (clientJson.getMessage().getBody().equals("\\exit")) {
                    System.out.println("Клиент " + messageFromTheClient(clientJson) + " завершил сеанс.\n--------------------------------------");
                    log.info("Клиент " + messageFromTheClient(clientJson) + " завершил сеанс.");
                    connection.close();
                    statement.close();
                    break;
                }
                //Выводим в консоль, что приняли
                System.out.println(outMessageInConsole(clientJson));
                log.info(outMessageInLogFile(clientJson));
                //JSON ответ
                writer.write(responseForClient(clientJson) + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Что-то пошло не так! Смотрите лог-файл!");
            log.info(e.getMessage());
            System.exit(0);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String messageFromTheClient(ClientJson clientJson) {
        return clientJson.getClient().getName() + " " + clientJson.getClient().getSecondName();
    }

    public JSONObject responseForClient(ClientJson clientJson) {
        return new JSONObject().put("Response", new JSONObject()
                .put("Message", new JSONObject()
                        .put("Body", "Добрый день, " + messageFromTheClient(clientJson) + ", Ваше сообщение успешно обработано!")
                        .put("Timestamp", Parameters.getFormatForDateNow().format(new Date()))));
    }

    public String outMessageInConsole(ClientJson clientJson) {
        return "Принято сообщение от " + messageFromTheClient(clientJson) + ": " + clientJson.getMessage().getBody() + "\nотправлено: "
                + clientJson.getMessage().getTimestamp() +
                "\n--------------------------------------";
    }

    public String outMessageInLogFile(ClientJson clientJson) {
        return "Принято сообщение от " + messageFromTheClient(clientJson) +
                ": " + clientJson.getMessage().getBody();
    }
}




