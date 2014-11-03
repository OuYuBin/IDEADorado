package com.bstek.designer.editor.xml;

import com.bstek.designer.common.xml.DoradoResourceSetImpl;
import com.intellij.openapi.project.Project;
import org.eclipse.emf.ecore.resource.Resource.Factory;


/**
 * @author Robin
 */

public class ViewResourceSetImpl extends DoradoResourceSetImpl {

    public ViewResourceSetImpl(Project project) {
        super(project);
    }

    protected Factory getResourceFactory() {
        return new ViewResourceFactoryImpl(project);
    }

}
