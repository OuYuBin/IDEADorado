package com.bstek.designer.editor.actions;

import com.intellij.ide.actions.CreateFileAction;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import icons.DoradoIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-29
 * Time: 下午4:22
 * To change this template use File | Settings | File Templates.
 */
public class CreateTouchFileAction extends CreateFileAction {
    public CreateTouchFileAction() {
        super("Dorado7 Touch", "Create new Dorado7 Touch File", DoradoIcons.DORADO7_TOUCH_FILE);
    }

    @NotNull
    @Override
    protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
        return super.create(newName, directory);
    }

    @Nullable
    @Override
    protected String getDefaultExtension() {
        return "touch.xml";
    }
}
