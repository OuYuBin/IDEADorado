package com.bstek.designer.project.view;

import com.bstek.designer.common.model.ViewDomFileDescription;
import com.bstek.designer.project.binding.ViewJsControllerIndex;
import com.bstek.designer.project.model.View;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.BasePsiNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import com.intellij.util.xml.impl.DomManagerImpl;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-30
 * Time: 下午4:13
 * To change this template use File | Settings | File Templates.
 */
public class ViewMergerTreeStructureProvider implements TreeStructureProvider {

    private final Project project;

    public ViewMergerTreeStructureProvider(Project project) {
        this.project = project;
    }

    @Override
    public Collection<AbstractTreeNode> modify(AbstractTreeNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {
        if (parent.getValue() instanceof View) return children;
        boolean jsControllerFound = false;
        for (AbstractTreeNode node : children) {
            if ((node.getValue() instanceof PsiFile)) {
                PsiFile file = (PsiFile) node.getValue();
                if (file.getName().contains(".js")) {
                    jsControllerFound = true;
                    break;
                }
            }
        }
        if (!jsControllerFound) return children;

        //--获取Dorado View文件的PsiFile对象
        Collection result = new LinkedHashSet(children);
        ProjectViewNode[] copy = (ProjectViewNode[]) children.toArray(new ProjectViewNode[children.size()]);
        for (ProjectViewNode element : copy) {
            PsiFile viewPsiFile = null;
            if (element.getValue() instanceof XmlFile) {
                DomFileDescription domFileDescription = DomManagerImpl.getDomManager(project).getDomFileDescription(((XmlFile) element.getValue()));
                if (domFileDescription instanceof ViewDomFileDescription) {
                    viewPsiFile = (PsiFile) element.getValue();
                }
            }

            //--获取Dorado View文件对应的JsCtronller对象
            if (viewPsiFile != null) {
                String qName = viewPsiFile.getName().substring(0, viewPsiFile.getName().indexOf(".view.xml"));
                if (qName != null) {
                    List jsControllers;
                    try {
                        //--获取项目下所有与Dorado View文件名相同的Js Controller对象
                        jsControllers = ViewJsControllerIndex.findJsControllerBoundToView(this.project, qName);
                    } catch (ProcessCanceledException e) {
                        continue;
                    }
                    //--在当前节点下的子中寻找PsiNode对象
                    Collection jsControllerNodes = findFormsIn(children, jsControllers);
                    if (!jsControllerNodes.isEmpty()) {
                        Collection jsControllerFiles = convertToFiles(jsControllerNodes);
                        Collection subNodes = new ArrayList();

                        subNodes.add((BasePsiNode) element);
                        subNodes.addAll(jsControllerNodes);
                        result.add(new ViewNode(project, new View((XmlFile) viewPsiFile, (PsiFile) jsControllerFiles.toArray()[0]), settings, subNodes));
                        result.remove(element);
                        result.removeAll(jsControllerNodes);
                    }
                }
            }
        }
        return result;
    }

    private static Collection<BasePsiNode<? extends PsiElement>> findFormsIn(Collection<AbstractTreeNode> children, List<PsiFile> forms) {
        if ((children.isEmpty()) || (forms.isEmpty())) return Collections.emptyList();
        ArrayList result = new ArrayList();
        HashSet psiFiles = new HashSet(forms);
        for (AbstractTreeNode child : children) {
            if ((child instanceof BasePsiNode)) {
                BasePsiNode treeNode = (BasePsiNode) child;

                if (psiFiles.contains(treeNode.getValue())) result.add(treeNode);
            }
        }
        return result;
    }

    private static Collection<PsiFile> convertToFiles(Collection<BasePsiNode<? extends PsiElement>> formNodes) {
        ArrayList psiFiles = new ArrayList();
        for (AbstractTreeNode treeNode : formNodes) {
            psiFiles.add((PsiFile) treeNode.getValue());
        }
        return psiFiles;
    }

    @Nullable
    @Override
    public Object getData(Collection<AbstractTreeNode> selected, String dataId) {
        if (selected != null) {
            if (View.DATA_KEY.is(dataId)) {
                List<View> result = new ArrayList<View>();
                for (AbstractTreeNode node : selected) {
                    if (node.getValue() instanceof View) {
                        result.add((View) node.getValue());
                    }
                }
                if (!result.isEmpty()) {
                    return result.toArray(new View[result.size()]);
                }
            } else if (PlatformDataKeys.DELETE_ELEMENT_PROVIDER.is(dataId)) {
                return null;
            }
        }
        return null;
    }
}
