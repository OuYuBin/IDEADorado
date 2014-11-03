package com.bstek.designer.common.propertyTable;

import com.bstek.designer.core.propertyTable.DoradoPropertyTablePanel;
import com.bstek.designer.core.propertyTable.DoradoRadPropertyTable;
import com.intellij.openapi.project.Project;

/**
 * Created by robin on 14-7-24.
 */
public class DoradoCommonPropertyTablePanel extends DoradoPropertyTablePanel {
    public DoradoCommonPropertyTablePanel(Project project) {
        super(project);
    }


    @Override
    public DoradoRadPropertyTable createDoradoPropertyTable() {
        return new DoradoCommonPropertyTable(getProject());
    }


}
