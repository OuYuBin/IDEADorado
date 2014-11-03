package com.bstek.designer.editor.surface;

import com.bstek.designer.common.model.IDoradoModelParser;
import com.bstek.designer.common.model.ViewDomFileDescription;
import com.bstek.designer.common.palette.DoradoPaletteManager;
import com.bstek.designer.common.surface.DoradoCommonDesignerEditorPanel;
import com.bstek.designer.core.AbstractDoradoToolWindowManager;
import com.bstek.designer.core.DoradoDesignerEditor;
import com.bstek.designer.core.model.DoradoRuleConfigManager;
import com.bstek.designer.core.palette.DoradoPaletteGroup;
import com.bstek.designer.core.palette.DoradoPaletteToolWindowContent;
import com.bstek.designer.core.surface.DoradoPropertySheetToolWindowContent;
import com.bstek.designer.editor.ViewComponentTreeToolWindowManager;
import com.bstek.designer.editor.componentTree.ViewTreeComponentDecorator;
import com.bstek.designer.editor.model.ViewDoradoModelParser;
import com.bstek.designer.editor.model.ViewRuleConfigManager;
import com.bstek.designer.editor.palette.ViewPaletteToolWindowManager;
import com.intellij.designer.componentTree.TreeComponentDecorator;
import com.intellij.designer.model.RadComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlFile;
import com.sun.istack.internal.NotNull;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by robin on 13-12-5.
 */
public class ViewDesignerEditorPanel extends DoradoCommonDesignerEditorPanel {

    public ViewDesignerEditorPanel(@NotNull DoradoDesignerEditor editor, @NotNull Project project, @NotNull Module module, @NotNull VirtualFile file) {
        super(editor, project, module, file);
    }

    public List<DoradoPaletteGroup> getPaletteGroups() {
        if (getDomFileDescription() instanceof ViewDomFileDescription) {
            DoradoPaletteManager paletteManager = new DoradoPaletteManager(getProject(), getDoradoMetaManager().getRulesConfig(getProject()));
            return paletteManager.getDoradoPaletteGroup();

        }
        return super.getPaletteGroups();
    }

    public TreeComponentDecorator getTreeDecorator() {
        if (treeComponentDecorator == null)
            treeComponentDecorator = new ViewTreeComponentDecorator(getProject());
        return treeComponentDecorator;
    }

    public DoradoRuleConfigManager getDoradoMetaManager() {
        return ViewRuleConfigManager.getInstance(getProject());
    }

    public IDoradoModelParser getModelParser(Project project, XmlFile xmlFile) {
        return new ViewDoradoModelParser(project, xmlFile);
    }

    @Nullable
    @Override
    public Object getData(@NonNls String dataId) {
        return null;
    }

    @Nullable
    @Override
    public RadComponent getRootComponent() {
        return null;
    }

    public AbstractDoradoToolWindowManager getDoradoDesignerWindowManager() {
        return ViewComponentTreeToolWindowManager.getInstance(getProject());
    }

    public DoradoPropertySheetToolWindowContent getDesignerToolWindow() {
        return ViewComponentTreeToolWindowManager.getInstance(this);
    }

    public AbstractDoradoToolWindowManager getPaletteWindowManager() {
        return ViewPaletteToolWindowManager.getInstance(getProject());
    }


    public DoradoPaletteToolWindowContent getPaletteToolWindow() {
        return ViewPaletteToolWindowManager.getInstance(this);
    }
}
