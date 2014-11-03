package com.bstek.designer.core.model;

import com.bstek.designer.core.component.DoradoProjectComponentImpl;
import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import com.intellij.openapi.project.Project;
import com.intellij.util.Alarm;
import com.intellij.util.ui.update.MergingUpdateQueue;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 14-3-16
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 * 用于生成dorado规则及附加模型信息管理对象,因为在Eclipse中已通过构建EMF模型时就完成了附加模型信息的收集,所以这里只需要接入重用即可
 */
public class DoradoRuleConfigManager {

    private final MergingUpdateQueue sessionQueue;

    private Dorado7RulesConfigImpl dorado7RulesConfig;

    private DoradoProjectComponentImpl doradoProjectComponent;

    protected DoradoRuleConfigManager(Project project) {
        loadModel(project);
        sessionQueue = new MergingUpdateQueue("dorado.designer", 10, true, null, project, null, Alarm.ThreadToUse.OWN_THREAD);
    }


    public MergingUpdateQueue getSessionQueue() {
        return sessionQueue;
    }


    protected void loadModel(Project project) {
        doradoProjectComponent = project.getComponent(DoradoProjectComponentImpl.class);
        dorado7RulesConfig = doradoProjectComponent.getDorado7RulesConfig();
        if (dorado7RulesConfig == null) {
            Dorado7RulesConfigImpl.apply(project);
        }
    }

    public Dorado7RulesConfigImpl getRulesConfig(Project project) {
        Dorado7RulesConfigImpl currentDorado7RulesConfig = doradoProjectComponent.getDorado7RulesConfig();
        if (currentDorado7RulesConfig != this.dorado7RulesConfig) {
            dorado7RulesConfig = currentDorado7RulesConfig;
        }
        return dorado7RulesConfig;
    }

}
