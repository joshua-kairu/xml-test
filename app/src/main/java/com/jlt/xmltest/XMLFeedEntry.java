package com.jlt.xmltest;

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

// begin class XMLFeedEntry
// This class represents a single entry (post) in the XML feed.
// It includes the data members "title," "link," and "summary."
public class XMLFeedEntry {

    /** CONSTANTS */

    /** VARIABLES */

    /** Strings */

    private String title;
    private String link;
    private String summary;

    /** CONSTRUCTOR */

    // begin constructor
    public XMLFeedEntry( String title, String  summary, String link ) {

        setTitle( title );

        setSummary( summary );

        setLink( link );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    // getter for the title
    public String getTitle() { return title; }

    // setter for the title
    public void setTitle( String title ) { this.title = title; }

    // getter for the link
    public String getLink() {
        return link;
    }

    // setter for the link
    public void setLink( String link ) {
        this.link = link;
    }

    // getter for the summary
    public String getSummary() {
        return summary;
    }

    // setter for the summary
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /** Overrides */

    /** Other Methods */

} // end class XMLFeedEntry