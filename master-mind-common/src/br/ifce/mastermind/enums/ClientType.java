package br.ifce.mastermind.enums;

/**
 * Created by jrocha on 25/07/14.
 */
public enum ClientType {

    MASTER("MASTER"), PLAYER("PLAYER");

    private final String name;

    ClientType(String name) {
        this.name = name;
    }


}
