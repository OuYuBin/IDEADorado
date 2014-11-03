package com.bstek.designer.common.property;

import com.bstek.designer.core.model.DoradoRadViewComponent;
import com.bstek.designer.core.property.DoradoProperty;
import com.intellij.designer.model.Property;
import com.intellij.designer.propertyTable.PropertyEditor;
import com.intellij.designer.propertyTable.PropertyRenderer;
import com.intellij.designer.propertyTable.editors.TextEditor;
import com.intellij.designer.propertyTable.renderers.LabelPropertyRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;

/**
 * Created by robin on 14-5-17.
 */
public class DoradoPropertiesProperty extends DoradoProperty {

    private com.bstek.dorado.idesupport.model.Property property;

    private PropertyRenderer propertyRenderer;

    private final PropertyEditor propertyEditor;

    public DoradoPropertiesProperty(String name, com.bstek.dorado.idesupport.model.Property property) {
        this(null, name, property);
        propertyRenderer = new LabelPropertyRenderer(null);
    }

    public DoradoPropertiesProperty(Property parent, String name, com.bstek.dorado.idesupport.model.Property property) {
        super(parent, name);
        this.property = property;
        propertyEditor=new TextEditor();


    }

    public Object getValue(DoradoRadViewComponent component) throws Exception {
        Object value = null;
        //--当前容器对应的XmlTag
        XmlTag xmlTag = component.getTag();
        PsiElement[] elements = xmlTag.getChildren();
        for (PsiElement element : elements) {
            if (element instanceof XmlTag) {
                String tagName = ((XmlTag) element).getName();
                if (tagName.equals("Property")) {
                    XmlAttribute attribute = ((XmlTag) element).getAttribute("name");
                    String name = attribute.getValue();
                    if (name.equals(getName())) {
                        value = ((XmlTag) element).getValue().getText();
                        break;
                    }
                }
            }
        }
        return value == null ? "" : value;
    }

    public PropertyRenderer getRenderer() {
        return propertyRenderer;
    }

    @Override
    public PropertyEditor getEditor() {
        return propertyEditor;
    }
}
