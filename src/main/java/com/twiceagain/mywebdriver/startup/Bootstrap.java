/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import java.util.Arrays;
import java.util.List;

/**
 * Main entry point for the CLI interface. This class is merked as the default
 * entry point in the Manifest (single-jar packaging), donot change its
 * name or package..
 *
 * @author xavier
 */
public class Bootstrap {

    static final List<String> MENU = Arrays.asList(
            "help",
            "test-grid",
            "test-no-grid"
    );

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.printf("\nBootstrapping with parameters : %s\n", Arrays.toString(args));
        if (args.length > 0) {
            switch (args[0]) {
                case "help":
                    System.out.printf("Valid commands are : %s\n", MENU);
                    break;
                case "test-grid":
                    DemoGrid.main(Arrays.copyOfRange(args, 1, args.length));
                    break;
                case "test-no-grid":
                    DemoNoGrid.main(Arrays.copyOfRange(args, 1, args.length));
                    break;
                default:
                    System.out.printf("\nCommand [%s] is not recognized\nValid commands are : %s\n", args[0], MENU);
            }

        } else {
            System.out.printf("\nPlease specify a valid command\nValid commands are : %s\n", MENU);
        }
    }

}
