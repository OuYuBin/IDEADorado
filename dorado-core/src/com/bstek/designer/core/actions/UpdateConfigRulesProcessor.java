package com.bstek.designer.core.actions;

import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-11-12
 * Time: 下午4:17
 * To change this template use File | Settings | File Templates.
 */
public class UpdateConfigRulesProcessor extends AbstractUpdateConfigRulesProcessor {

    private static final String TITLE = "Update Rules info";

    private static final String MESSAGE = "Update dorado7 config rules successfully";

    public UpdateConfigRulesProcessor(Project project) {
        super(project, TITLE, MESSAGE);
    }


}
