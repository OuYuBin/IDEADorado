package com.bstek.designer.support.facets;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-23
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */

import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.framework.library.DownloadableLibraryService;
import com.intellij.framework.library.DownloadableLibraryType;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.libraries.CustomLibraryDescription;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DoradoAddonUnderlyingFrameworkSupportProviderBase extends FrameworkSupportInModuleProvider {

    private final FrameworkTypeEx frameworkType;
    private final Class<? extends DownloadableLibraryType> libraryTypeClass;

    public FrameworkTypeEx getFrameworkType() {
        return frameworkType;
    }


    public DoradoAddonUnderlyingFrameworkSupportProviderBase(FrameworkTypeEx frameworkType, Class<? extends DownloadableLibraryType> libraryTypeClass) {
        this.frameworkType = frameworkType;
        this.libraryTypeClass = libraryTypeClass;
    }


    public FrameworkSupportInModuleConfigurable createConfigurable(@NotNull final FrameworkSupportModel model) {
        return new DoradoAddonLibrarySupportConfigurable();
    }

    @Override
    public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
        return moduleType instanceof JavaModuleType;
    }

    private class DoradoAddonLibrarySupportConfigurable extends FrameworkSupportInModuleConfigurable {
        @Override
        public JComponent createComponent() {
            return null;
        }

        @Override
        public void addSupport(@NotNull Module module,
                               @NotNull ModifiableRootModel rootModel,
                               @NotNull ModifiableModelsProvider modifiableModelsProvider) {
        }

        @NotNull
        @Override
        public CustomLibraryDescription createLibraryDescription() {
            return DownloadableLibraryService.getInstance().createDescriptionForType(libraryTypeClass);
        }

        @Override
        public boolean isOnlyLibraryAdded() {
            return true;
        }
    }
}
