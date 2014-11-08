package com.bstek.designer.editor.xml;

import com.bstek.designer.common.xml.DoradoXMLSaveImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMLHelper;


public class ViewXMLSaveImpl extends DoradoXMLSaveImpl {

    public ViewXMLSaveImpl(XMLHelper helper) {
        super(helper);
    }

    protected void saveDataTypeSingle(EObject o, EStructuralFeature f) {
        super.saveDataTypeSingle(o, f);
    }

}
