package com.bstek.designer.common.property;

import com.bstek.designer.core.model.DoradoRadViewComponent;
import com.bstek.designer.core.property.DoradoProperty;
import com.intellij.designer.model.Property;
import com.intellij.designer.propertyTable.PropertyEditor;
import com.intellij.designer.propertyTable.PropertyRenderer;
import com.intellij.designer.propertyTable.editors.TextEditor;
import com.intellij.designer.propertyTable.renderers.LabelPropertyRenderer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by robin on 14-5-15.
 */
public class DoradoAttributeProperty extends DoradoProperty {

    private com.bstek.dorado.idesupport.model.Property property;

    private PropertyRenderer propertyRenderer;

    private final PropertyEditor propertyEditor;

    public DoradoAttributeProperty(String name, com.bstek.dorado.idesupport.model.Property property) {
        this(null, name, property);
        propertyRenderer = new LabelPropertyRenderer(null);
    }

    public DoradoAttributeProperty(Property parent, String name, com.bstek.dorado.idesupport.model.Property property) {
        super(parent, name);
        this.property=property;
        propertyEditor=new TextEditor();
    }

    @Override
    public Object getValue(DoradoRadViewComponent component) throws Exception {
        Object value = null;

        XmlAttribute xmlAttribute = getAttribute(component);
        if (xmlAttribute != null) {
            value = xmlAttribute.getValue();
        }

        return value == null ? "" : value;

    }
    public void setValue(final @NotNull DoradoRadViewComponent doradoRadViewComponent, final @Nullable Object value) throws Exception {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                if (StringUtil.isEmpty((String) value)) {
                    XmlAttribute attribute = getAttribute(doradoRadViewComponent);
                    if (attribute != null) {
                        attribute.delete();
                    }
                }
                else {
                    doradoRadViewComponent.getTag().setAttribute(property.getName(),  (String)value);
                }
            }
        });
    }


    private XmlAttribute getAttribute(DoradoRadViewComponent component) {
        return component.getTag().getAttribute(this.property.getName());
    }

    public PropertyRenderer getRenderer() {
        return propertyRenderer;
    }

    @Override
    public PropertyEditor getEditor() {
        return propertyEditor;
    }

}
