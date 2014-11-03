package com.bstek.designer.common.xml;

import java.util.Iterator;
import java.util.Map;

import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.FeatureNotFoundException;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXXMLHandler;

/**
 * @author Robin
 */
public abstract class DoradoSAXXMLHandler extends SAXXMLHandler {

    public DoradoSAXXMLHandler(XMLResource xmiResource, XMLHelper helper,
                               Map options) {
        super(xmiResource, helper, options);
    }

    public Dorado7RulesConfigImpl getConfig() {
        return ((DoradoXMLHelperImpl) helper).getConfig();
    }

    protected EStructuralFeature getFeatureByOtherStrategy(EObject eOwner,
                                                           String prefix, String name, boolean b) {
        return getFeature(eOwner, prefix, eOwner.eClass().getName()
                + "-Element", true);
    }

    protected String getFeatureNameByTagName(EObject eOwner, String tagName) {
        // 默认实现返回节点的名称
        return tagName;
    }

    // --覆写改方法,确保始终能够找到对应Feature,将标记节点名称传入创建对象方法,以便提供对应的超类处理
    protected void handleFeature(String prefix, String name) {
        EObject peekObject = objects.peekEObject();

        // This happens when processing an element with simple content that has
        // elements content even though it shouldn't.
        //
        if (peekObject == null) {
            types.push(ERROR_TYPE);
            error(new FeatureNotFoundException(name, null, getLocation(),
                    getLineNumber(), getColumnNumber()));
            return;
        }
        // --先根据name查找是否存在相关feature，如果没有再添加指定的前缀查找是否存在该feature
        EStructuralFeature feature = getFeature(peekObject, prefix, name, true);
        // if (name.equals("Columns")
        // && peekObject.eClass().getName().equals("Portal")) {
        // System.out.println(name);
        // System.out.println(feature.getEType());
        //
        // }
        // if (name.equals("ToolBarButton")
        // && peekObject.eClass().getName().equals("Buttons")) {
        // //System.out.println(objects);
        // System.out.println("---" + peekObject.eClass());
        // // System.out.println(feature.getEType());
        // }
        // --如果不存在对应的feature，接着根据name+“-Element”查找是否存在该feature
        if (feature == null) {
            feature = getFeatureByOtherStrategy(peekObject, prefix, name, true);
        }

        if (feature != null) {
            int kind = helper.getFeatureKind(feature);
            if (kind == XMLHelper.DATATYPE_SINGLE
                    || kind == XMLHelper.DATATYPE_IS_MANY) {
                objects.push(null);
                mixedTargets.push(null);
                types.push(feature);
                if (!isNull()) {
                    text = new StringBuffer();
                }
            } else if (extendedMetaData != null) {
                EReference eReference = (EReference) feature;
                boolean isContainment = eReference.isContainment();
                if (!isContainment
                        && !eReference.isResolveProxies()
                        && extendedMetaData.getFeatureKind(feature) != ExtendedMetaData.UNSPECIFIED_FEATURE) {
                    isIDREF = true;
                    objects.push(null);
                    mixedTargets.push(null);
                    types.push(feature);
                    text = new StringBuffer();
                } else {
                    createObject(peekObject, feature);
                    EObject childObject = objects.peekEObject();
                    if (childObject != null) {
                        if (isContainment) {
                            EStructuralFeature simpleFeature = extendedMetaData
                                    .getSimpleFeature(childObject.eClass());
                            if (simpleFeature != null) {
                                isSimpleFeature = true;
                                isIDREF = simpleFeature instanceof EReference;
                                objects.push(null);
                                mixedTargets.push(null);
                                types.push(simpleFeature);
                                text = new StringBuffer();
                            }
                        } else if (!childObject.eIsProxy()) {
                            text = new StringBuffer();
                        }
                    }
                }
            } else {
                // if (name.equals("Columns")
                // && peekObject.eClass().getName().equals("Portal")) {
                // System.out.println("debug");
                // }
                //
                createObject(peekObject, feature, name);
            }
        } else {
            // Try to get a general-content feature.
            // Use a pattern that's not possible any other way.
            //
            if (xmlMap != null
                    && (feature = getFeature(peekObject, null, "", true)) != null) {

                EFactory eFactory = getFactoryForPrefix(prefix);

                // This is for the case for a local unqualified element that has
                // been bound.
                //
                if (eFactory == null) {
                    eFactory = feature.getEContainingClass().getEPackage()
                            .getEFactoryInstance();
                }

                EObject newObject = null;
                if (useNewMethods) {
                    newObject = createObject(eFactory, feature.getEType(),
                            false);
                } else {
                    newObject = createObjectFromFactory(eFactory, name);
                }
                newObject = validateCreateObjectFromFactory(eFactory, name,
                        newObject, feature);
                if (newObject != null) {
                    setFeatureValue(peekObject, feature, newObject);
                }
                processObject(newObject);
            } else {
                // This handles the case of a substitution group.
                //
                if (xmlMap != null) {
                    EFactory eFactory = getFactoryForPrefix(prefix);
                    EObject newObject = createObjectFromFactory(eFactory, name);
                    validateCreateObjectFromFactory(eFactory, name, newObject);
                    if (newObject != null) {
                        for (Iterator i = peekObject.eClass()
                                .getEAllReferences().iterator(); i.hasNext(); ) {
                            EReference eReference = (EReference) i.next();
                            if (eReference.getEType().isInstance(newObject)) {
                                setFeatureValue(peekObject, eReference,
                                        newObject);
                                processObject(newObject);
                                return;
                            }
                        }
                    }
                }

                handleUnknownFeature(prefix, name, true, peekObject, null);
            }
        }
    }

    protected void createObject(EObject peekObject, EStructuralFeature feature,
                                String tagName) {
        if (isNull()) {
            setFeatureValue(peekObject, feature, null);
            objects.push(null);
            mixedTargets.push(null);
            types.push(OBJECT_TYPE);
        } else {
            // --改成不是根据xsi type来找子类，而是根据自定义的type来找对应的子类
            String type = ((EClass) feature.getEType()).isAbstract() ? getFeatureNameByTagName(
                    peekObject, tagName) : null;
            type = type == null ? getXSIType() : type;
            if (type != null) {
                createObjectFromTypeName(peekObject, type, feature);
            } else {
                createObjectFromFeatureType(peekObject, feature);
                // This check is redundant -- see handleFeature method (EL)
                /*
                 * if (extendedMetaData != null &&
				 * !((EReference)feature).isContainment()) { text = new
				 * StringBuffer(); }
				 */
                if (xmlMap != null && !((EReference) feature).isContainment()) {
                    XMLResource.XMLInfo info = xmlMap.getInfo(feature);
                    if (info != null
                            && info.getXMLRepresentation() == XMLResource.XMLInfo.ELEMENT) {
                        text = new StringBuffer();
                    }
                }
            }
        }
    }

    protected void handleObjectAttribs(EObject obj) {
        if (attribs != null) {
            InternalEObject internalEObject = (InternalEObject) obj;
            for (int i = 0, size = attribs.getLength(); i < size; ++i) {
                String name = attribs.getQName(i);
                if (name.equals(idAttribute)) {
                    xmlResource.setID(internalEObject, attribs.getValue(i));
                } else if (name.equals(hrefAttribute)
                        && (!recordUnknownFeature || types.peek() != UNKNOWN_FEATURE_TYPE)) {
                    handleProxy(internalEObject, attribs.getValue(i));
                } else if (!name.startsWith(XMLResource.XML_NS)
                        && !notFeatures.contains(name)) {
                    String value = attribs.getValue(i);
                    setAttribValue(obj, name, value);
                }
            }
        }
    }

    // --覆写该方法,使用推荐的createObject方法构建对象
    protected EObject createObjectFromFeatureType(EObject peekObject,
                                                  EStructuralFeature feature) {
        String typeName = null;
        EFactory factory = null;
        EClassifier eType = null;
        EObject obj = null;

        if (feature != null && (eType = feature.getEType()) != null) {
            if (useNewMethods) {
                if (extendedMetaData != null
                        && eType == EcorePackage.Literals.EOBJECT
                        && extendedMetaData.getFeatureKind(feature) != ExtendedMetaData.UNSPECIFIED_FEATURE) {
                    eType = anyType;
                    typeName = extendedMetaData.getName(anyType);
                    factory = anyType.getEPackage().getEFactoryInstance();
                } else {
                    factory = eType.getEPackage().getEFactoryInstance();
                    typeName = extendedMetaData == null ? eType.getName()
                            : extendedMetaData.getName(eType);
                }
                obj = createObject(factory, eType, false);
            } else {

                if (extendedMetaData != null
                        && eType == EcorePackage.Literals.EOBJECT
                        && extendedMetaData.getFeatureKind(feature) != ExtendedMetaData.UNSPECIFIED_FEATURE) {
                    typeName = extendedMetaData.getName(anyType);
                    factory = anyType.getEPackage().getEFactoryInstance();
                } else {
                    EClass eClass = (EClass) eType;
                    typeName = extendedMetaData == null ? eClass.getName()
                            : extendedMetaData.getName(eClass);
                    factory = eClass.getEPackage().getEFactoryInstance();
                }
                // --通过覆写该处不再使用deprecated的createObjectFromFactory方法来构建对象
                obj = createObject(factory, eType, false);
            }
        }

        obj = validateCreateObjectFromFactory(factory, typeName, obj, feature);

        if (obj != null) {
            setFeatureValue(peekObject, feature, obj);
        }

        processObject(obj);
        return obj;
    }

    protected EObject createObjectFromFactory(EFactory factory, String typeName) {
        EObject newObject = null;

        if (factory != null) {
            newObject = helper.createObject(factory, typeName);

            if (newObject != null) {
                if (disableNotify)
                    newObject.eSetDeliver(false);

                handleObjectAttribs(newObject);
            }
        }
        return newObject;
    }


}
