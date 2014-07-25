package br.ifce.mastermind.server;

import br.ifce.mastermind.constants.NetConstants;
import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.factory.MessageHandlerFactory;
import br.ifce.mastermind.handlers.AbstractMessageHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jrocha on 24/07/14.
 */
public class Server {

    private static Logger logger = LogManager.getLogger(Server.class.getName());

    private static Server instance;

    private Server () {

    }

    public static Server getInstance() {

        if (instance == null) {
            instance = new Server();
        }

        return instance;
    }

    public void start() {

        logger.error("Starting \"Master Mind\" server... waiting for players!");

        ServerSocket serverSocket;

        try {

            int count = 0;
            serverSocket = new ServerSocket(NetConstants.SERVER_PORT);
            Socket masterSocket = serverSocket.accept();
            AbstractMessageHandler masterHandler = MessageHandlerFactory.build(masterSocket, ClientType.MASTER);
            Thread masterThread = new Thread(masterHandler,"MASTER");
            logger.trace("MASTER is starting the game!");
            masterThread.start();

            while (masterHandler.isBusy()) {
                logger.trace("MASTER is busy... wait!");
                Thread.sleep(1000);
            }

            ThreadGroup players = new ThreadGroup("PLAYERS");

            while (true) {
                ++count;
                Socket playerSocket = serverSocket.accept();
                AbstractMessageHandler playerHandler = MessageHandlerFactory.build(playerSocket, ClientType.PLAYER);
                Thread t = new Thread(players , playerHandler, "PLAYER_" + count);
                logger.trace("PLAYER_" + count + " is starting the game!");
                t.start();
            }

        } catch (IOException e) {
            logger.error("ERROR: Couldn't create the socket.", e);

        } catch (InterruptedException e) {
            logger.error("ERROR: Main thread has been interrupted!", e);
        }
    }
}
