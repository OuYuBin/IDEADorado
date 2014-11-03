package com.bstek.designer.editor.palette;

import com.bstek.designer.common.palette.DoradoCommonPaletteProvider;
import com.intellij.ide.palette.PaletteGroup;
import com.intellij.openapi.project.Project;
import com.intellij.util.xml.DomFileDescription;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-11-13
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */
public class ViewPaletteProvider extends DoradoCommonPaletteProvider {

    public ViewPaletteProvider(Project project) {
        super(project);
    }

    @Override
    public PaletteGroup[] getActiveGroups(DomFileDescription domFileDescription) {
        return super.getActiveGroups(domFileDescription);
    }
}
