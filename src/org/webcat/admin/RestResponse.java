/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2017-2018 Virginia Tech
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
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

//-------------------------------------------------------------------------
/**
 * A simple XML response page for REST-style admin commands.
 *
 * @author  edwards
 * @author  Last changed by $Author$
 * @version $Revision$, $Date$
 */
public class RestResponse
    extends WOComponent
{
    //~ Instance/static fields ................................................

    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page.
     *
     * @param context The page's context
     */
    public RestResponse(WOContext context)
    {
        super(context);
    }


    // ----------------------------------------------------------
    /**
     * Generate a RestReponse page from parameters.
     * @param message The message to display.
     * @return The page containing the message.
     */
    public static RestResponse message(String message)
    {
        return page("message", message);
    }


    // ----------------------------------------------------------
    /**
     * Generate a RestReponse page from parameters.
     * @param message The message to display.
     * @return The page containing the message.
     */
    public static RestResponse error(String message)
    {
        return page("error", message);
    }


    // ----------------------------------------------------------
    /**
     * Generate a RestReponse page from parameters.
     * @param message The message to display.
     * @return The page containing the message.
     */
    public static RestResponse page(String elementName, String message)
    {
        RestResponse result =
            Application.wcApplication().pageWithName(RestResponse.class);
        result.elementName = elementName;
        result.message = message;
        return result;
    }


    //~ KVC Properties ........................................................

    public String message = "Invalid request";
    public String elementName = "error";


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public String description()
    {
        return getClass().getName() + "(" + elementName + "): " + message;
    }
}
