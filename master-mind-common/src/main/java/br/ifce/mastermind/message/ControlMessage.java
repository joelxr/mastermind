package br.ifce.mastermind.message;

/**
 * Created by jrocha on 16/08/14.
 */
public class ControlMessage extends AbstractMessage {

    private String message;

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}
