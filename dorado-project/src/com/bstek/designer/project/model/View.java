package com.bstek.designer.project.model;

import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-30
 * Time: 下午4:18
 * To change this template use File | Settings | File Templates.
 */
public class View implements Navigatable {

    public static final DataKey<View[]> DATA_KEY = DataKey.create("view");

    //--PSI(Program Structure Interface)
    private final XmlFile viewFile;
    private final PsiFile jsControllerToBind;

    public View(XmlFile viewFile) {
        this.viewFile = viewFile;
        this.jsControllerToBind = null;
    }

    public View(XmlFile viewFile, PsiFile jsControllerToBind) {
        this.viewFile = viewFile;
        this.jsControllerToBind = jsControllerToBind;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (viewFile != null && viewFile.canNavigate()) {
            viewFile.navigate(requestFocus);
        }
    }

    public String getName() {
        return viewFile.getName().substring(0, viewFile.getName().indexOf("."));
    }

    @Override
    public boolean canNavigate() {
        if (viewFile != null && viewFile.canNavigate()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        if (viewFile != null && viewFile.canNavigate()) {
            return true;
        }
        return false;
    }

    public PsiFile getJsControllerToBind() {
        return jsControllerToBind;
    }

    public XmlFile getViewFile() {
        return viewFile;
    }
}
