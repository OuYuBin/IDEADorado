package com.bstek.designer.project.binding;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-1
 * Time: 下午4:52
 * To change this template use File | Settings | File Templates.
 */
public class ViewJsControllerIndex extends ScalarIndexExtension<String> {

    public static final ID<String, Void> NAME = ID.create("JsControllerIndex");

    private final ViewJsControllerDataIndexer viewJsControllerDataIndexer = new ViewJsControllerDataIndexer();

    private final EnumeratorStringDescriptor keyDescriptor = new EnumeratorStringDescriptor();

    private final ViewJsControllerInputFilter viewInputFilter = new ViewJsControllerInputFilter();


    public static List<PsiFile> findJsControllerBoundToView(Project project, String name) {
        return findJsControllerBoundToView(project, name, ProjectScope.getAllScope(project));
    }

    public static List findJsControllerBoundToView(final Project project, final String jsName, final GlobalSearchScope scope)

    {
        return (List) ApplicationManager.getApplication().runReadAction(new Computable() {
            public List<PsiFile> compute() {
                Collection<VirtualFile> files;

                try {
                    files = FileBasedIndex.getInstance().getContainingFiles(NAME, jsName, GlobalSearchScope.projectScope(project).intersectWith(scope));
                } catch (IndexNotReadyException e) {
                    return Collections.emptyList();
                }
                if (files.isEmpty()) return Collections.emptyList();
                List result = new ArrayList();
                for (VirtualFile file : files) {
                    if (file.isValid()) {
                        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                        if (psiFile != null)
                            result.add(psiFile);
                    }
                }
                return result;
            }
        });
    }

    @NotNull
    @Override
    public ID getName() {
        return NAME;
    }

    @NotNull
    @Override
    public DataIndexer getIndexer() {
        return viewJsControllerDataIndexer;
    }

    @Override
    public KeyDescriptor getKeyDescriptor() {
        return keyDescriptor;
    }

    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return this.viewInputFilter;
    }

    @Override
    public boolean dependsOnFileContent() {
        return false;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    private static class ViewJsControllerInputFilter
            implements FileBasedIndex.InputFilter {
        public boolean acceptInput(VirtualFile file) {
            if (file.getExtension() != null)
                return file.getExtension().equals("js");
            return false;
        }
    }

    private static class ViewJsControllerDataIndexer implements DataIndexer<String, Void, FileContent> {
        @Override
        @NotNull
        public Map<String, Void> map(final FileContent inputData) {
            if (inputData.getFile().getExtension() != null) {
                if (inputData.getFile().getExtension().equals("js")) {

                    return Collections.singletonMap(inputData.getFileName().substring(0, inputData.getFileName().indexOf(".")), null);
                }
            }
            return Collections.emptyMap();
        }
    }
}
