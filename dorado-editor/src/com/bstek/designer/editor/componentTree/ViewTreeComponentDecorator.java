package com.bstek.designer.editor.componentTree;

import com.bstek.designer.resources.DoradoConstants;
import com.bstek.designer.core.componentTree.DoradoTreeDecorator;
import com.bstek.designer.core.model.DoradoMetaModel;
import com.intellij.designer.componentTree.AttributeWrapper;
import com.intellij.designer.model.IComponentDecorator;
import com.intellij.designer.model.MetaModel;
import com.intellij.designer.model.RadComponent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import icons.DoradoIcons;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by robin on 14-3-14.
 */
public class ViewTreeComponentDecorator extends DoradoTreeDecorator {


    public ViewTreeComponentDecorator(Project project) {
        super(project);
    }

    public void decorate(RadComponent component, SimpleColoredComponent renderer, AttributeWrapper wrapper, boolean full) {
        DoradoMetaModel metaModel = (DoradoMetaModel) component.getMetaModel();

        String iconPath = getProject().getBasePath() + File.separator + ".idea" + metaModel.getModelMeta().getIcon();
        File file = new File(iconPath);
        if (file.exists()) {
            Icon icon = DoradoIcons.load(file);
            renderer.setIcon(icon);
        }
        decorate(component, metaModel, renderer, wrapper, full);

    }

    public void decorate(RadComponent component, MetaModel metaModel, SimpleColoredComponent renderer, AttributeWrapper wrapper, boolean full) {
        String idOrName = component.getPropertyValue("id");
        if (idOrName == null) {
            idOrName = component.getPropertyValue("name");
        }
        if (idOrName != null&&!"".equals(idOrName)) {
            renderer.append(idOrName);
        }
        String type = metaModel.getTag();
        if (type != null) {
            SimpleTextAttributes typeStyle = wrapper.getAttribute(new SimpleTextAttributes(Font.PLAIN, DoradoConstants.InternalColor.DECORATIONS_COLOR));
            renderer.append(idOrName != null&&!"".equals(idOrName) ? String.format(" - %1s", type) : type,typeStyle);
        }
        if (component instanceof IComponentDecorator) {
            ((IComponentDecorator) component).decorateTree(renderer, wrapper);
        }
    }

}
