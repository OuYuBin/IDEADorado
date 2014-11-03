package com.bstek.designer.common.model;

import com.intellij.openapi.util.Iconable;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileDescription;
import icons.DoradoIcons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-29
 * Time: 上午12:08
 * To change this template use File | Settings | File Templates.
 */
public class ViewDomFileDescription extends DomFileDescription {
    public ViewDomFileDescription() {
        super(DomElement.class, "ViewConfig", new String[0]);
    }

    @Nullable
    @Override
    public Icon getFileIcon(@Iconable.IconFlags int flags) {
        return DoradoIcons.DORADO7_VIEW_FILE;
    }

    @Override
    public boolean hasStubs() {
        return true;
    }
}
