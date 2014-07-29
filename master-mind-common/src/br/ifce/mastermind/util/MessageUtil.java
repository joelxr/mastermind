package br.ifce.mastermind.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by jrocha on 25/07/14.
 */
public class MessageUtil {

    private static Logger logger = Logger.getLogger(MessageUtil.class.getName());

    public static String getMessage(Socket socket) throws IOException {
        String result = "";
        DataInputStream inputStream = null;

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            result = inputStream.readUTF();
            logger.info("Reading the follow message... " + result);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't get a message, input is no longer available! ", e);
            inputStream.close();
            throw e;
        }

        return result;
    }

    public static void sendMessage(Socket socket, String message) throws IOException {

        DataOutputStream outputStream = null;

        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(message);
            logger.info("Sending the follow message... " + message);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't get a message, output is no longer available! ", e);
            outputStream.close();
            throw e;
        }
    }
}
