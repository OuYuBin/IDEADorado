package com.bstek.designer.common.model;

import com.bstek.designer.common.property.DoradoAttributeProperty;
import com.bstek.designer.common.property.DoradoPropertiesProperty;
import com.bstek.designer.core.model.DoradoMetaModel;
import com.bstek.designer.core.model.DoradoRadViewComponent;
import com.intellij.designer.model.MetaModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.intellij.designer.model.Property;

/**
 * 属性解析器实现
 * Created by robin on 14-3-30.
 */
public class DoradoProperyParser {

    public DoradoProperyParser() {

    }


    public void loadRecursive(DoradoRadViewComponent component) throws Exception {
        load(component);

        for (Object child : component.getChildren()) {
            if (child instanceof DoradoRadViewComponent) {
                loadRecursive((DoradoRadViewComponent) child);
            }
        }
    }

    public void load(DoradoRadViewComponent component) throws Exception {
        MetaModel model = component.getMetaModelForProperties();
        component.setProperties(loadProperties(model));
    }

    private List<Property> loadProperties(MetaModel model) {
        List<Property> properties = new ArrayList<Property>();

        if (model instanceof DoradoMetaModel) {
            properties = loadAttributesProperty(model, properties);
            properties = loadPropertiesProperty(model, properties);
        }
        return properties;
    }

    private List<Property> loadAttributesProperty(MetaModel model, List<Property> properties) {
        Map attributeMap = ((DoradoMetaModel) model).getModelMeta().getPrimitivePropertyMap();
        for (Iterator iter = attributeMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            com.bstek.dorado.idesupport.model.Property property = (com.bstek.dorado.idesupport.model.Property) entry.getValue();
            DoradoAttributeProperty doradoAttributeProperty = new DoradoAttributeProperty((String) entry.getKey(), property);
            properties.add(doradoAttributeProperty);
        }
        return properties;
    }

    private List<Property> loadPropertiesProperty(MetaModel model, List<Property> properties) {
        Map propertyMap = ((DoradoMetaModel) model).getModelMeta().getPropertyMap();
        for (Iterator iter = propertyMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            com.bstek.dorado.idesupport.model.Property property = (com.bstek.dorado.idesupport.model.Property) entry.getValue();
            DoradoPropertiesProperty doradoPropertiesProperty = new DoradoPropertiesProperty((String) entry.getKey(), property);
            properties.add(doradoPropertiesProperty);
        }
        return properties;
    }

}

