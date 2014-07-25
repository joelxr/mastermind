package br.ifce.mastermind.client;

import br.ifce.mastermind.constants.NetConstants;
import br.ifce.mastermind.handler.MessageHandler;
import br.ifce.mastermind.util.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by jrocha on 24/07/14.
 */
public class Client {

    public static void main(String args[]) {
        Socket clientSocket = null;

        try {

            clientSocket = new Socket(NetConstants.LOCALHOST, NetConstants.SERVER_PORT);

            String text = Util.readKeyboard();
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            outputStream.writeUTF(text);

            MessageHandler handler = new MessageHandler(clientSocket);
            Thread t = new Thread(handler);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
