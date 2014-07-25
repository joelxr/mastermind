package br.ifce.mastermind.util;

import java.util.Scanner;

/**
 * Created by jrocha on 25/07/14.
 */
public class Util {

    public static String readKeyboard() {
        System.out.println("Enter: ");
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }
}
