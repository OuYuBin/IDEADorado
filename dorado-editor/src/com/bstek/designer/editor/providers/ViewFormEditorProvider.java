package com.bstek.designer.editor.providers;

import com.bstek.designer.common.AbstractDoradoFormEditorProvider;
import com.bstek.designer.common.model.ViewDomFileDescription;
import com.bstek.designer.editor.ViewDesignerEditor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.xml.DomFileDescription;
import com.intellij.util.xml.impl.DomManagerImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-8
 * Time: 下午11:22
 * To change this template use File | Settings | File Templates.
 */
public class ViewFormEditorProvider extends AbstractDoradoFormEditorProvider {
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        if (file.getFileType() == StdFileTypes.XML) {
            DomFileDescription domFileDescription = DomManagerImpl.getDomManager(project).getDomFileDescription(PsiManager.getInstance(project).findFile(file));
            if (domFileDescription instanceof ViewDomFileDescription) {
                return true;
            }
        }
        return super.accept(project, file);
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return (FileEditor) new ViewDesignerEditor(project,file);
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "view-editor";
    }

    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }

}
