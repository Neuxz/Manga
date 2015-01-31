package de.herrlock.manga.downloader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

import de.herrlock.manga.util.Constants;

public class ConsoleDownloader extends MDownloader {

    public static void execute() {
        try ( OutputStream fOut = new FileOutputStream( Constants.TRACE_FILE ) ) {
            Properties p = new Properties();
            try ( InputStream fIn = new FileInputStream( Constants.SETTINGS_FILE ) ) {
                p.load( fIn );
            }
            new ConsoleDownloader( p, System.in, fOut ).start();
        } catch ( RuntimeException ex ) {
            throw ex;
        } catch ( Exception ex ) {
            throw new RuntimeException( ex );
        }
    }

    private Scanner sc;

    public ConsoleDownloader( Properties p, InputStream in, OutputStream out ) {
        super( p, out );
        this.sc = new Scanner( in, "UTF-8" );
    }

    @Override
    public void run() {
        this.trace.println( "run()" );
        try {
            initCLC();
            if ( goonCLC() ) {
                initPMC();
                if ( goonPMC() ) {
                    downloadAll();
                }
            }
        } catch ( IOException ex ) {
            ex.printStackTrace( System.out );
            throw new RuntimeException( ex );
        }
    }

    protected boolean goon() {
        System.out.println( "go on? y|n" );
        try {
            char c = this.sc.next( ".+" ).charAt( 0 );
            return c == 'y' || c == 'Y';
        } catch ( NoSuchElementException ex ) {
            return false;
        }
    }

    private boolean goonCLC() {
        int noOfChapters = getCLCSize();
        if ( noOfChapters > 0 ) {
            System.out.println( noOfChapters + " chapter" + ( noOfChapters > 1 ? "s" : "" ) + " availabile." );
            return goon();
        }
        return false;
    }

    public boolean goonPMC() {
        int noOfPictures = getPMCSize();
        if ( noOfPictures > 0 ) {
            System.out.println( noOfPictures + " pages" + ( noOfPictures > 1 ? "s" : "" ) + " availabile." );
            return goon();
        }
        return false;
    }
}