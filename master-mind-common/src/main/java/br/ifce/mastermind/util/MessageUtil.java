package br.ifce.mastermind.util;

import br.ifce.mastermind.message.AbstractMessage;
import br.ifce.mastermind.message.MasterMindMessage;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by jrocha on 25/07/14.
 */
public class MessageUtil {

    private static Logger logger = Logger.getLogger(MessageUtil.class.getName());

    public static void sendMasterMindMessage(Socket socket, AbstractMessage message) throws IOException {

        ObjectOutputStream outputStream = null;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(message);
            logger.info("Sending the follow message... " + message);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't get a message, output is no longer available! ", e);
            if (outputStream != null)
                outputStream.close();
            throw e;
        }
    }

    public static AbstractMessage getMasterMindMessage(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = null;
        AbstractMessage message;

        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            message = (AbstractMessage) inputStream.readObject();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't get a message, input is no longer available! ", e);
            if (inputStream != null) inputStream.close();
            throw e;
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Couldn't get a message, message has another format! ", e);
            if (inputStream != null) inputStream.close();
            throw e;
        }

        return message;
    }
}
