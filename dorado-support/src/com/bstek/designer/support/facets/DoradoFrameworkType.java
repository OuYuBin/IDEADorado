package com.bstek.designer.support.facets;

import com.intellij.facet.ui.FacetBasedFrameworkSupportProvider;
import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.framework.library.DownloadableLibraryType;
import icons.DoradoIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-25
 * Time: 上午12:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class DoradoFrameworkType extends FrameworkTypeEx{

    protected DoradoFrameworkType(String id) {
        super(id);
    }

    @NotNull
    @Override
    public FrameworkSupportInModuleProvider createProvider() {
        return new DoradoAddonUnderlyingFrameworkSupportProviderBase(this, getLibraryTypeClass());
    }

    protected abstract Class<? extends DownloadableLibraryType> getLibraryTypeClass();


    @NotNull
    @Override
    public Icon getIcon() {
        return DoradoIcons.DORADO7_ICON;
    }

    @Nullable
    @Override
    public String getUnderlyingFrameworkTypeId() {
        return FacetBasedFrameworkSupportProvider.getProviderId(DoradoFacet.ID);
    }
}
