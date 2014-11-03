package com.bstek.designer.common.xml;

import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl;

import java.util.Map;

/**
 * @author robin
 */
public interface IDoradoResource extends Resource {

    public Map getOptions(Map options);

    public Dorado7RulesConfigImpl getConfig();

    //--获取xmlload实现
    public XMLLoadImpl getXmlLoadImpl();

}
