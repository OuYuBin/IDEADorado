package com.bstek.designer.common.xml;

import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import com.bstek.designer.core.config.DoradoConfigRulesModelMeta;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl;
import org.eclipse.emf.ecore.xml.type.SimpleAnyType;


/**
 * @author robin
 */
public class DoradoXMLHelperImpl extends XMLHelperImpl {

    private Object previousNS;
    private EPackage previousPackage;

    public DoradoXMLHelperImpl() {
        super();
    }

    public DoradoXMLHelperImpl(XMLResource resource) {
        this();
        setResource(resource);
    }

    public Dorado7RulesConfigImpl getConfig() {
        return ((DoradoResourceImpl) resource).getConfig();
    }

    //
    public String getName(ENamedElement obj) {
        if (extendedMetaData != null) {
            return obj instanceof EStructuralFeature ? extendedMetaData
                    .getName((EStructuralFeature) obj) : extendedMetaData
                    .getName((EClassifier) obj);
        }

        if (xmlMap != null) {
            XMLResource.XMLInfo info = xmlMap.getInfo(obj);
            if (info != null) {
                String result = info.getName();
                if (result != null) {
                    return result;
                }
            }
        }
        String tagName = null;

        EClassifier eClassifier = null;
        if (obj instanceof EClassifier) {
            eClassifier = (EClassifier) obj;
        } else if (obj instanceof EReference) {
            eClassifier = ((EReference) obj).getEType();
        }
        DoradoConfigRulesModelMeta modelMeta = null;
        if (eClassifier instanceof EClass) {
            modelMeta = getConfig().getMetaOfModel(
                    (EClass) eClassifier);
        } else if (eClassifier instanceof EObject) {
            modelMeta = getConfig().getMetaOfModel(
                    (EObject) eClassifier);
        }
        if (modelMeta != null) {
            try {
                tagName = modelMeta.getNodeName();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return tagName == null ? obj.getName() : tagName;
    }

    public String getQName(EStructuralFeature feature) {
        if (extendedMetaData != null) {
            String namespace = extendedMetaData.getNamespace(feature);
            String name = extendedMetaData.getName(feature);
            String result = name;

            // We need to be careful that we don't end up requiring the no
            // namespace package
            // just because the feature is unqualified.
            //
            if (namespace != null) {
                // There really must be a package.
                //
                EPackage ePackage;
                if (namespace.equals(previousNS)) {
                    ePackage = previousPackage;
                } else {
                    ePackage = extendedMetaData.getPackage(namespace);
                    if (ePackage == null) {
                        ePackage = extendedMetaData.demandPackage(namespace);
                    }
                    previousPackage = ePackage;
                    previousNS = namespace;
                }

                result = getQName(ePackage, name);

                // We must have a qualifier for an attribute that needs
                // qualified.
                //
                if (result.length() == name.length()
                        && extendedMetaData.getFeatureKind(feature) == ExtendedMetaData.ATTRIBUTE_FEATURE) {
                    result = getQName(ePackage, name, true);
                }
            }
            return result;
        }

        String name = getName(feature);
        if (xmlMap != null) {
            XMLResource.XMLInfo info = xmlMap.getInfo(feature);
            if (info != null) {
                return getQName(info.getTargetNamespace(), name);
            }
        }

        return name;
    }

    public EObject createObject(EFactory eFactory, EClassifier type) {
        EObject newObject = null;
        if (eFactory != null) {
            if (extendedMetaData != null) {
                if (type == null) {
                    return null;
                } else if (type instanceof EClass) {
                    EClass eClass = (EClass) type;
                    if (!eClass.isAbstract()) {
                        newObject = eFactory.create((EClass) type);
                    }
                } else {
                    SimpleAnyType result = (SimpleAnyType) EcoreUtil
                            .create(anySimpleType);
                    result.setInstanceType((EDataType) type);
                    newObject = result;
                }
            } else {
                if (type != null) {
                    EClass eClass = (EClass) type;
                    if (!eClass.isAbstract()) {
                        newObject = eFactory.create((EClass) type);
                    }
                }
            }
        }
        return newObject;
    }

}
