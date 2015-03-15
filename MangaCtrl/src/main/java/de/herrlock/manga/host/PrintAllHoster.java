package de.herrlock.manga.host;

import java.io.PrintStream;

/**
 * @author Jan Rau
 */
public final class PrintAllHoster {
    public static void execute() {
        printHoster( System.out );
    }

    public static void printHoster( PrintStream out ) {
        out.println( "availabile hoster" );
        for ( Hoster h : Hoster.values() ) {
            out.println( h );
        }
    }
    private PrintAllHoster() {
        // nothing to do
    }
}