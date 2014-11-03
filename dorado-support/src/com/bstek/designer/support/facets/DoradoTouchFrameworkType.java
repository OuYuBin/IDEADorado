package com.bstek.designer.support.facets;

import com.bstek.designer.support.library.DoradoTouchLibraryType;
import com.intellij.framework.library.DownloadableLibraryType;
import icons.DoradoIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-25
 * Time: 上午2:30
 * To change this template use File | Settings | File Templates.
 */
public class DoradoTouchFrameworkType extends DoradoFrameworkType {

    protected DoradoTouchFrameworkType() {
        super("dorado7-touch");
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Touch";
    }

    @Override
    protected Class<? extends DownloadableLibraryType> getLibraryTypeClass() {
        return DoradoTouchLibraryType.class;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return DoradoIcons.DORADO7_TOUCH_ICON;
    }
}
