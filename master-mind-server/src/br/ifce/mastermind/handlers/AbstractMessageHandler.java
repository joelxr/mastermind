package br.ifce.mastermind.handlers;

import br.ifce.mastermind.enums.ClientType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

/**
 * Created by jrocha on 24/07/14.
 */
public abstract class AbstractMessageHandler implements Runnable {

    private ClientType type;
    private Socket socket;
    private Logger logger;
    private boolean isBusy;

    public AbstractMessageHandler(ClientType type, Socket socket) {
        this.type = type;
        this.socket = socket;
        this.logger = LogManager.getLogger(this.getClass().getName());
        this.isBusy = true;
    }

    public ClientType getType() {
        return this.type;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public boolean isBusy() {
        return this.isBusy;
    }

    public void setBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public abstract void process();

    @Override
    public void run() {
        this.process();
    }
}
