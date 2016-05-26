package com.jlt.xmltest;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

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

// begin class MainActivity
public class MainActivity extends AppCompatActivity {

    /** CONSTANTS */

    /** Booleans */

    public static final boolean

    PREFERENCE_VALUE_INCLUDE_SUMMARY_TEXT_YES = true,

    PREFERENCE_VALUE_INCLUDE_SUMMARY_TEXT_NO = false;

    /** Strings */

    public static final String

    PREFERENCE_NAME_NETWORK = "listPref",

    PREFERENCE_NAME_INCLUDE_SUMMARY_TEXT = "PREFERENCE_NAME_INCLUDE_SUMMARY_TEXT",

    PREFERENCE_VALUE_NETWORK_WIFI = "Wi-Fi",

    PREFERENCE_VALUE_NETWORK_ANY = "Any",

    URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

    /** VARIABLES */

    /** Booleans */

    private boolean

    // Whether there is a Wi-Fi connection.

    wifiConnected = false,

    // Whether there is a mobile connection.

    mobileConnected = false;

    // Whether the display should be refreshed.

    private static boolean refreshDisplay = true;

    /** Network Receivers */

    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkReceiver networkReceiver = new NetworkReceiver();

    /* Strings */

    // The user's current network preference setting.
    private static String currentNetworkPreferenceSetting;

    /** METHODS */

    /** Getters and Setters */

    // getter for the wifiConnected
    public boolean isWifiConnected() { return wifiConnected; }

    // setter for the wifiConnected
    public void setWifiConnected( boolean wifiConnected ) { this.wifiConnected = wifiConnected; }

    // getter for the mobileConnected
    public boolean isMobileConnected() { return mobileConnected; }

    // setter for the mobileConnected
    public void setMobileConnected( boolean mobileConnected ) { this.mobileConnected = mobileConnected; }

    // getter for the refreshDisplay
    public static boolean isRefreshDisplay() { return refreshDisplay; }

    // setter for the refreshDisplay
    public static void setRefreshDisplay( boolean refreshDisplay ) { MainActivity.refreshDisplay = refreshDisplay; }

    // getter for the currentNetworkPreferenceSetting
    public static String getCurrentNetworkPreferenceSetting() { return currentNetworkPreferenceSetting; }

    // setter for the currentNetworkPreferenceSetting
    public static void setCurrentNetworkPreferenceSetting( String currentNetworkPreferenceSetting ) { MainActivity.currentNetworkPreferenceSetting = currentNetworkPreferenceSetting; }

    /** Overrides */

    @Override
    // begin onCreate
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        // Register BroadcastReceiver to track connection changes.
        IntentFilter filter = new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION );
        networkReceiver = new NetworkReceiver();
        this.registerReceiver( networkReceiver, filter );

    } // end onCreate

    // Refreshes the display if the network connection and the
    // pref settings allow it.
    @Override
    // begin onStart
    public void onStart() {
        super.onStart();

        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences( this );

        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        currentNetworkPreferenceSetting = sharedPrefs.getString( PREFERENCE_NAME_NETWORK, PREFERENCE_VALUE_NETWORK_WIFI );

        updateConnectedFlags();

        // Only loads the page if refreshDisplay is true. Otherwise, keeps previous
        // display. For example, if the user has set "Wi-Fi only" in prefs and the
        // device loses its Wi-Fi connection midway through the user using the app,
        // you don't want to refresh the display--this would force the display of
        // an error page instead of stackoverflow.com content.

        if ( isRefreshDisplay() == true ) { loadPage(); }

    } // end onStart

    @Override
    // begin onDestroy
    public void onDestroy() {

        super.onDestroy();

        // unregister the broadcast networkReceiver
        if ( networkReceiver != null ) { this.unregisterReceiver( networkReceiver ); }

    } // end onDestroy

    // Populates the activity's options menu.
    @Override
    // begin onCreateOptionsMenu
    public boolean onCreateOptionsMenu( Menu menu ) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate( R.menu.menu_main, menu );

        return true;

    } // end onCreateOptionsMenu

    // Handles the user's menu selection.
    @Override
    // begin onOptionsItemSelected
    public boolean onOptionsItemSelected( MenuItem item ) {

        // begin switch to determine which item is selected
        switch (item.getItemId()) {

            // case settings
            case R.id.menu_settings:

                // start the settings activity
                Intent settingsActivity = new Intent( getBaseContext(), SettingsActivity.class );

                startActivity( settingsActivity );

                return true;

            // case refresh
            case R.id.menu_refresh:

                // load the page again
                loadPage();

                return true;

            default:

                return super.onOptionsItemSelected( item );

        } // end switch to determine which item is selected

    } // end onOptionsItemSelected

    /** Other Methods */

    // begin method updateConnectedFlags
    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    private void updateConnectedFlags() {

        ConnectivityManager connMgr =
                ( ConnectivityManager ) getSystemService( Context.CONNECTIVITY_SERVICE );

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

        // if there is a network connection, set the appropriate flag to true
        // else set both flags false

        // begin if for if there is a connection
        if ( activeInfo != null && activeInfo.isConnected() ) {

            setWifiConnected( activeInfo.getType() == ConnectivityManager.TYPE_WIFI );
            setMobileConnected( mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE );

        } // end if for if there is a connection

        // begin else for when there is no connection
        else {

            setWifiConnected( false );
            setMobileConnected( false );

        } // end else for when there is no connection

    } // end method updateConnectedFlags

    // begin method loadPage
    // Uses AsyncTask subclass to download the XML feed from stackoverflow.com.
    // This avoids UI lock up. To prevent network operations from
    // causing a delay that results in a poor user experience, always perform
    // network operations on a separate thread from the UI.
    private void loadPage() {

        // begin if for if the user decides to download with any connection and either wifi or mobile are available
        // or if the user decides to use wifi and wifi is available
        if ( ( ( currentNetworkPreferenceSetting.equals( PREFERENCE_VALUE_NETWORK_ANY ) )
                &&
                ( isWifiConnected() == true
                  ||
                  isMobileConnected() == true ) )
             ||
             ( ( currentNetworkPreferenceSetting.equals( PREFERENCE_VALUE_NETWORK_WIFI ) )
                && ( isWifiConnected() == true ) )
            )  {

            // AsyncTask subclass to download the feeds
            new DownloadXMLAsyncTask( this ).execute( URL );

        } // end if for if the user decides to download with any connection and either wifi or mobile are available
          // or if the user decides to use wifi and wifi is available

        // else for when things do not allow for a download
        else { showErrorPage(); }

    } // end method loadPage

    // begin method showErrorPage
    // Displays an error if the app is unable to load content.
    private void showErrorPage() {

        setContentView( R.layout.activity_main );

        // The specified network connection is not available. Displays error message.

        WebView myWebView = (WebView) findViewById(R.id. m_wv_feeds );
        myWebView.loadData(
                getResources().getString( R.string.connection_error ),
                "text/html",
                null );

    } // end method showErrorPage


//    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
//    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try {
//                return loadXmlFromNetwork(urls[0]);
//            } catch (IOException e) {
//                return getResources().getString(R.string.connection_error);
//            } catch (XmlPullParserException e) {
//                return getResources().getString(R.string.xml_error);
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            setContentView(R.layout.main);
//            // Displays the HTML string in the UI via a WebView
//            WebView myWebView = (WebView) findViewById(R.id.webview);
//            myWebView.loadData(result, "text/html", null);
//        }
//    }
//
//    // Uploads XML from stackoverflow.com, parses it, and combines it with
//    // HTML markup. Returns HTML string.
//    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
//        InputStream stream = null;
//        StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
//        List<Entry> entries = null;
//        String title = null;
//        String url = null;
//        String summary = null;
//        Calendar rightNow = Calendar.getInstance();
//        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");
//
//        // Checks whether the user set the preference to include summary text
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean pref = sharedPrefs.getBoolean("summaryPref", false);
//
//        StringBuilder htmlString = new StringBuilder();
//        htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
//        htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
//                formatter.format(rightNow.getTime()) + "</em>");
//
//        try {
//            stream = downloadUrl(urlString);
//            entries = stackOverflowXmlParser.parse(stream);
//            // Makes sure that the InputStream is closed after the app is
//            // finished using it.
//        } finally {
//            if (stream != null) {
//                stream.close();
//            }
//        }
//
//        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
//        // Each Entry object represents a single post in the XML feed.
//        // This section processes the entries list to combine each entry with HTML markup.
//        // Each entry is displayed in the UI as a link that optionally includes
//        // a text summary.
//        for (Entry entry : entries) {
//            htmlString.append("<p><a hr ef='");
//            htmlString.append(entry.link);
//            htmlString.append("'>" + entry.title + "</a></p>");
//            // If the user set the preference to include summary text,
//            // adds it to the display.
//            if (pref) {
//                htmlString.append(entry.summary);
//            }
//        }
//        return htmlString.toString();
//    }
//
//    // Given a string representation of a URL, sets up a connection and gets
//    // an input stream.
//    private InputStream downloadUrl(String urlString) throws IOException {
//        URL url = new URL(urlString);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setReadTimeout(10000 /* milliseconds */);
//        conn.setConnectTimeout(15000 /* milliseconds */);
//        conn.setRequestMethod("GET");
//        conn.setDoInput(true);
//        // Starts the query
//        conn.connect();
//        InputStream stream = conn.getInputStream();
//        return stream;
//    }

} // end class MainActivity
