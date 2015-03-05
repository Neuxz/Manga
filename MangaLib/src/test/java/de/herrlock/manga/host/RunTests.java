package de.herrlock.manga.host;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jan Rau
 */
public class RunTests {

    @Test
    public void checkHoster() throws IOException {
        Collection<Class<? extends ChapterList>> classes = getClasses( "de.herrlock.manga.host" );
        Set<String> classnames = new HashSet<>( classes.size() );
        for ( Class<? extends ChapterList> c : classes ) {
            classnames.add( c.getSimpleName().toLowerCase( Locale.GERMAN ) );
        }
        Hoster[] hoster = Hoster.values();
        Set<String> hosternames = new HashSet<>( hoster.length );
        for ( Hoster h : hoster ) {
            hosternames.add( h.name().toLowerCase( Locale.GERMAN ) );
        }

        Assert.assertTrue( classnames.equals( hosternames ) && hosternames.equals( classnames ) );
    }

    /**
     * from http://stackoverflow.com/a/862130/3680684
     */
    private static Collection<Class<? extends ChapterList>> getClasses( String packageName ) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace( '.', '/' );
        Enumeration<URL> resources = classLoader.getResources( path );
        List<File> dirs = new ArrayList<>();
        while ( resources.hasMoreElements() ) {
            URL resource = resources.nextElement();
            dirs.add( new File( resource.getFile() ) );
        }

        List<Class<? extends ChapterList>> classes = new ArrayList<>();
        for ( File directory : dirs ) {
            List<Class<?>> allClasses = new ArrayList<>();
            if ( directory.exists() ) {
                File[] files = directory.listFiles();
                for ( File file : files ) {
                    try {
                        String filename = file.getName();
                        if ( filename.endsWith( ".class" ) ) {
                            String className = packageName + '.' + filename.substring( 0, filename.length() - 6 );
                            allClasses.add( Class.forName( className ) );
                        }
                    } catch ( ClassNotFoundException ex ) {
                        throw new RuntimeException( ex );
                    }
                }
            }
            for ( Class<?> c : allClasses ) {
                if ( ChapterList.class.equals( c.getSuperclass() ) ) {
                    classes.add( c.asSubclass( ChapterList.class ) );
                }
            }
        }
        return classes;
    }
}