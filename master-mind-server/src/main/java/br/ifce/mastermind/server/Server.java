package br.ifce.mastermind.server;

import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.engine.GameEngine;
import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.factory.MessageHandlerFactory;
import br.ifce.mastermind.handlers.AbstractMessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jrocha on 24/07/14.
 */
public class Server {

    private static Logger logger = Logger.getLogger(Server.class.getName());

    private static Server instance;

    private static GameEngine engine;

    private Server() {
        this.engine = GameEngine.getInstance();
    }

    public static Server getInstance() {

        if (instance == null) {
            instance = new Server();
        }

        return instance;
    }

    public void start() {

        logger.info("Starting \"Master Mind\" server... waiting for players!");

        ServerSocket serverSocket;

        try {

            int count = 0;
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
            Socket masterSocket = serverSocket.accept();
            AbstractMessageHandler masterHandler = MessageHandlerFactory.build(masterSocket, ClientType.MASTER);
            Thread masterThread = new Thread(masterHandler, "__MASTER__");
            logger.info("MASTER is starting the game!");
            masterThread.start();

            this.engine.setMasterHandler(masterHandler);

            ThreadGroup players = new ThreadGroup("PLAYERS");

            while (masterThread.isAlive()) {
                ++count;
                Socket playerSocket = serverSocket.accept();
                AbstractMessageHandler playerHandler = MessageHandlerFactory.build(playerSocket, ClientType.PLAYER);
                this.engine.addPlayerHandler(playerHandler);
                Thread t = new Thread(players, playerHandler, "PLAYER_" + String.format("%03d", (count)));
                logger.info("PLAYER_" + String.format("%03d", (count)) + " is starting the game!");
                t.start();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "ERROR: Couldn't create the socket.", e);
        }

        System.exit(0);
    }
}
