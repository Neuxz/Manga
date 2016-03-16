package de.herrlock.manga.http.location;

import java.net.URL;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.herrlock.manga.http.response.DocumentResponse;
import de.herrlock.manga.http.response.Response;

/**
 * A Location that can be returnes by default when no other Loation matches
 * 
 * @author HerrLock
 */
public final class NotFoundLocation extends Location {

    /**
     * Create a new NotFoundLocation
     */
    public NotFoundLocation() {
        super( "/*" );
    }

    @Override
    protected Response handleXHR( final URL url ) {
        Document doc = Document.createShell( url.toExternalForm() );
        Element head = doc.select( "head" ).first();
        head.appendElement( "title" ).text( "Not Found (404)" );

        Element body = doc.select( "body" ).first();
        body.appendElement( "h1" ).text( "404" );
        body.appendElement( "div" ).text( "Not Found" );

        return new DocumentResponse( 404, doc );
    }

}