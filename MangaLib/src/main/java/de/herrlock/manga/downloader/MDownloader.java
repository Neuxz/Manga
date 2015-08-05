package de.herrlock.manga.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.herrlock.manga.downloader.clc.ChapterListContainer;
import de.herrlock.manga.downloader.dqc.DownloadQueueContainer;
import de.herrlock.manga.downloader.pmc.PictureMapContainer;
import de.herrlock.manga.exceptions.InitializeException;
import de.herrlock.manga.ui.LogWindow;
import de.herrlock.manga.util.Constants;
import de.herrlock.manga.util.configuration.DownloadConfiguration;

public abstract class MDownloader {
    protected static final Logger logger = LogManager.getLogger();

    protected final ChapterListContainer clc;
    protected final PictureMapContainer pmc;
    protected final DownloadQueueContainer dqc;

    /**
     * creates a new Downloader. this constructor initializes the ChapterListContainer, the PictureMapContainer and the
     * DownloadQueueContainer
     * 
     * @param conf
     *            the Configuration to use
     */
    public MDownloader( DownloadConfiguration conf ) {
        logger.entry();
        logger.info( conf.getUrl().toExternalForm() );
        try {
            this.clc = new ChapterListContainer( conf );
        } catch ( IOException ex ) {
            throw new InitializeException( ex );
        }
        this.pmc = new PictureMapContainer( this.clc );
        this.dqc = new DownloadQueueContainer( this.clc, conf );
    }

    protected abstract void run();

    /**
     * returns the number of Chapters in the ChapterListContainer
     * 
     * @return the number of Chapters
     * @see ChapterListContainer#getSize()
     */
    public final int getCLCSize() {
        return this.clc.getSize();
    }

    /**
     * returns the number of Pictures in the PicturesMapContainer
     * 
     * @return the number of Pictures
     * @see PictureMapContainer#getSize()
     */
    public final int getPMCSize() {
        return this.pmc.getSize();
    }

    /**
     * downloads everything in the PictureMapContainer<br>
     * basically calls {@link #downloadChapter(String, Map)} for every chapter
     */
    public void downloadAll() {
        logger.entry();
        Map<String, Map<Integer, URL>> picturemap = this.pmc.getPictureMap();
        List<String> keys = new ArrayList<>( picturemap.keySet() );
        Collections.sort( keys, Constants.STRING_NUMBER_COMPARATOR );
        LogWindow.setProgressMax( keys.size() );
        int progress = 0;
        for ( String key : keys ) {
            Map<Integer, URL> urlMap = picturemap.get( key );
            downloadChapter( key, urlMap );
            picturemap.remove( key );
            LogWindow.setProgress( ++progress );
        }
    }

    /**
     * downloads the Chapter with the "name"of {@code key} and the pictures from {@code urlMap}<br>
     * adds every Chapter th the DownloadQueueContainer and starts the download
     * 
     * @param key
     *            the name of the chapter (in general it is a number as String)
     * @param urlMap
     *            a map containing the URLs for the pictures
     * @see DownloadQueueContainer#downloadPages()
     */
    private void downloadChapter( String key, Map<Integer, URL> urlMap ) {
        logger.entry( key );
        logger.info( "Download chapter {} ({} pages)", key, urlMap.size() );
        File chapterFolder = new File( this.clc.getPath(), key );
        if ( chapterFolder.exists() || chapterFolder.mkdirs() ) {
            // add pictures to queue
            this.dqc.addMap( chapterFolder, urlMap );
            // start download
            this.dqc.downloadPages();
        } else {
            throw new RuntimeException( chapterFolder + " does not exists and could not be created" );
        }
        logger.info( "finished chapter {}\n", key );
    }

    /**
     * returns the folder where the downloaded chapters are stored in
     * 
     * @see ChapterListContainer#getPath()
     */
    public File getTargetFolder() {
        return this.clc.getPath();
    }

}
