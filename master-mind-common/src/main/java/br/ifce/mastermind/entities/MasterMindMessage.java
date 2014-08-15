package br.ifce.mastermind.entities;

import br.ifce.mastermind.enums.ClientType;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by jrocha on 28/07/14.
 */
public class MasterMindMessage implements Serializable {

    private ClientType type;

    private String clientName;

    private Color[] colors;

    private String raw;

    private Integer sequence;

    private Color[] response;

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public Color[] getResponse() {
        return this.response;
    }

    public void setResponse(Color[] response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "clientName='" + clientName + '\'' +
                ", sequence=" + sequence +
                ", colors=" + raw +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MasterMindMessage that = (MasterMindMessage) o;

        if (sequence != that.sequence) return false;
        if (clientName != null ? !clientName.equals(that.clientName) : that.clientName != null) return false;
        if (!Arrays.equals(colors, that.colors)) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
        result = 31 * result + (colors != null ? Arrays.hashCode(colors) : 0);
        result = 31 * result + sequence;
        return result;
    }
}
