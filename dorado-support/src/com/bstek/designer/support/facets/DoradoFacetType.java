package com.bstek.designer.support.facets;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.ui.DefaultFacetSettingsEditor;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import icons.DoradoIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-23
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class DoradoFacetType extends FacetType<DoradoFacet,DoradoFacetConfiguration> {


    public static final DoradoFacetType INSTANCE = new DoradoFacetType();

    public DoradoFacetType(){
        super(DoradoFacet.ID, "dorado7","Dorado 7", WebFacet.ID);
    }

    @Override
    public Icon getIcon() {
        return DoradoIcons.DORADO7_ICON;
    }

    @Override
    public DoradoFacetConfiguration createDefaultConfiguration() {
        return new DoradoFacetConfiguration();
    }

    @Override
    public DoradoFacet createFacet(@NotNull Module module, String name, @NotNull DoradoFacetConfiguration configuration, @Nullable Facet facet) {
        return new DoradoFacet(this,module,name,configuration,facet);
    }

    @Override
    public boolean isSuitableModuleType(ModuleType moduleType) {
        return moduleType instanceof JavaModuleType;
    }

    public DefaultFacetSettingsEditor createDefaultConfigurationEditor(@NotNull Project project, @NotNull DoradoFacetConfiguration configuration){
        return null;
    }

}
