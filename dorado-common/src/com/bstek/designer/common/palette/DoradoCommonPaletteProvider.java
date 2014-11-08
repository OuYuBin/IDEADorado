package com.bstek.designer.common.palette;

import com.bstek.designer.core.DoradoPaletteProvider;
import com.intellij.designer.palette.PaletteGroup;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import com.intellij.util.xml.impl.DomManagerImpl;

import java.beans.PropertyChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-11-13
 * Time: 下午3:48
 * To change this template use File | Settings | File Templates.
 */
public class DoradoCommonPaletteProvider extends DoradoPaletteProvider {


    public DoradoCommonPaletteProvider(Project project) {
        super(project);
    }

    @Override
    public void addListener(PropertyChangeListener propertyChangeListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeListener(PropertyChangeListener propertyChangeListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PaletteGroup[] getActiveGroups(PsiFile psiFile) {
        if (psiFile instanceof XmlFile) {
            DomFileDescription domFileDescription = DomManagerImpl.getDomManager(getProject()).getDomFileDescription(((XmlFile) psiFile));
            return getActiveGroups(domFileDescription);
        }
        return new PaletteGroup[0];
    }

    public PaletteGroup[] getActiveGroups(DomFileDescription domFileDescription) {
        return new PaletteGroup[0];
    }

    @Override
    public Project getProject() {
        return null;
    }

    @Override
    public Project project() {
        return null;
    }
}
