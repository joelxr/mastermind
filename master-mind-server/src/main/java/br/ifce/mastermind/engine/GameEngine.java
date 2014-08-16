package br.ifce.mastermind.engine;

import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.handlers.AbstractMessageHandler;
import br.ifce.mastermind.util.MessageUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jrocha on 28/07/14.
 */
public class GameEngine {

    private static GameEngine engine;
    private static Logger logger = Logger.getLogger(GameEngine.class.getName());

    private Set<MasterMindMessage> messages;
    private MasterMindMessage passwordMessage;
    private AbstractMessageHandler masterHandler;
    private List<AbstractMessageHandler> playersHandler;

    private GameEngine() {
        this.messages = new LinkedHashSet<MasterMindMessage>();
        this.playersHandler = new ArrayList<AbstractMessageHandler>();
    }

    public static GameEngine getInstance() {
        if (engine == null)
            engine = new GameEngine();
        return engine;
    }

    public void setMasterHandler(AbstractMessageHandler handler) {
        this.masterHandler = handler;
    }

    public void addPlayerHandler(AbstractMessageHandler handler) {
        this.playersHandler.add(handler);
    }

    public void addMessage(MasterMindMessage message) {
        this.messages.add(message);

        if (message.getType() == ClientType.MASTER) {
            this.passwordMessage = message;
            this.notifyAllPassword(message);
        } else {
            try {
                MessageUtil.sendMasterMindMessage(masterHandler.getSocket(), message);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Couldn't read a message from stream!", e);
            }
        }
    }

    public void checkMessage(MasterMindMessage message) {

        if (this.passwordMessage != null) {
            Color[] passwordColors = passwordMessage.getColors();
            Color[] messageColors = message.getColors();
            Color[] response = new Color[Constants.COLORS_QUANTITY];

            for (int i = 0; i < Constants.COLORS_QUANTITY; i++) {
                for (int j = 0; j < Constants.COLORS_QUANTITY; j++) {
                    if (messageColors[i].equals(passwordColors[j])) {
                        if (i == j) {
                            response[i] = Color.BLACK;
                            break;
                        } else {
                            response[i] = Color.WHITE;
                            break;
                        }
                    } else {
                        if (j == Constants.COLORS_QUANTITY - 1) { // last interaction
                            response[i] = null;
                            break;
                        }
                    }
                }
            }

            message.setResponse(response);

            if (isWinner(message.getResponse())) {
                notifyAllWinner(message);
            }
        }
    }

    private boolean isWinner(Color[] response) {

        boolean result = false;

        if (response != null && response.length > 0) {

            result = true;

            for (int i = 0; i < Constants.COLORS_QUANTITY; i++) {
                if (response[i] != null) {
                    if (!response[i].equals(Color.BLACK)) {
                        result = false;
                    }
                }
            }
        }

        return result;

    }

    public void notifyAllWinner(MasterMindMessage messageWon) {

        MasterMindMessage message = new MasterMindMessage();
        message.setClientName(messageWon.getClientName());
        message.setType(messageWon.getType());
        message.setColors(messageWon.getColors());
        message.setSequence(messageWon.getSequence());
        message.setResponse(messageWon.getResponse());
        message.setRaw(Constants.WINNER);

        try {

            MessageUtil.sendMasterMindMessage(masterHandler.getSocket(), message);

            for (int i = 0; i < this.playersHandler.size(); i++) {
                MessageUtil.sendMasterMindMessage(playersHandler.get(i).getSocket(), message);
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't notify all player about the winner!", e);
        }
    }

    public void notifyAllPassword(MasterMindMessage message) {

        try {
            for (int i = 0; i < this.playersHandler.size(); i++) {
                MessageUtil.sendMasterMindMessage(playersHandler.get(i).getSocket(), message);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't notify all player about the winner!", e);
        }
    }

    public boolean hasPassword() {
        return this.passwordMessage != null;
    }

    public MasterMindMessage getPasswordMessage() {
        return this.passwordMessage;
    }

    public void sendChatMessage(MasterMindMessage message) {
        try {
            MessageUtil.sendMasterMindMessage(masterHandler.getSocket(), message);

            for (int i = 0; i < this.playersHandler.size(); i++) {
                MessageUtil.sendMasterMindMessage(playersHandler.get(i).getSocket(), message);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't notify all player about the winner!", e);
        }
    }
}
