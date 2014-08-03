package br.ifce.mastermind.engine;

import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.enums.ClientType;

import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by jrocha on 28/07/14.
 */
public class GameEngine {

    private static GameEngine engine;

    private Set<MasterMindMessage> messages;

    private MasterMindMessage passwordMessage;

    private GameEngine() {
        this.messages = new LinkedHashSet<MasterMindMessage>();
    }

    public static GameEngine getInstance() {
        if (engine == null)
            engine = new GameEngine();
        return engine;
    }

    public Set<MasterMindMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<MasterMindMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(MasterMindMessage message) {
        this.messages.add(message);

        if (message.getType() == ClientType.MASTER) {
            this.passwordMessage = message;
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

    public MasterMindMessage getPasswordMessage() {
        return passwordMessage;
    }

    public void setPasswordMessage(MasterMindMessage passwordMessage) {
        this.passwordMessage = passwordMessage;
    }
}
