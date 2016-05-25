package com.jlt.xmltest;
/**
 * Created by joshua on 4/28/16.
 */

/**
 * Copyright 2016 Kairu Joshua Wambugu
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// begin class NetworkReceiver

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
*
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