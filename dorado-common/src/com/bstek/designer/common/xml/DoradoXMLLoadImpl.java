package com.bstek.designer.common.xml;

import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl;

/**
 * @author robin
 */
public class DoradoXMLLoadImpl extends XMLLoadImpl {

    protected DoradoSAXXMLHandler doradoSAXXMLHandler;

    public DoradoXMLLoadImpl(XMLHelper helper) {
        super(helper);
    }

    public DoradoSAXXMLHandler getDoradoSAXXMLHandler() {
        return doradoSAXXMLHandler;
    }
}
