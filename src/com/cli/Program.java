package com.cli;

import com.imagetyperzapi.ImageTyperzAPI;

import java.util.concurrent.TimeUnit;

/**
 * Created by icebox on 22/05/17.
 */
public class Program {
    // command-line mode
    private static void command_line(String[] args) throws Exception {
        new CommandLineController(args).run();      // run commandline controller
    }

    // main/run method
    public static void main(String[] args) {
        try
        {
            Program.command_line(args);      // commandline
        }
        catch(Exception ex)
        {
            System.out.println(String.format("Error occured: %s", ex.getMessage()));     // print exception message
        }
    }
}