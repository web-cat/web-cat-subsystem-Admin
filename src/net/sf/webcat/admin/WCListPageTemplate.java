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
import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import er.directtoweb.*;
import org.apache.log4j.Logger;

// -------------------------------------------------------------------------
/**
 * The template for D2W list pages in Web-CAT.
 *
 * @author edwards
 * @version $Id$
 */
public class WCListPageTemplate
    extends er.directtoweb.ERD2WListPage
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WCListPageTemplate object.
     * 
     * @param context The page's context
     */
    public WCListPageTemplate( WOContext context )
    {
        super( context );
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Returns the alternate row color for tables on this page.
     * Because of the way the alternating row info is computed,
     * this is really the color of the "first" row.  The d2w.d2wmodel
     * rule file sets the table background to "#eeeeee" for list
     * tasks to create the alternating effect.
     *
     * @return The color as a string
     */
    public String backgroundColorForTableDark()
    {
        return "white";
    }
}