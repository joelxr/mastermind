package br.ifce.mastermind.message;

import br.ifce.mastermind.enums.ClientType;

import java.io.Serializable;

/**
 * Created by jrocha on 16/08/14.
 */
public class AbstractMessage implements Serializable{

    private ClientType clientType;

    private String clientName;

    private Integer sequence;

    private String raw;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
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

    @Override
    public String toString() {
        return "Message{" +
                "clientName='" + clientName + '\'' +
                ", sequence=" + sequence +
                ", raw=" + raw +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMessage that = (AbstractMessage) o;

        if (getSequence() != that.getSequence()) return false;
        if (getClientName() != null ? !getClientName().equals(that.getClientName()) : that.getClientName()!= null) return false;
        if (getClientType() != that.getClientType()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clientType != null ? clientType.hashCode() : 0;
        result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
        result = 31 * result + sequence;
        return result;
    }

}
