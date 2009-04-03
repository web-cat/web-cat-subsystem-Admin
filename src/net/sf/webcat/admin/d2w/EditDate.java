/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
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

package net.sf.webcat.admin.d2w;

import com.webobjects.appserver.*;
import com.webobjects.eoaccess.*;
import com.webobjects.foundation.*;

//-------------------------------------------------------------------------
/**
 * A customized version of
 * {@link er.directtoweb.components.ERDEditDateJavascript}
 * that uses the user's specified date formatting and selected time zone,
 * and also uses Web-CAT's preferred date picker widget.
 *
 *  @author edwards
 *  @version $Id$
 */
public class EditDate
    extends er.directtoweb.components.ERDCustomEditComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public EditDate( WOContext context )
    {
        super( context );
    }

    // ----------------------------------------------------------
    public String controlId()
    {
        if (id == null)
        {
            id = "date" + context().elementID();
        }
        return id;
    }


    // ----------------------------------------------------------
    @Override
    public void reset()
    {
        id = null;
    }


    // ----------------------------------------------------------
    private String id = null;
}
