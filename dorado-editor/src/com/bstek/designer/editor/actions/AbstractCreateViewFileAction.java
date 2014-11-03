package com.bstek.designer.editor.actions;

import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.text.StringUtil;

import javax.swing.*;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-6
 * Time: 下午10:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCreateViewFileAction extends CreateElementActionBase implements DumbAware {

    public AbstractCreateViewFileAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    public void update(final AnActionEvent event) {
        super.update(event);
    }


    protected String createViewBody(String viewName) {
        final InputStream inputStream = getClass().getResourceAsStream(viewName);
        final StringBuffer buffer = new StringBuffer();
        try {
            for (int ch; (ch = inputStream.read()) != -1; ) {
                buffer.append((char) ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }

        String s = buffer.toString();
        return StringUtil.convertLineSeparators(s);

    }
}
