package com.bstek.designer.editor;

import com.bstek.designer.common.propertyTable.DoradoCommonPropertyTablePanel;
import com.bstek.designer.core.DoradoComponentTreeToolWindowContent;
import com.bstek.designer.core.propertyTable.DoradoPropertyTablePanel;
import com.intellij.openapi.project.Project;

/**
 * Created by robin on 14-7-24.
 */
public class ViewComponentTreeToolWindowContent extends DoradoComponentTreeToolWindowContent {


    public ViewComponentTreeToolWindowContent(Project project, boolean updateOrientation) {
        super(project, updateOrientation);
    }

    @Override
    public DoradoPropertyTablePanel createDoradoPropertyTablePanel(Project project) {
        return new DoradoCommonPropertyTablePanel(project);
    }
}
