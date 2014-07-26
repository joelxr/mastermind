package br.ifce.mastermind.handlers;

import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.util.MessageUtil;

import java.io.IOException;
import java.net.Socket;

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
            MessageUtil.sendMessage(getSocket(), "Ok, welcome to the game! Try to guess the password: ");

            while (true) {

                MessageUtil.getMessage(getSocket());
                this.setBusy(false);

            }
        } catch (IOException e) {
            getLogger().error(Thread.currentThread().getName() + " is aborting the game...");
            Thread.currentThread().interrupt();
        }
    }
}
