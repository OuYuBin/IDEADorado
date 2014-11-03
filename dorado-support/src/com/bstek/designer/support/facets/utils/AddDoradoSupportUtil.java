package com.bstek.designer.support.facets.utils;

import com.bstek.designer.resources.DoradoResource;
import com.bstek.designer.support.facets.DoradoFacet;
import com.intellij.facet.Facet;
import com.intellij.javaee.web.WebRoot;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-25
 * Time: 下午10:14
 * To change this template use File | Settings | File Templates.
 */
public class AddDoradoSupportUtil {

    public static void createDoradoSupportFilesAction(final Facet facet, Project project, final String configFilePath) {
        String name = facet.getTypeId().toString();
        File file = DoradoResource.loadFile(name);
        if (facet instanceof DoradoFacet) {
            WebFacet webFacet = ((DoradoFacet) facet).getWebFacet();
            WebRoot webRoot = webFacet.getWebRoots().get(0);
            String webRootPath = VfsUtil.urlToPath(webRoot.getDirectoryUrl());
            if (file.exists()) {
                File webFile = new File(file.getAbsolutePath() + "/web");
                if (webFile.exists())
                    createDoradoSupportWebFiles(webFile, new File(webRootPath));
            }
        }

    }

    private static void createDoradoSupportWebFiles(File fromFile, File toFile) {
        try {
            if (fromFile.isDirectory()) {
                createDoradoSupportFiles(fromFile, toFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void createDoradoSupportFiles(File fromFile, File toFile) {
        try {
            if (fromFile.isDirectory()) {
                FileUtil.copyDirContent(fromFile, toFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
