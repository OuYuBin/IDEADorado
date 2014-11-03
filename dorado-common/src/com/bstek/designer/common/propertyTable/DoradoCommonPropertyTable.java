package com.bstek.designer.common.propertyTable;

import com.bstek.designer.core.propertyTable.DoradoRadPropertyTable;
import com.intellij.designer.model.ErrorInfo;
import com.intellij.designer.model.PropertiesContainer;
import com.intellij.designer.model.Property;
import com.intellij.designer.model.RadComponent;
import com.intellij.designer.propertyTable.PropertyTableTab;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by robin on 14-7-24.
 */
public class DoradoCommonPropertyTable extends DoradoRadPropertyTable {

    public DoradoCommonPropertyTable(Project myProject) {
        super(myProject);
    }

    @Override
    public List<ErrorInfo> getErrors(@NotNull PropertiesContainer container) {
        return container instanceof RadComponent ? RadComponent.getError((RadComponent) container) : Collections.<ErrorInfo>emptyList();
    }

    protected List<Property> getProperties(PropertiesContainer component) {
        PropertyTableTab tab = getMyPropertyTablePanel().getCurrentTab();
        if (tab != null) {
            return ((RadComponent) component).getProperties(tab.getKey());
        }
        return super.getProperties(component);
    }

    public void update() {
        try {
            removeSelectionListener();
            if (getMyArea() == null) {
                update(Collections.EMPTY_LIST, null);
            } else {
                List selection = getMyArea().getSelection();
                update(selection,
                        getMyDesigner().getSelectionProperty(getCurrentKey()));
            }
        } finally {
            addSelectionListener();
        }
    }
}
