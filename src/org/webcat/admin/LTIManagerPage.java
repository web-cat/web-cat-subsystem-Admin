/*==========================================================================*\
 |  Copyright (C) 2006-2018 Virginia Tech
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

import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.appserver.ERXDisplayGroup;
import er.extensions.foundation.ERXArrayUtilities;
import er.extensions.foundation.ERXValueUtilities;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import net.sf.webcat.FeatureDescriptor;
import net.sf.webcat.FeatureProvider;
import org.apache.log4j.Logger;
import org.webcat.admin.PropertyListPage.Entry;
import org.webcat.core.*;
import org.webcat.core.lti.LMSInstance;

// -------------------------------------------------------------------------
/**
 *  The main "control panel" page for subsystems in the administration
 *  tab.
 *
 *  @author  Stephen Edwards
 */
public class LTIManagerPage
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page object.
     *
     * @param context The context to use
     */
    public LTIManagerPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public LMSInstance instance;
    public ERXDisplayGroup<LMSInstance> lmsDisplayGroup;
    public int index;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        lmsDisplayGroup.setObjectArray(LMSInstance.allObjects(
            session().defaultEditingContext()));
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public String lmsName()
    {
        return LMSInstance.lmsNameFor(instance);
    }


    // ----------------------------------------------------------
    public String ltiConfigUrl()
    {
        return Application.completeURLWithRequestHandlerKey(context(),
            Application.application().directActionRequestHandlerKey(),
            "ltiConfiguration", null, true, 0);
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(LTIManagerPage.class);
}
