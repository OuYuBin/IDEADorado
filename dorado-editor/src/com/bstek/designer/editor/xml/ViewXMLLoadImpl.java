package com.bstek.designer.editor.xml;

import com.bstek.designer.common.xml.DoradoXMLLoadImpl;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author robin
 */
public class ViewXMLLoadImpl extends DoradoXMLLoadImpl {

    public ViewXMLLoadImpl(XMLHelper helper) {
        super(helper);

    }

    protected DefaultHandler makeDefaultHandler() {
        doradoSAXXMLHandler = new ViewSAXXMLHandler(resource, helper, options);
        return doradoSAXXMLHandler;
    }
}
