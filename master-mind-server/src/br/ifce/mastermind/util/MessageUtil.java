package br.ifce.mastermind.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



/**
 * Created by jrocha on 25/07/14.
 */
public class MessageUtil {

    private static Logger logger = LogManager.getLogger(MessageUtil.class.getName());

    public static String getMessage(Socket socket) {
        String result = "";
        DataInputStream inputStream = null;

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            result = inputStream.readUTF();
            logger.trace("Reading the follow message... " + result);
        } catch (IOException e) {
            logger.error(e);
        }

        return result;
    }

    public static void sendMessage(Socket socket, String message) {

        DataOutputStream outputStream = null;

        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(message);
            logger.trace("Sending the follow message... " + message);
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
