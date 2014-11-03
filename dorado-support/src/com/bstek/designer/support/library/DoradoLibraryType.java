package com.bstek.designer.support.library;

import com.intellij.framework.library.DownloadableLibraryTypeBase;
import icons.DoradoIcons;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-25
 * Time: 上午12:42
 * To change this template use File | Settings | File Templates.
 */
public class DoradoLibraryType extends DownloadableLibraryTypeBase {

    public DoradoLibraryType(){
        super("Dorado7", "dorado7","dorado7", DoradoIcons.DORADO7_ICON,new URL[]{ });
    }

    @Override
    protected String[] getDetectionClassNames() {
        return new String[0];
    }
}
