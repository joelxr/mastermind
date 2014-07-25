package br.ifce.mastermind.handlers;

import br.ifce.mastermind.enums.ClientType;

import java.net.Socket;

/**
 * Created by jrocha on 25/07/14.
 */
public class PlayerMessageHandler extends AbstractMessageHandler {

    public PlayerMessageHandler(Socket socket) {
        super(ClientType.PLAYER, socket);
    }

    @Override
    public void process() {


    }
}
