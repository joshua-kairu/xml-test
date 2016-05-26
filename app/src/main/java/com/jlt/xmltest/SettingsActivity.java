package com.jlt.xmltest;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

// begin class SettingsActivity
/*
*
* This preference activity has in its manifest declaration
* an intent filter for the ACTION_MANAGE_NETWORK_USAGE action.
* This activity provides a settings UI for users to
* specify network settings to control data usage
*
* */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    /** CONSTANTS */

    /** VARIABLES */

    /* Main Activities */

    private MainActivity mainActivity; // the main activity

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin onCreate
    public void onCreate( Bundle savedInstanceState ) {

        // 0. super things
        // 1. load the XML prefs file

        // 0. super things

        super.onCreate( savedInstanceState );

        // 1. load the XML prefs file

        addPreferencesFromResource( R.xml.preferences );

    } // end onCreate

    @Override
    // begin onResume
    protected void onResume() {

        // 0. super things
        // 1. register a callback to be invoked whenever a user changes a pref

        // 0. super things

        super.onResume();

        // 1. register a callback to be invoked whenever a user changes a pref

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener( this );

    } // end onResume

    @Override
    // begin onPause
    protected void onPause() {

        // 0. super things
        // 1. unregister pref change listener to reduce sys overhead

        // 0. super things

        super.onPause();

        // 1. unregister pref change listener to reduce sys overhead

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener( this );

    } // end onPause

    @Override
    // begin onSharedPreferenceChanged
    // fired when user changes a pref
    public void onSharedPreferenceChanged( SharedPreferences sharedPreferences, String key ) {

        // 0. tell the main activity to refresh
        // so that when the user returns to the main activity
        // the display refreshes to reflect the new changes

        // 0. tell the main activity to refresh
        // so that when the user returns to the main activity
        // the display refreshes to reflect the new changes

        MainActivity.setRefreshDisplay( true );

    } // end onSharedPreferenceChanged

} // end class SettingsActivity
