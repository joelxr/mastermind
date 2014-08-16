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
public class MasterMessageHandler extends AbstractMessageHandler {

    public MasterMessageHandler(Socket socket) {
        super(ClientType.MASTER, socket);
    }

    @Override
    public void process() {

        try {
            MessageUtil.sendMessage(getSocket(), Thread.currentThread().getName());

            MasterMindMessage message;

            while (true) {
                message = MessageUtil.getMasterMindMessage(getSocket());
                message.setType(getType());

                if (message.getColors() != null) {
                    GameEngine.getInstance().addMessage(message);
                    MessageUtil.sendMasterMindMessage(getSocket(), message);
                    getLogger().info("Adding the follow message....  " + message);
                } else {
                    GameEngine.getInstance().sendChatMessage(message);
                }

                this.setBusy(false);
            }

        } catch (IOException e) {
            getLogger().log(Level.SEVERE, Thread.currentThread().getName() + " is aborting the game....");
            Thread.currentThread().interrupt();
            this.setBusy(false);
        } catch (ClassNotFoundException e) {
            getLogger().log(Level.SEVERE, Thread.currentThread().getName() + " is aborting the game....");
            Thread.currentThread().interrupt();
            this.setBusy(false);
        }
    }
}
