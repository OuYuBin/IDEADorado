package com.bstek.designer.common.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.ui.PerspectiveFileEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-9
 * Time: 下午8:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class DoradoFileEditorWrapper extends PerspectiveFileEditor {

    protected DoradoDomElementsConfigComponent component;

    public DoradoFileEditorWrapper(Project project, VirtualFile file) {
        super(project, file);
        initialize();
        //DomElementEditorsFactory.getDomElementEditorsFactory().registerEditor(IDoradoElement.class, DoradoBasicDomElementEditor.class);
    }

    private void initialize() {
        loadModel();
    }


    @Nullable
    @Override
    protected DomElement getSelectedDomElement() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void setSelectedDomElement(DomElement domElement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    protected JComponent createCustomComponent() {
        return getComponent();
    }

    public abstract JComponent getCustomComponent();

    @Override
    public void commit() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return component;
    }

    @NotNull
    @Override
    public String getName() {
        return "Properties";
    }

    public void loadModel() {
        ResourceSet resourceSet = getResourceSet();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", getResourceFactory());
    }

    public ResourceSet getResourceSet() {
        return new ResourceSetImpl();
    }

    protected abstract Resource.Factory getResourceFactory();
}
