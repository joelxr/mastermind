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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

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

    public void addMessage(MasterMindMessage message) {
        this.messages.add(message);

        if (message.getType() == ClientType.MASTER) {
            this.passwordMessage = message;
        } else {
            try {
                MessageUtil.sendMasterMindMessage(masterHandler.getSocket(), message);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Couldn't read a message from stream!", e);
            }
        }
    }

    public void checkMessage (MasterMindMessage message) {

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
                        if (j == Constants.COLORS_QUANTITY-1) { // last interaction
                            response[i] = null;
                            break;
                        }
                    }
                }
            }

            message.setResponse(response);
        }
    }

    public void setMasterHandler(AbstractMessageHandler handler) {
        this.masterHandler = handler;
    }

    public void addPlayerHandler(AbstractMessageHandler handler) {
        this.playersHandler.add(handler);
    }
}
