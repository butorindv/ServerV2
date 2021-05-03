package ru.butorin;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
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
            while (true) {
                //читаем сообщение от клиента и сохраняем в переменной
                JSONObject clientMassageJSON = new JSONObject(reader.readLine());
                //Выходим из цикла, если в сообщении присутствует \exit.
                if (clientMassageJSON.getJSONObject("Request").getJSONObject("Massage").get("Body").equals("\\exit")) {
                    System.out.println("Клиент " + messageFromTheClient(clientMassageJSON) + " завершил сеанс.\n--------------------------------------");
                    log.info("Клиент " + messageFromTheClient(clientMassageJSON) + " завершил сеанс.");
                    break;
                }
                //Выводим в консоль, что приняли
                System.out.println(outMassageInConsole(clientMassageJSON));
                log.info(outMassageInLogFile(clientMassageJSON));
                //JSON ответ
                writer.write(responseForClient(clientMassageJSON) + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Что-то пошло не так! Смотрите лог-файл!");
            log.info(e.getMessage());
            System.exit(0);
        }
    }
    public String messageFromTheClient(JSONObject jsonObject) {
        return jsonObject.getJSONObject("Request")
                .getJSONObject("User").get("Name") + " " + jsonObject.getJSONObject("Request")
                .getJSONObject("User").get("SecondName");
    }

    public JSONObject responseForClient(JSONObject jsonObject) {
        return new JSONObject().put("Response", new JSONObject()
                .put("Massage", new JSONObject()
                        .put("Body", "Добрый день, " + messageFromTheClient(jsonObject) + ", Ваше сообщение успешно обработано!")
                        .put("Timestamp", Parameters.getFormatForDateNow().format(new Date()))));
    }

    public String outMassageInConsole(JSONObject jsonObject) {
        return "Принято сообщение от " + messageFromTheClient(jsonObject) + ": " + jsonObject.getJSONObject("Request")
                .getJSONObject("Massage").get("Body") + "\nотправлено: "
                + jsonObject.getJSONObject("Request").getJSONObject("Massage").get("Timestamp") +
                "\n--------------------------------------";
    }

    public String outMassageInLogFile(JSONObject jsonObject) {
        return "Принято сообщение от " + messageFromTheClient(jsonObject) +
                ": " + jsonObject.getJSONObject("Request").getJSONObject("Massage").get("Body");
    }
}




