package com.bstek.designer.editor.ui;

import com.bstek.designer.common.ui.DoradoFileEditorWrapper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.eclipse.emf.ecore.resource.Resource;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-11-10
 * Time: 下午6:52
 * To change this template use File | Settings | File Templates.
 */
public class ViewFileEditorWrapper extends DoradoFileEditorWrapper {

    public ViewFileEditorWrapper(Project project, VirtualFile file) {
        super(project, file);
    }

    @Override
    public JComponent getCustomComponent() {
        return new ViewDomElementsConfigComponent();
    }

    @Override
    protected Resource.Factory getResourceFactory() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
