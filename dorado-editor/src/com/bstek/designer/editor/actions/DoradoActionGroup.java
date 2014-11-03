package com.bstek.designer.editor.actions;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import icons.DoradoIcons;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-29
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class DoradoActionGroup extends DefaultActionGroup {

    public DoradoActionGroup() {
        super("Dorado7", true);
        getTemplatePresentation().setDescription("Dorado7");
        getTemplatePresentation().setIcon(DoradoIcons.DORADO7_ICON);
    }
}
