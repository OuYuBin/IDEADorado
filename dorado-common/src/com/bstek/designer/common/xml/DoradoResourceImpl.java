package com.bstek.designer.common.xml;

import java.io.IOException;
import java.util.Map;

import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave.XMLTypeInfo;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;


/**
 * @author Robin
 */
public abstract class DoradoResourceImpl extends XMLResourceImpl implements
        IDoradoResource {

    protected DoradoXMLLoadImpl xmlLoadImpl;

    public DoradoResourceImpl() {
    }

    public DoradoResourceImpl(URI uri) {
        super(uri);
        init();
    }

    public void load(Map<?, ?> options) throws IOException {
        super.load(options);
    }

    public abstract Map getOptions(Map options);

    public void setXsiOptions(Map options) {
        options.put(XMLResource.OPTION_SAVE_TYPE_INFORMATION,
                new XMLTypeInfo() {

                    public boolean shouldSaveType(EClass objectType,
                                                  EClassifier featureType, EStructuralFeature feature) {
                        return false;
                    }

                    public boolean shouldSaveType(EClass objectType,
                                                  EClass featureType, EStructuralFeature feature) {
                        return false;
                    }
                });
        options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.FALSE);
    }

    public void setFormatOptions(Map options) {
        options.put(XMLResource.OPTION_FORMATTED, Boolean.TRUE);
    }

    public void setPerformantOptions(Map options) {
        options.put(XMLResource.OPTION_CONFIGURATION_CACHE, Boolean.TRUE);
        options.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
        options.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        options.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.TRUE);
        // --不要使用预定义的实体
        options.put(XMLResource.OPTION_SKIP_ESCAPE, Boolean.FALSE);
    }

    public abstract Dorado7RulesConfigImpl getConfig();

    public DoradoXMLLoadImpl getXmlLoadImpl() {
        return xmlLoadImpl;
    }
}
