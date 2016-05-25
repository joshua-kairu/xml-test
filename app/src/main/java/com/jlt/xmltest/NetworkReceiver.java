package com.jlt.xmltest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

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

// begin class NetworkReceiver
/**
 * This BroadcastReceiver intercepts the android.net.ConnectivityManager.CONNECTIVITY_ACTION,
 * which indicates a connection change. It checks whether the type is TYPE_WIFI.
 * If it is, it checks whether Wi-Fi is connected and sets the wifiConnected flag in the
 * main activity accordingly.
 *
 */
public class NetworkReceiver extends BroadcastReceiver {

    /** CONSTANTS */

    /** VARIABLES */

    /** CONSTRUCTOR */

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin onReceive
    public void onReceive( Context context, Intent intent ) {

        ConnectivityManager connMgr =
                ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Checks the user prefs and the network connection. Based on the result, decides
        // whether
        // to refresh the display or keep the current display.
        // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.

        // begin if for if the set preference is WiFi and if the network information has something as if the network information is WiFi
        if ( MainActivity.PREFERENCE_VALUE_NETWORK_WIFI.equals( MainActivity.sPref ) &&
             networkInfo != null &&
             networkInfo.getType() == ConnectivityManager.TYPE_WIFI ) {

            // If device has its Wi-Fi connection, sets refreshDisplay
            // to true. This causes the display to be refreshed when the user
            // returns to the app.

            MainActivity.refreshDisplay = true;

            // inform the user of the connected state

            Toast.makeText( context, R.string.wifi_connected, Toast.LENGTH_SHORT ).show();

        }  // end if for if the set preference is WiFi and if the network information has something as if the network information is WiFi

        // If the setting is ANY network and there is a network connection
        // (which by process of elimination would be mobile), sets refreshDisplay to true.

        // begin else if for if the setting is ANY network and there is a network connection
        else if ( MainActivity.PREFERENCE_VALUE_NETWORK_ANY.equals( MainActivity.sPref ) &&
                  networkInfo != null ) {

            MainActivity.refreshDisplay = true;

        } // end else if for if the setting is ANY network and there is a network connection

        // Otherwise, the app can't download content--either because there is no network
        // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
        // is no Wi-Fi connection.
        // Sets refreshDisplay to false.

        // begin else for when the app cannot download
        else {

            MainActivity.refreshDisplay = false;

            // inform the user of the sad state

            Toast.makeText( context, R.string.lost_connection, Toast.LENGTH_SHORT ).show();

        } // end else for when the app cannot download

    } // end onReceive

    /** Other Methods */

} // end class NetworkReceiver