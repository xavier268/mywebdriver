/*
 * MIT License
 * 
 * Copyright (c) 2017 Xavier Gandillot
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
