package com.bstek.designer.editor.xml;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bstek.designer.common.xml.DoradoResourceImpl;
import com.bstek.designer.common.xml.DoradoXMLHelperImpl;
import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import com.intellij.openapi.project.Project;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMLInfoImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;

/**
 * @author Robin
 */
public class ViewXMLResourceImpl extends DoradoResourceImpl {

    Project project;


    public ViewXMLResourceImpl() {
        super();
    }


    public ViewXMLResourceImpl(URI uri, Project project) {
        super(uri);
        this.project = project;
    }

    public void load(Map options) throws IOException {
        super.load(getOptions(options));
    }

    public void save(Map<?, ?> options) throws IOException {
        super.save(getOptions(options));
    }

    public Map getOptions(Map options) {
        Dorado7RulesConfigImpl config = getConfig();
        if (options == Collections.EMPTY_MAP) {
            options = new HashMap();
        }
        setXsiOptions(options);
        setFormatOptions(options);
        setPerformantOptions(options);
        XMLMapImpl xmlMap = new XMLMapImpl();
        xmlMap.setNoNamespacePackage(config.getEPackage());

        EPackage ePackage = config.getEPackage();

        XMLResource.XMLInfo info = new XMLInfoImpl();
        EClass eclass = (EClass) ePackage.getEClassifier("Property");
        info.setXMLRepresentation(XMLResource.XMLInfo.CONTENT);
        xmlMap.add(eclass.getEStructuralFeature("value"), info);

        info = new XMLInfoImpl();
        eclass = (EClass) ePackage.getEClassifier("Value");
        info.setXMLRepresentation(XMLResource.XMLInfo.CONTENT);
        xmlMap.add(eclass.getEStructuralFeature("value"), info);

        info = new XMLInfoImpl();
        eclass = (EClass) ePackage.getEClassifier("ClientEvent");
        info.setXMLRepresentation(XMLResource.XMLInfo.CONTENT);
        xmlMap.add(eclass.getEStructuralFeature("content"), info);

        options.put(XMLResource.OPTION_XML_MAP, xmlMap);
        return options;
    }

    protected XMLHelper createXMLHelper() {
        return new DoradoXMLHelperImpl(this);
    }

    @Override
    protected XMLLoad createXMLLoad() {
        xmlLoadImpl = new ViewXMLLoadImpl(createXMLHelper());
        return xmlLoadImpl;
    }

    @Override
    protected XMLSave createXMLSave() {
        return new ViewXMLSaveImpl(createXMLHelper());
    }

    @Override
    public Dorado7RulesConfigImpl getConfig() {
        // --守护创建文件时使用
        return new Dorado7RulesConfigImpl(project);
    }


}
