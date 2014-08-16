package br.ifce.mastermind.handlers;

import br.ifce.mastermind.engine.GameEngine;
import br.ifce.mastermind.message.AbstractMessage;
import br.ifce.mastermind.message.ChatMessage;
import br.ifce.mastermind.message.ControlMessage;
import br.ifce.mastermind.message.MasterMindMessage;
import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.util.MessageUtil;

import javax.naming.ldap.Control;
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

            ControlMessage ackMsg = new ControlMessage();
            ackMsg.setMessage(Thread.currentThread().getName());
            ackMsg.setClientType(getType());
            ackMsg.setClientName(Thread.currentThread().getName());

            MessageUtil.sendMasterMindMessage(getSocket(), ackMsg);

            AbstractMessage message;

            while (true) {
                message = MessageUtil.getMasterMindMessage(getSocket());
                message.setClientType(getType());

                if (message instanceof MasterMindMessage) {
                    MasterMindMessage masterMindMessage = (MasterMindMessage) message;
                    GameEngine.getInstance().addMessage(masterMindMessage);
                    MessageUtil.sendMasterMindMessage(getSocket(), masterMindMessage);
                    getLogger().info("Adding the follow message....  " + masterMindMessage);
                } else if (message instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) message;
                    GameEngine.getInstance().sendChatMessage(chatMessage);
                } else if (message instanceof ControlMessage) {
                    ControlMessage controlMessage = (ControlMessage) message;
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
