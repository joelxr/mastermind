package br.ifce.mastermind.handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by jrocha on 25/07/14.
 */
public class KeywordHandler implements Runnable {

    private Socket socket;

    public KeywordHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        DataOutputStream outputStream = null;

        try {
            outputStream = new DataOutputStream(this.socket.getOutputStream());
            Scanner scan = new Scanner(System.in);

            while (scan.hasNextLine()) {
                outputStream.writeUTF(scan.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
