package com.bstek.designer.project.view;

import com.bstek.designer.project.model.View;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.BasePsiNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import icons.DoradoIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-1
 * Time: 下午5:29
 * To change this template use File | Settings | File Templates.
 */
public class ViewNode extends ProjectViewNode<View> {

    private final Collection<BasePsiNode<? extends PsiElement>> children;

    public ViewNode(Project project, Object value, ViewSettings viewSettings) {
        this(project, (View) value, viewSettings, getChildren(project, (View) value, viewSettings));
    }

    public ViewNode(Project project, View value, ViewSettings viewSettings, Collection<BasePsiNode<? extends PsiElement>> children) {
        super(project, value, viewSettings);
        this.children = children;
    }


    @Override
    public boolean contains(@NotNull VirtualFile file) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public Collection<? extends AbstractTreeNode> getChildren() {
        return children;
    }

    @Override
    protected void update(PresentationData presentation) {
        if (getValue() == null) {
            setValue(null);
        } else {
            presentation.setPresentableText(getValue().getName());
            presentation.setIcon(DoradoIcons.DORADO7_VIEW_FILE);
        }
    }

    private static Collection<BasePsiNode<? extends PsiElement>> getChildren(final Project project, final View view, final ViewSettings settings) {
        final Set<BasePsiNode<? extends PsiElement>> children = new LinkedHashSet<BasePsiNode<? extends PsiElement>>();
        children.add(new PsiFileNode(project, view.getViewFile(), settings));
        children.add(new PsiFileNode(project, view.getJsControllerToBind(), settings));
        return children;
    }
}