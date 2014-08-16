package br.ifce.mastermind.handlers;

import br.ifce.mastermind.engine.GameEngine;
import br.ifce.mastermind.message.AbstractMessage;
import br.ifce.mastermind.message.ControlMessage;
import br.ifce.mastermind.message.MasterMindMessage;
import br.ifce.mastermind.message.ChatMessage;
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

            ControlMessage ackMsg = new ControlMessage();
            ackMsg.setMessage(Thread.currentThread().getName());
            ackMsg.setClientType(getType());
            ackMsg.setClientName(Thread.currentThread().getName());

            MessageUtil.sendMasterMindMessage(getSocket(), ackMsg);

            if (GameEngine.getInstance().hasPassword()) {
                MessageUtil.sendMasterMindMessage(getSocket(), GameEngine.getInstance().getPasswordMessage());
            }

            AbstractMessage message;

            while (true) {

                message = MessageUtil.getMasterMindMessage(getSocket());
                message.setClientType(getType());

                if (message instanceof MasterMindMessage) {
                    MasterMindMessage masterMindMessage = (MasterMindMessage) message;

                    if (GameEngine.getInstance().hasPassword()) {
                        getLogger().info("Adding the follow message....  " + masterMindMessage);
                        GameEngine.getInstance().addMessage(masterMindMessage);
                        GameEngine.getInstance().checkMessage(masterMindMessage);
                        MessageUtil.sendMasterMindMessage(getSocket(), masterMindMessage);
                    } else {
                        getLogger().info("Master Player must set the password first!");
                    }

                } else if (message instanceof ControlMessage) {

                } else if (message instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) message;
                    GameEngine.getInstance().sendChatMessage(chatMessage);
                } else {
                    getLogger().log(Level.SEVERE, "Cannot understand the message!");
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
