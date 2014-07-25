package br.ifce.mastermind.factory;

import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.handlers.AbstractMessageHandler;
import br.ifce.mastermind.handlers.MasterMessageHandler;
import br.ifce.mastermind.handlers.PlayerMessageHandler;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import javax.xml.ws.handler.HandlerResolver;
import java.net.Socket;

/**
 * Created by jrocha on 25/07/14.
 */
public class MessageHandlerFactory {


    public static AbstractMessageHandler build(Socket clientSocket, ClientType type) {

        AbstractMessageHandler handler = null;

        switch (type) {
            case PLAYER:
                handler = new PlayerMessageHandler(clientSocket);
                break;
            case MASTER:
                handler = new MasterMessageHandler(clientSocket);
                break;
            default:
                handler = null;

        }

        return handler;
    }
}
