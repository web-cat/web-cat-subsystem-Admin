/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License
 |  along with Web-CAT; if not, write to the Free Software
 |  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 |
 |  Project manager: Stephen Edwards <edwards@cs.vt.edu>
 |  Virginia Tech CS Dept, 660 McBryde Hall (0106), Blacksburg, VA 24061 USA
\*==========================================================================*/

package net.sf.webcat.admin;

import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.ERXArrayUtilities;
import er.extensions.ERXValueUtilities;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import net.sf.webcat.core.*;
import net.sf.webcat.FeatureDescriptor;
import net.sf.webcat.FeatureProvider;
import org.apache.log4j.Logger;

// -------------------------------------------------------------------------
/**
 *  The main "control panel" page for subsystems in the administration
 *  tab.
 *
 *  @author  stedwar2
 *  @version $Id$
 */
public class SubsystemManagerPage
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page object.
     * 
     * @param context The context to use
     */
    public SubsystemManagerPage( WOContext context )
    {
        super( context );
    }


    //~ KVC Attributes (must be public) .......................................

    public Subsystem         subsystem;
    public NSArray           subsystems;
    public NSArray           newSubsystems;
    public FeatureDescriptor feature;
    public int               index;
    public String            providerURL;

    public static final String TERSE_DESCRIPTIONS_KEY =
        "terseSubsystemDescriptions";


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse( WOResponse response, WOContext context )
    {
        terse = null;
        subsystems = ERXArrayUtilities.sortedArraySortedWithKey(
            ( (Application)Application.application() )
                .subsystemManager().subsystems(),
            "name",
            EOSortOrdering.CompareCaseInsensitiveAscending );
        if ( newSubsystems == null )
        {
            for ( Iterator i = FeatureProvider.providers().iterator();
                  i.hasNext(); )
            {
                ( (FeatureProvider)i.next() ).refresh();
            }
            newSubsystems = ERXArrayUtilities.sortedArraySortedWithKey(
                newSubsystems(),
                "name",
                EOSortOrdering.CompareCaseInsensitiveAscending );
        }
        super.appendToResponse( response, context );
    }

    
    // ----------------------------------------------------------
    /**
     * Get the current servlet adaptor, if one is available.
     * @return the servlet adaptor, or null when none is available
     */
    public net.sf.webcat.WCServletAdaptor adaptor()
    {
        return net.sf.webcat.WCServletAdaptor.getInstance();
    }


    // ----------------------------------------------------------
    /**
     * Calculate the current set of subsystems that are available from
     * all registered providers, but that are not yet installed.  This
     * method assumes that the private <code>subsystems</code> data member
     * has already been initialized with a list of currently installed
     * subsystems.
     * @return an array of feature descriptors for available uninstalled
     *         subsystems
     */
    public NSArray newSubsystems()
    {
        Collection availableSubsystems = new HashSet();
        for ( Iterator i = FeatureProvider.providers().iterator();
              i.hasNext(); )
        {
            FeatureProvider provider = (FeatureProvider)i.next();
            if ( provider != null )
            {
                availableSubsystems.addAll( provider.subsystems() );
            }
        }
        for ( int i = 0; i < subsystems.count(); i++ )
        {
            Subsystem s = (Subsystem)subsystems.objectAtIndex( i );
            availableSubsystems.remove( s.descriptor().providerVersion() );
        }
        return new NSArray( availableSubsystems.toArray() );
    }


    // ----------------------------------------------------------
    /**
     * Determine if update download and installation support is active.
     * @return null to refresh the current page
     */
    public boolean canUpdate()
    {
        return adaptor() != null;
    }


    // ----------------------------------------------------------
    /**
     * Download the latest version of the current subsystem for updating
     * on restart.
     * @return null to refresh the current page
     */
    public WOComponent download()
    {
        clearErrors();
        String msg = subsystem.descriptor().providerVersion().downloadTo(
            adaptor().updateDownloadLocation() );
        possibleErrorMessage( msg );
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Download a new subsystem for installation on restart.
     * @return null to refresh the current page
     */
    public WOComponent downloadNew()
    {
        clearErrors();
        String msg = feature.providerVersion().downloadTo(
            adaptor().updateDownloadLocation() );
        possibleErrorMessage( msg );
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Scan the specified provider URL.
     * @return null to refresh the current page
     */
    public WOComponent scanNow()
    {
        clearErrors();
        if ( providerURL == null || providerURL.equals( "" ) )
        {
            errorMessage( "Please specify a provider URL first." );
        }
        else
        {
            if ( FeatureProvider.getProvider( providerURL ) == null )
            {
                errorMessage( "Cannot read feature provider information from "
                    + " specified URL: '" + providerURL + "'." );
            }
        }

        // Erase cache of new subsystems so it will be recalculated now
        newSubsystems = null;

        // refresh page
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Edit the selected subsystem's configuration settings.
     * @return the subsystem's edit page
     */
    public WOComponent edit()
    {
        ConfigureSubsystemPage page = (ConfigureSubsystemPage)
            pageWithName( ConfigureSubsystemPage.class.getName() );
        page.subsystem = subsystem;
        page.nextPage = this;
        return page;
    }


    // ----------------------------------------------------------
    /**
     * Toggle the
     * {@link net.sf.webcat.WCServletAdaptor#willUpdateAutomatically()}
     * attribute.
     * @return null to refresh the current page
     */
    public WOComponent toggleAutoUpdates()
    {
        net.sf.webcat.WCServletAdaptor adaptor = adaptor();
        adaptor.setWillUpdateAutomatically(
            !adaptor.willUpdateAutomatically() );
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the history URL for the current subsystem.
     * @return The history URL, or null if none is defined
     */
    public String subsystemHistoryUrl()
    {
        return subsystem.descriptor().getProperty( "history.url" );
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the information URL for the current subsystem.
     * @return The information URL, or null if none is defined
     */
    public String subsystemInfoUrl()
    {
        return subsystem.descriptor().getProperty( "info.url" );
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the history URL for the current subsystem.
     * @return The history URL, or null if none is defined
     */
    public String featureHistoryUrl()
    {
        return feature.getProperty( "history.url" );
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the information URL for the current subsystem.
     * @return The information URL, or null if none is defined
     */
    public String featureInfoUrl()
    {
        return feature.getProperty( "info.url" );
    }


    // ----------------------------------------------------------
    /**
     * Toggle whether or not the user wants verbose descriptions of subsystems
     * to be shown or hidden.  The setting is stored in the user's preferences
     * under the key specified by the VERBOSE_DESCRIPTIONS_KEY, and will be
     * permanently saved the next time the session's local changes are saved.
     */
    public void toggleVerboseDescriptions()
    {
        boolean verboseOptions = ERXValueUtilities.booleanValue(
            wcSession().userPreferences.objectForKey(
                TERSE_DESCRIPTIONS_KEY ) );
        verboseOptions = !verboseOptions;
        wcSession().userPreferences.setObjectForKey(
            Boolean.valueOf( verboseOptions ), TERSE_DESCRIPTIONS_KEY );
        wcSession().commitLocalChanges();
    }


    // ----------------------------------------------------------
    /**
     * Look up the user's preferences and determine whether or not to show
     * verbose subsystem descriptions in this component.
     * @return true if verbose descriptions should be hidden, or false if
     * they should be shown
     */
    public Boolean terse()
    {
        if ( terse == null )
        {
            terse = ERXValueUtilities.booleanValue(
                wcSession().userPreferences.objectForKey(
                    TERSE_DESCRIPTIONS_KEY ) )
                ? Boolean.TRUE : Boolean.FALSE;
        }
        return terse;
    }


    //~ Instance/static variables .............................................
    private Boolean terse;
    static Logger log = Logger.getLogger( SubsystemManagerPage.class );
}