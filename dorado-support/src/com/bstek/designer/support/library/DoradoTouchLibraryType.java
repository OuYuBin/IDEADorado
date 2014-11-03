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
public class DoradoTouchLibraryType extends DownloadableLibraryTypeBase {

    public DoradoTouchLibraryType(){
        super("Dorado7 Touch","dorado7-touch","dorado7-touch", DoradoIcons.DORADO7_TOUCH_ICON,new URL[]{});
    }

    @Override
    protected String[] getDetectionClassNames() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
