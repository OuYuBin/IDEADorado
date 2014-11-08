package com.bstek.designer.common.editor;

import com.bstek.designer.core.DoradoDesignerEditor;
import com.bstek.designer.core.surface.DoradoDesignerEditorPanel;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

/**
 * 主编辑器缓冲基类,主要用于扩展
 * Created by robin on 13-12-5.
 */
public class DoradoCommonDesignerEditor extends DoradoDesignerEditor {


    public DoradoCommonDesignerEditor(Project project, VirtualFile file) {
        super(project, file);
    }


    @Override
    public DoradoDesignerEditorPanel createDoradoDesignerPanel(Project project, Module module, VirtualFile file) {
        return null;
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Override
    public VirtualFile file() {
        return null;
    }
}

