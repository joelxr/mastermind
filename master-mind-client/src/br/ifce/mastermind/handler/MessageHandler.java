package br.ifce.mastermind.handler;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by jrocha on 25/07/14.
 */
public class MessageHandler implements Runnable{

    private Socket socket;

    public MessageHandler (Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        DataInputStream inputStream = null;

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
