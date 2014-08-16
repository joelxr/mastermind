package br.ifce.mastermind.message;

import br.ifce.mastermind.enums.ClientType;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by jrocha on 28/07/14.
 */
public class MasterMindMessage extends AbstractMessage {

    private Color[] colors;

    private Color[] response;

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }

    public Color[] getResponse() { return this.response; }

    public void setResponse(Color[] response) {
        this.response = response;
    }
}
