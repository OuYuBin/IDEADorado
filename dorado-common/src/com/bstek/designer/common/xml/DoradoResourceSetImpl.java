package com.bstek.designer.common.xml;

import com.intellij.openapi.project.Project;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author robin(mailto:robin.ou@bstek.com)
 */
public abstract class DoradoResourceSetImpl extends ResourceSetImpl {

    public Project project;

    public DoradoResourceSetImpl() {
        super();
    }

    public DoradoResourceSetImpl(Project project) {
        this();
        this.project = project;
    }

    public Resource createResource(URI uri) {

        Resource.Factory resourceFactory = getResourceFactory();
        if (resourceFactory != null) {
            Resource result = resourceFactory.createResource(uri);
            getResources().add(result);
            return result;
        } else {
            return null;
        }
    }

    protected abstract Resource.Factory getResourceFactory();
}