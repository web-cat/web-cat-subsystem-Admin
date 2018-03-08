/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2017-18 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/


package org.webcat.admin;

import org.webcat.core.Application;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import er.extensions.appserver.ERXDirectAction;

//-------------------------------------------------------------------------
/**
 * This direct action class handles all response actions for this subsystem.
 *
 * @author  edwards
 * @author  Last changed by $Author$
 * @version $Revision$, $Date$
 */
public class adminctl
    extends ERXDirectAction
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new DirectAction object.
     *
     * @param aRequest
     *            The request to respond to
     */
    public adminctl(WORequest aRequest)
    {
        super(aRequest);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * The default action simply returns an invalid request response.
     *
     * @return The session response
     */
    public WOActionResults defaultAction()
    {
        return RestResponse.error("invalid request");
    }


    // ----------------------------------------------------------
    /**
     * Triggers a JStack-like thread dump.
     *
     * @return An acknowledgment message.
     */
    public WOActionResults threadDumpAction()
    {
        JStack.threadDump();
        return RestResponse.message("thread dump generated");
    }


    // ----------------------------------------------------------
    /**
     * Enables SQL-level logging.
     *
     * @return An acknowledgment message.
     */
    public WOActionResults sqlLoggingOnAction()
    {
        Application.enableSQLLogging();
        return RestResponse.message("SQL logging enabled");
    }


    // ----------------------------------------------------------
    /**
     * Disables SQL-level logging.
     *
     * @return An acknowledgment message.
     */
    public WOActionResults sqlLoggingOffAction()
    {
        Application.disableSQLLogging();
        return RestResponse.message("SQL logging disabled");
    }
}
