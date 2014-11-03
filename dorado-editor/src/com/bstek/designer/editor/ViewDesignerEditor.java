package com.bstek.designer.editor;

import com.bstek.designer.common.editor.DoradoCommonDesignerEditor;
import com.bstek.designer.core.surface.DoradoDesignerEditorPanel;
import com.bstek.designer.editor.surface.ViewDesignerEditorPanel;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

/**
 * Created by robin on 13-12-5.
 */
public class ViewDesignerEditor extends DoradoCommonDesignerEditor {

    public ViewDesignerEditor(Project project, VirtualFile file) {
        super(project, file);
    }

    @Override
    public DoradoDesignerEditorPanel createDoradoDesignerPanel(Project project, Module module, VirtualFile file) {
        return new ViewDesignerEditorPanel(this, project, module, file);
    }


    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }
}
