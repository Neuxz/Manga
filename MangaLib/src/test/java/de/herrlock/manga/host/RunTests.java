package de.herrlock.manga.host;

import java.io.File;
import java.io.FilenameFilter;
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

import de.herrlock.manga.exceptions.MDRuntimeException;

/**
 * @author HerrLock
 */
public class RunTests {

    /**
     * Checks all ProvidedHoster-classes in the host-package
     * 
     * @throws IOException
     *             if an IOException occurs
     */
    @Test
    public void checkHoster() throws IOException {
        Collection<Class<? extends ChapterList>> classes = getClasses( "de.herrlock.manga.host.impl" );
        Set<String> classnames = new HashSet<>( classes.size() );
        for ( Class<? extends ChapterList> c : classes ) {
            classnames.add( c.getSimpleName().toLowerCase( Locale.GERMAN ) );
        }
        List<Hoster> hoster = Hosters.sortedValues();
        Set<String> hosternames = new HashSet<>( hoster.size() );
        for ( Hoster h : hoster ) {
            hosternames.add( h.getName().toLowerCase( Locale.GERMAN ) );
        }

        Assert.assertEquals( classnames, hosternames );
        Assert.assertTrue( classnames.equals( hosternames ) && hosternames.equals( classnames ) );
    }

    /**
     * from http://stackoverflow.com/a/862130/3680684
     */
    private static Collection<Class<? extends ChapterList>> getClasses( final String packageName ) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace( '.', '/' );
        Enumeration<URL> resources = classLoader.getResources( path );
        List<File> dirs = new ArrayList<>();
        while ( resources.hasMoreElements() ) {
            URL resource = resources.nextElement();
            dirs.add( new File( resource.getFile() ) );
        }

        final FilenameFilter IS_CLASS_FILE_FILTER = new FilenameFilter() {
            @Override
            public boolean accept( final File dir, final String name ) {
                return name.endsWith( ".class" );
            }
        };

        List<Class<? extends ChapterList>> classes = new ArrayList<>();
        for ( File directory : dirs ) {
            File[] files = directory.listFiles( IS_CLASS_FILE_FILTER );
            if ( files == null ) {
                break;
            }
            for ( File file : files ) {
                try {
                    String filename = file.getName();
                    int length = filename.length();
                    String className = packageName + '.' + filename.substring( 0, length - 6 );
                    Class<?> c = Class.forName( className );
                    if ( ChapterList.class.equals( c.getSuperclass() ) ) {
                        classes.add( c.asSubclass( ChapterList.class ) );
                    }
                } catch ( final ClassNotFoundException ex ) {
                    throw new MDRuntimeException( ex );
                }
            }
        }
        return classes;
    }
}
