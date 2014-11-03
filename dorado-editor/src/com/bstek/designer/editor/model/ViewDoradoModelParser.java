package com.bstek.designer.editor.model;

import com.bstek.designer.common.model.DoradoModelParser;
import com.bstek.designer.core.model.DoradoRuleConfigManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlFile;

/**
 * Created by robin on 14-3-20.
 */
public class ViewDoradoModelParser extends DoradoModelParser {


    public ViewDoradoModelParser(Project project, XmlFile xmlFile) {
        super(project, xmlFile);
    }

    @Override
    public DoradoRuleConfigManager getMetaManger(Project project) {
        return ViewRuleConfigManager.getInstance(project);
    }
}
