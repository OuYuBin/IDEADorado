/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.bstek.designer.editor.xml;

import com.intellij.openapi.project.Project;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;


public class ViewResourceFactoryImpl extends XMIResourceFactoryImpl {


    Project project;

    /**
     * Constructor for XMIResourceFactoryImpl.
     */
    public ViewResourceFactoryImpl(Project project) {
        super();
        this.project = project;
    }

    public Resource createResource(URI uri) {
        return new ViewXMLResourceImpl(uri, project);
    }

} 
