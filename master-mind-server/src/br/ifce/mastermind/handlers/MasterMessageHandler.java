package br.ifce.mastermind.handlers;

import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.util.MessageUtil;

import java.net.Socket;

/**
 * Created by jrocha on 25/07/14.
 */
public class MasterMessageHandler extends AbstractMessageHandler {

    public MasterMessageHandler(Socket socket) {
        super(ClientType.MASTER, socket);
    }

    @Override
    public void process() {
        MessageUtil.getMessage(getSocket());
        MessageUtil.sendMessage(getSocket(), "alooou");
    }
}
