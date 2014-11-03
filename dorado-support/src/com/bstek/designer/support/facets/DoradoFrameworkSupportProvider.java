package com.bstek.designer.support.facets;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-23
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */

import com.bstek.designer.support.facets.utils.AddDoradoSupportUtil;
import com.bstek.designer.support.library.DoradoLibraryType;
import com.intellij.facet.ui.FacetBasedFrameworkSupportProvider;
import com.intellij.framework.library.DownloadableLibraryService;
import com.intellij.framework.library.FrameworkSupportWithLibrary;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportConfigurableBase;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportProviderBase;
import com.intellij.ide.util.frameworkSupport.FrameworkVersion;
import com.intellij.javaee.web.WebRoot;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.libraries.CustomLibraryDescription;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VfsUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DoradoFrameworkSupportProvider extends FacetBasedFrameworkSupportProvider<DoradoFacet> {

    public DoradoFrameworkSupportProvider() {
        super(DoradoFacetType.INSTANCE);
    }

    @Override
    public String getTitle() {
        return "Dorado 7";
    }

    @Override
    protected void setupConfiguration(DoradoFacet doradoFacet, ModifiableRootModel modifiableRootModel, FrameworkVersion frameworkVersion) {
    }

    @Override
    protected void onFacetCreated(final DoradoFacet facet, ModifiableRootModel rootModel, FrameworkVersion version) {
        final WebFacet webFacet = facet.getWebFacet();
        WebRoot webRoot = webFacet.getWebRoots().get(0);
        final String webRootPath = VfsUtil.urlToPath(webRoot.getDirectoryUrl());
        final Project project = facet.getModule().getProject();
        StartupManager.getInstance(project).runWhenProjectIsInitialized(new Runnable() {
            @Override
            public void run() {
                AddDoradoSupportUtil.createDoradoSupportFilesAction(facet, project, webRootPath);
            }
        });
    }

    @NotNull
    @Override
    public FrameworkSupportConfigurableBase createConfigurable(@NotNull FrameworkSupportModel model) {
        if (model != null) {
            return new DoradoFrameworkSupportConfigurable(this, model);
        }
        return super.createConfigurable(model);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private static class DoradoFrameworkSupportConfigurable extends FrameworkSupportConfigurableBase implements FrameworkSupportWithLibrary {

        public DoradoFrameworkSupportConfigurable(FrameworkSupportProviderBase frameworkSupportProvider, FrameworkSupportModel model) {
            super(frameworkSupportProvider, model);
        }


        @Nullable
        @Override
        public CustomLibraryDescription createLibraryDescription() {
            return DownloadableLibraryService.getInstance().createDescriptionForType(DoradoLibraryType.class);
        }

        @Override
        public boolean isLibraryOnly() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
