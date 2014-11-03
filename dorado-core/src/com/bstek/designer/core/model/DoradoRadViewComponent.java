package com.bstek.designer.core.model;

import com.intellij.designer.model.RadComponent;
import com.intellij.designer.model.RadVisualComponent;
import com.intellij.psi.xml.XmlTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robin on 14-3-27.
 */
public class DoradoRadViewComponent<T extends com.intellij.android.designer.model.RadViewComponent> extends RadVisualComponent {

    private final List<RadComponent> myChildren = new ArrayList<RadComponent>();
    private List properties;
    private XmlTag xmlTag;

    public XmlTag getTag(){
        if(xmlTag!=null){

        }
        return xmlTag;
    }

    public void setXmlTag(XmlTag xmlTag) {
        this.xmlTag = xmlTag;
    }

    public List getProperties() {
        return properties;
    }

    public void setProperties(List properties) {
        this.properties = properties;
    }

    public List<RadComponent> getChildren() {
        return myChildren;
    }
}

