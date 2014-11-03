package com.bstek.designer.support.facets;

import com.bstek.designer.support.library.DoradoLibraryType;
import com.intellij.facet.impl.ui.FacetEditorsFactoryImpl;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.facet.ui.libraries.FrameworkLibraryValidator;
import com.intellij.framework.library.DownloadableLibraryService;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-23
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
public class DoradoFacetConfiguration implements com.intellij.facet.FacetConfiguration {
    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext facetEditorContext, FacetValidatorsManager facetValidatorsManager) {
        FrameworkLibraryValidator validator = FacetEditorsFactoryImpl.getInstanceImpl().createLibraryValidator(DownloadableLibraryService.getInstance().createDescriptionForType(DoradoLibraryType.class), facetEditorContext, facetValidatorsManager, "dorado7");
        facetValidatorsManager.registerValidator(validator, new JComponent[0]);
        return new FacetEditorTab[]{};

    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
