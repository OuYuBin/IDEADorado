package com.bstek.designer.resources;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-25
 * Time: 下午10:59
 * To change this template use File | Settings | File Templates.
 */
public class DoradoResource {

    public static final File DORADO7_RESOURCE=loadFile("dorado7");


    public static File loadFile(String facetName){
        File file=null;
        IdeaPluginDescriptor ideaPluginDescriptor=PluginManager.getPlugin(PluginId.getId("com.bstek.designer.resource"));
        ClassLoader classLoader=ideaPluginDescriptor.getPluginClassLoader();
        try {
            URL url=classLoader.getResource(facetName);
            file=new File(url.toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return file;
    }
}
