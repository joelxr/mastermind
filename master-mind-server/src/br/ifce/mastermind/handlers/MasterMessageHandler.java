package br.ifce.mastermind.handlers;

import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.util.MessageUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jrocha on 25/07/14.
 */
public class MasterMessageHandler extends AbstractMessageHandler {

    public MasterMessageHandler(Socket socket) {
        super(ClientType.MASTER, socket);
    }

    @Override
    public void process() {

        try {
            MessageUtil.sendMessage(getSocket(), "Congrats! You are the master! Choose the password: ");

            while (true) {

                MessageUtil.getMessage(getSocket());
                this.setBusy(false);

            }
        } catch (IOException e) {
            getLogger().error(Thread.currentThread().getName() + " is aborting the game....");
            Thread.currentThread().interrupt();
            this.setBusy(false);
        }
    }
}
