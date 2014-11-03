package com.bstek.designer.editor.model;

import com.bstek.designer.core.model.DoradoRuleConfigManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 14-3-16
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */
public class ViewRuleConfigManager extends DoradoRuleConfigManager {

    protected ViewRuleConfigManager(Project project) {
        super(project);
    }

    public static ViewRuleConfigManager getInstance(Project project) {
        return ServiceManager.getService(project, ViewRuleConfigManager.class);
    }

}
