package com.bstek.designer.common;

import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.ui.PerspectiveFileEditor;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-8
 * Time: 下午10:53
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDoradoFormEditorProvider implements FileEditorProvider, DumbAware {


    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return false;
    }

    @NotNull
    @Override
    public abstract FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file);
    //{
        //return getEditorWrapper(project, file);
        //return new DoradoFileEditorWrapper(project, file);
        //return DomFileEditor.createDomFileEditor("view", DoradoIcons.DORADO7_VIEW_FILE,)
    //}

    @Override
    public void disposeEditor(@NotNull FileEditor editor) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element sourceElement, @NotNull Project project, @NotNull VirtualFile file) {
        return new FileEditorState() {
            @Override
            public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
                return true;
            }
        };
    }

    @Override
    public void writeState(@NotNull FileEditorState state, @NotNull Project project, @NotNull Element targetElement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public abstract String getEditorTypeId();

    @NotNull
    @Override
    public abstract FileEditorPolicy getPolicy();

}
