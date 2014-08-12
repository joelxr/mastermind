package br.ifce.mastermind.handlers;

import br.ifce.mastermind.engine.GameEngine;
import br.ifce.mastermind.entities.MasterMindMessage;
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
            MessageUtil.sendMasterMindMessage(getSocket(), GameEngine.getInstance().getPasswordMessage());

            MasterMindMessage message;

            while (true) {
                message = MessageUtil.getMasterMindMessage(getSocket());
                message.setType(getType());

                if (GameEngine.getInstance().hasPassword()) {

                    getLogger().info("Adding the follow message....  " + message);
                    GameEngine.getInstance().addMessage(message);
                    GameEngine.getInstance().checkMessage(message);

                    MessageUtil.sendMasterMindMessage(getSocket(), message);
                } else {
                    getLogger().info("Master Player must set the password first!");
                }
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, Thread.currentThread().getName() + " is aborting the game...");
            Thread.currentThread().interrupt();
        } catch (ClassNotFoundException e) {
            getLogger().log(Level.SEVERE, Thread.currentThread().getName() + " is aborting the game...");
            Thread.currentThread().interrupt();
        }
    }
}
