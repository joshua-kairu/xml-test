package com.jlt.xmltest;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua on 4/28/16.
 */

/**

 XML Test

 Android application to test fetching XML from the net

 Copyright (C) 2016 Kairu Joshua Wambugu

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see http://www.gnu.org/licenses/.

 */

// begin class StackOverflowXMLParser
/**
 * This class parses XML feeds from stackoverflow.com.
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 */
public class StackOverflowXMLParser {

    /** CONSTANTS */

    private static final String namespace = null; // no namespaces

    /** VARIABLES */

    /** CONSTRUCTOR */

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    /** Other Methods */

    // begin method parseXML
    // parses an XML given an input stream
    public List< XMLFeedEntry > parseXML ( InputStream inputStream ) throws XmlPullParserException, IOException {

        // 0. initialize an XML pull parser
        // 1. set appropriate parser features
        // 2. use the provided input stream to get the XML
        // 3. start the parsing process
        // 4. extract and process relevant XML data

        // begin try to try parse XML
        try {

            // 0. initialize an XML pull parser

            XmlPullParser xmlPullParser = Xml.newPullParser();

            // 1. set appropriate parser features

            // no processing of namespaces
            xmlPullParser.setFeature( XmlPullParser.FEATURE_PROCESS_NAMESPACES, false );

            // 2. use the provided input stream to get the XML

            // use the input stream with a null input encoding string
            xmlPullParser.setInput( inputStream, null );

            // 3. start the parsing process

            // Call next() and return event if it is START_TAG or END_TAG otherwise throw an exception.
            xmlPullParser.nextTag();

            // 4. extract and process relevant XML data

            return readXMLFeed( xmlPullParser );

        } // end try to try parse XML

        // close the stream

        finally { inputStream.close(); }

    } // end method parseXML

    // begin method readXMLFeed
    // does actual feed processing
    private List readXMLFeed( XmlPullParser xmlPullParser ) throws XmlPullParserException, IOException {

        // 0. initialize the list that will be returned
        // 1. ensure that the current start tag is a feed one
        // 2. as long as the XML has not ended
        // 2a. if the current tag is not a start tag
        // 2a1. continue execution
        // 2b. get the data inside the current tag
        // 2c. if the current tag is an entry tag
        // 2c1. add to the return list all entries around that tag
        // 2d. otherwise skip the entries
        // 3. return the added entries

        // 0. initialize the list that will be returned

        List returnedEntriesList = new ArrayList();

        // 1. ensure that the current start tag is a feed one

        xmlPullParser.require( XmlPullParser.START_TAG, namespace, "feed" );

        // 2. as long as the XML has not ended

        // begin while for when the next parser event is not the end tag
        while ( xmlPullParser.next() != XmlPullParser.END_TAG ) {

            // 2a. if the current tag is not a start tag

            // 2a1. continue execution

            if ( xmlPullParser.getEventType() != XmlPullParser.START_TAG ) { continue; }

            // 2b. get the data inside the current tag

            String name = xmlPullParser.getName();

            // 2c. if the current tag is an entry tag

            // 2c1. add to the return list all entries around that tag

            if ( name.equals( "entry" ) == true ) { returnedEntriesList.add( readXmlFeedEntry( xmlPullParser ) ); }

            // 2d. otherwise skip the entries

            else { skip( xmlPullParser ); }

        } // end while for when the next parser event is not the end tag

        // 3. return the added entries

        return returnedEntriesList;

    } // end method readXMLFeed

    // begin method readXMLEntry
    // parses the content of an XML entry
    // if it finds a title, summary, or link tag
    // if hands them to their respective read methods
    // any other tags are skipped
    private XMLFeedEntry readXmlFeedEntry ( XmlPullParser xmlPullParser ) throws XmlPullParserException, IOException {

        // 0. ensure that the current start tag is an entry one
        // 1. null initialize the title, summary and link
        // 2. as long as we have not hit an end tag
        // 2a. if the current tag is not the start tag
        // 2a1. continue execution
        // 2b. get the data inside the current tag
        // 2c. if the current tag is the title tag
        // 2c1. read and store the title
        // 2d. if the current tag is the summary tag
        // 2d1. read and store the summary
        // 2e. if the current tag is the link tag
        // 2e1. read and store the link
        // 2f. otherwise skip
        // 3. return an XML feed entry having the acquired title, summary, and link

        // 0. ensure that the current start tag is an entry one

        xmlPullParser.require( XmlPullParser.START_TAG, namespace, "entry" );

        // 1. null initialize the title, summary and link

        String title = "", summary = "", link = "";

        // 2. as long as we have not hit an end tag

        // begin while to loop through the XML as long as there is no end tag
        while ( xmlPullParser.next() != XmlPullParser.END_TAG ) {

            // 2a. if the current tag is not the start tag

            // 2a1. continue execution

            if ( xmlPullParser.getEventType() != XmlPullParser.START_TAG ) { continue; }

            // 2b. get the data inside the current tag

            String name = xmlPullParser.getName();

            // 2c. if the current tag is the title tag

            // 2c1. read and store the title

            if ( name.equals( "title" ) == true ) { title = readTitle( xmlPullParser ); }

            // 2d. if the current tag is the summary tag

            // 2d1. read and store the summary

            else if ( name.equals( "summary" ) == true ) { summary = readSummary( xmlPullParser ); }

            // 2e. if the current tag is the link tag

            // 2e1. read and store the link

            else if ( name.equals( "link" ) == true ) { link = readLink( xmlPullParser ); }

            // 2f. otherwise skip

            else { skip( xmlPullParser ); }

        } // end while to loop through the XML as long as there is no end tag

        // 3. return an XML feed entry having the acquired title, summary, and link

        return new XMLFeedEntry( title, summary, link );

    } // end method readXMLEntry

    // begin method readTitle
    // processes title tags in the XML feed
    private String readTitle( XmlPullParser xmlPullParser ) throws XmlPullParserException, IOException {

        // 0. ensure that the current start tag is called title

        xmlPullParser.require( XmlPullParser.START_TAG, namespace, "title" );

        // 1. read the title text from the parser

        String title = readText( xmlPullParser );

        // 2. ensure the current end tag is called title

        xmlPullParser.require( XmlPullParser.END_TAG, namespace, "title" );

        // 3. return the read text

        return title;

    } // end method readTitle

    // begin method readLink
    // processes link tags in the XML feed
    private String readLink( XmlPullParser xmlPullParser ) throws XmlPullParserException, IOException {

        // -1. null initialize the link that will be returned

        String returnLink = "";

        // 0. ensure that the current start tag is called link

        xmlPullParser.require( XmlPullParser.START_TAG, namespace, "link" );

        // 1. get the data inside the current tag

        String tag = xmlPullParser.getName();

        // 2. get the rel(ationship) type of this link

        String relType = xmlPullParser.getAttributeValue( namespace, "rel" );

        // 3. if the data inside the current tag reads "link"

        // begin if for if the tag reads link
        if ( tag.equals( "link" ) == true ) {

            // 3a. if the relationship type of this link is alternate

            // begin if for if the rel reads alternate
            if ( relType.equals( "alternate" ) == true ) {

                // 3a1. the link will be found in the "href" attribute (which specifies the link's destination)

                returnLink = xmlPullParser.getAttributeValue( namespace, "href" );

                // 3ab. gobble up the remaining data up to the next tag

                xmlPullParser.nextTag();

            } // end if for if the rel reads alternate

        } // end if for if the tag reads link

        // 4. ensure the current end tag is called link

        xmlPullParser.require( XmlPullParser.END_TAG, namespace, "link" );

        // 5. return the link

        return returnLink;

    } // end method readLink

    // begin method readSummary
    // processes summary tags in the XML feed
    private String readSummary( XmlPullParser xmlPullParser ) throws XmlPullParserException, IOException {

        // 0. ensure that the current start tag is called summary

        xmlPullParser.require( XmlPullParser.START_TAG, namespace, "summary" );

        // 1. read the summary text from the parser

        String summary = readText( xmlPullParser );

        // 2. ensure the current end tag is called summary

        xmlPullParser.require( XmlPullParser.END_TAG, namespace, "summary" );

        // 3. return the read text

        return summary;

    } // end method readTitle

    // begin method readText
    // extracts text values for the title and the summary tags
    private String readText( XmlPullParser xmlPullParser ) throws XmlPullParserException, IOException {

        // 0. null initialize the text to be returned
        // 1. if the next item in the parser is text
        // 1a. read that text
        // 1b. gobble up the remaining data up to the next tag
        // 2. return the read text

        // 0. null initialize the text to be returned

        String returnString = "";

        // 1. if the next item in the parser is text

        // begin if for if the next item in the parser is text
        if ( xmlPullParser.next() == XmlPullParser.TEXT ) {

            // 1a. read that text

            returnString = xmlPullParser.getText();

            // 1b. gobble up the remaining data up to the next tag

            xmlPullParser.nextTag();

        } // end if for if the next item in the parser is text

        // 2. return the read text

        return returnString;

    } // end method readText

    // begin method skip
    // skips tags unimportant
    private void skip( XmlPullParser xmlPullParser ) throws XmlPullParserException, IOException {

        // 0. if the parser's current event is not a start tag
        // 0a. throw an illegal state problem
        // 1. start with a nesting depth of 1
        // 2. as long as the nesting depth is not 0
        // 2a. if the next event is an end tag
        // 2a1. decrement the nesting depth by 1
        // 2b. if the next event is a start tag
        // 2b1. increment the nesting depth by 1

        // 0. if the parser's current event is not a start tag

        // 0a. throw an illegal state problem

        if ( xmlPullParser.getEventType() != XmlPullParser.START_TAG ) { throw new IllegalStateException(); }

        // 1. start with a nesting depth of 1

        int nestingDepth = 1;

        // 2. as long as the nesting depth is not 0

        // begin while that loops as long as the nesting depth is not 0
        while ( nestingDepth != 0 ) {

            // begin switch to determine the next parsing event
            switch ( xmlPullParser.next() ) {

                // 2a. if the next event is an end tag

                // 2a1. decrement the nesting depth by 1

                case XmlPullParser.END_TAG: nestingDepth--; break;

                // 2b. if the next event is a start tag

                // 2b1. increment the nesting depth by 1

                case XmlPullParser.START_TAG: nestingDepth++; break;

            } // end switch to determine the next parsing event

        } // end while that loops as long as the nesting depth is not 0

    } // end method skip

} // end class StackOverflowXMLParser