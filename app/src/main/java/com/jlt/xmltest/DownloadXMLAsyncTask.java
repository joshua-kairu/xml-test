package com.jlt.xmltest;
/**
 * Created by joshua on 4/28/16.
 */

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

// begin class DownloadXMLAsyncTask
// AsyncTask used to download XML
public class DownloadXMLAsyncTask extends AsyncTask< String, Void, String > {

    /** CONSTANTS */

    /** VARIABLES */

    /** Activities */

    private MainActivity mainActivity; // the main activity

    /** CONSTRUCTOR */

    // begin constructor
    public DownloadXMLAsyncTask( MainActivity mainActivity ) { this.mainActivity = mainActivity; } // end constructor

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */
    @Override
    // begin doInBackground
    protected String doInBackground( String... urls ) {

        // 0. return the loaded XML

        // 0. return the loaded XML

        // try return loaded XML

        try { return loadXMLFromNet( urls[ 0 ] ); }

        // catch IO issues

        catch ( IOException e ) { return mainActivity.getResources().getString( R.string.connection_error ); }

        // catch XML parsing issues

        catch ( XmlPullParserException e ) { return mainActivity.getResources().getString( R.string.xml_error ); }

    } // end doInBackground

    @Override
    // begin onPostExecute
    protected void onPostExecute( String result ) {

        // 0. put the main layout on the main activity
        // 1. get the web view from main layout
        // 2. put the result in the web view in HTML form

        // 0. put the main layout on the main activity

        mainActivity.setContentView( R.layout.activity_main );

        // 1. get the web view from main layout

        WebView webView = ( WebView ) mainActivity.findViewById( R.id.m_wv_feeds );

        // 2. put the result in the web view in HTML form

        webView.loadData( result, "text/html", null );

    } // end onPostExecute

    /** Other Methods */

    // begin method loadXMLFromNet
    // gets an XML,
    // parses it,
    // combines it with HTML markup, and
    // returns a HTML string
    private String loadXMLFromNet( String urlString ) throws XmlPullParserException, IOException {

        // 0. null initialize
        // 0a. the input stream for getting the feed
        // 0b. the feed entries
        // 0c. the title
        // 0d. the url
        // 0e. the summary
        // 1. get the time now
        // 2. get a simple date format that will be used to display time
        // 3. instantiate the XML parser
        // 4. check to see if user set preference to include summary text
        // 5. build the return HTML to show the page title and the last updated time
        // 6. get an input stream from the net URL
        // 7. parse feeds from the input stream
        // 8. close the input stream
        // 9. for each feed
        // 9a. append the feed's link to the return HTML
        // 9b. append the feed's title to the return HTML
        // 9c. if the user wants it, append the feed's summary to the return HTML
        // 10. return the return HTML

        // 0. null initialize
        // 0a. the input stream for getting the feed

        InputStream inputStream = null;

        // 0b. the feed entries

        List< XMLFeedEntry > xmlFeedEntries = null;

        // 0c. the title

        String feedTitle = null;

        // 0d. the url

        String url = null;

        // 0e. the summary

        String feedSummary = null;

        // 1. get the time now

        Calendar rightNow = Calendar.getInstance();

        // 2. get a simple date format that will be used to display time

        DateFormat dateFormatter = new SimpleDateFormat( "MMM dd h:mmaa", Locale.ENGLISH );

        // 3. instantiate the XML parser

        StackOverflowXMLParser stackOverflowXMLParser = new StackOverflowXMLParser();

        // 4. check to see if user set preference to include summary text

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( mainActivity );
        boolean shouldIncludeSummaryText = sharedPreferences.getBoolean( MainActivity.PREFERENCE_NAME_INCLUDE_SUMMARY_TEXT, MainActivity.PREFERENCE_VALUE_INCLUDE_SUMMARY_TEXT_NO );

        // 5. build the return HTML to show the page title and the last updated time

        StringBuilder returnHTMLString =

                new StringBuilder()

                .append( "<h3>" + mainActivity.getResources().getString( R.string.page_title ) + "</h3>" )

                .append( "<em>" + mainActivity.getResources().getString( R.string.updated ) + " " + dateFormatter.format( rightNow.getTime() ) + "</em>" );

        // begin try to try work on the net
        try {

            // 6. get an input stream from the net URL

            inputStream = downloadURL( urlString );

            xmlFeedEntries = stackOverflowXMLParser.parseXML( inputStream );

            // 7. parse feeds from the input stream

        } // end try to try work on the net

        // 8. close the input stream

        finally { if ( inputStream != null ) { inputStream.close(); } }

        // 9. for each feed

        // begin enhanced for to go through each XML feed entry
        for ( XMLFeedEntry xmlFeedEntry : xmlFeedEntries ) {

            returnHTMLString

                    // 9a. append the feed's link to the return HTML

                    .append( "<p><a href='" )

                    .append( xmlFeedEntry.getLink() )

                    // 9b. append the feed's title to the return HTML

                    .append( "'>" + xmlFeedEntry.getTitle() + "</a></p>" );

            // 9c. if the user wants it, append the feed's summary to the return HTML

            if ( shouldIncludeSummaryText == true ) { returnHTMLString.append( xmlFeedEntry.getSummary() ); }

        } // end enhanced for to go through each XML feed entry

        // 10. return the return HTML

        return returnHTMLString.toString();

    } // end method loadXMLFromNet

    // begin method downloadURL
    // given a URL, sets up a connection and gets an input stream from that connection
    private InputStream downloadURL( String urlString ) throws IOException {

        // 0. create a URL from the string
        // 1. open a HTTP connection from the URL
        // 2. set a HTTP read timeout of 10 seconds
        // 3. set a HTTP connect timeout of 15 seconds
        // 4. use the GET HTTP request method
        // 5. allow this HTTP connection to accept input
        // 6. start the HTTP connection
        // 7. get a stream from the connection
        // 8. return this stream

        // 0. create a URL from the string

        URL url = new URL( urlString );

        // 1. open a HTTP connection from the URL

        HttpURLConnection httpURLConnection = ( HttpURLConnection ) url.openConnection();

        // 2. set a HTTP read timeout of 10 seconds

        httpURLConnection.setReadTimeout( 10 * 1000 );

        // 3. set a HTTP connect timeout of 15 seconds

        httpURLConnection.setConnectTimeout( 15 * 1000 );

        // 4. use the GET HTTP request method

        httpURLConnection.setRequestMethod( "GET" );

        // 5. allow this HTTP connection to accept input

        httpURLConnection.setDoInput( true );

        // 6. start the HTTP connection

        httpURLConnection.connect();

        // 7. get a stream from the connection

        // 8. return this stream

        return httpURLConnection.getInputStream();

    } // edn method downloadURL

} // end class DownloadXMLAsyncTask