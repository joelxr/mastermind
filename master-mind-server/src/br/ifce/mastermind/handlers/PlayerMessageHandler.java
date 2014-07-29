package br.ifce.mastermind.handlers;

import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.util.MessageUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by jrocha on 25/07/14.
 */
public class PlayerMessageHandler extends AbstractMessageHandler {

    public PlayerMessageHandler(Socket socket) {
        super(ClientType.PLAYER, socket);
    }

    @Override
    public void process() {

        try {
            MessageUtil.sendMessage(getSocket(), Thread.currentThread().getName());

            while (true) {
                MessageUtil.getMessage(getSocket());
                this.setBusy(false);
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, Thread.currentThread().getName() + " is aborting the game...");
            Thread.currentThread().interrupt();
        }
    }
}
