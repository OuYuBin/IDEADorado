package com.bstek.designer.editor.xml;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.bstek.designer.common.IDoradoElementConstants;
import com.bstek.designer.common.xml.DoradoSAXXMLHandler;
import com.bstek.designer.core.config.DoradoConfigRulesModelMeta;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;

import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.Property;
import com.bstek.dorado.idesupport.model.Rule;

//import com.bstek.dorado.utils.StringHelper;
//import com.bstek.ide.common.editor2.xml.DoradoSAXXMLHandler;
//import com.bstek.ide.config.BaseConfig;
//import com.bstek.ide.config.ViewConfig;
//import com.bstek.ide.util.DoradoEcoreUtils;
//import com.bstek.ide.xmi.BaseSAXXMLHandler;

/**
 * @author robin
 */
public class ViewSAXXMLHandler extends DoradoSAXXMLHandler {

    public ViewSAXXMLHandler(XMLResource xmiResource, XMLHelper helper,
                             Map options) {
        super(xmiResource, helper, options);
    }

    protected EStructuralFeature getFeatureByOtherStrategy(EObject peekObject,
                                                           String prefix, String name, boolean b) {
        // --针对不是聚合的reference引用的处理
        String featurnName = getFeatureNameByTagName(peekObject, name);
        EStructuralFeature feature = getFeature(peekObject, prefix,
                featurnName, true);
        if (feature == null) {
            feature = super.getFeatureByOtherStrategy(peekObject, prefix, name,
                    b);
        }

        return feature;
    }

    protected String getFeatureNameByTagName(EObject eOwner, String tagName) {
        String peekName = eOwner.eClass().getName();
        if (tagName.equals("View")) {
            return "DefaultView";
        } else if (tagName.equals("Validator")) {
            String type = attribs.getValue("type");
            return StringUtils.capitalize(type) + tagName;
        } else if (tagName.equals("PropertyDef")) {
            return IDoradoElementConstants.BASE_PROPERTY_DEF;
        } else if (ArrayUtils.contains(
                IDoradoElementConstants.BASE_DATA_OBJECTS, tagName)) {
            String type = attribs.getValue("type");
            if (type != null && !"".equals(type)) {
                Rule rule = getConfig().getRuleByName("Abstract" + tagName);
                Rule[] subRules = rule.getSubRules();
                for (int i = 0; i < subRules.length; i++) {
                    Property typeProperty = subRules[i]
                            .getPrimitiveProperty("type");
                    String defaultValue = (String) typeProperty
                            .getDefaultValue();
                    if (defaultValue != null) {
                        if (defaultValue.equals(type)) {
                            return subRules[i].getName();
                        }
                    }
                }
            }
        } else if (tagName.equals("Constraint")) {
            return "Lookup." + tagName;
        } else if (tagName.equals("DataType")) {
            if (ArrayUtils.contains(IDoradoElementConstants.DATATYPE_ELEMENTS,
                    eOwner.eClass().getName())) {
                return "PropertyDefinition" + tagName;
            }
        } else {
            DoradoConfigRulesModelMeta modelMeat = getConfig().getMetaOfModel(
                    eOwner);
            if (modelMeat != null) {
                Rule rule = getConfig().getMetaOfModel(eOwner).getRule();
                Map<String, Child> ownChildren = rule.getChildren();
                for (Iterator<Child> iter = ownChildren.values().iterator(); iter
                        .hasNext(); ) {
                    Child child = (Child) iter.next();
                    Set<Rule> subChildrenRules = child.getConcreteRules();
                    for (Iterator<Rule> subIter = subChildrenRules.iterator(); subIter
                            .hasNext(); ) {
                        Rule subChildRule = (Rule) subIter.next();
                        if (subChildRule.getNodeName().equals(tagName)) {
                            return subChildRule.getName();
                        }
                    }
                }
            }
        }
        return super.getFeatureNameByTagName(eOwner, tagName);
    }

}
