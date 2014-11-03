package com.bstek.designer.common.xml;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.SimpleAnyType;
import org.w3c.dom.Node;

import java.util.Iterator;

public class DoradoXMLSaveImpl extends XMLSaveImpl {

    public DoradoXMLSaveImpl(XMLHelper helper) {
        super(helper);
    }

    protected String getContent(EObject o, EStructuralFeature[] features) {
        if (map == null) {
            return null;
        }

        for (int i = 0; i < features.length; i++) {
            EStructuralFeature feature = features[i];
            XMLResource.XMLInfo info = map.getInfo(feature);
            if (info != null
                    && info.getXMLRepresentation() == XMLResource.XMLInfo.CONTENT) {
                Object value = helper.getValue(o, feature);
                String svalue = getDatatypeValue(value, feature, false);

                if (toDOM) {
                    Node text = document.createTextNode(svalue);
                    currentNode.appendChild(text);
                    handler.recordValues(text, o, feature, value);
                }
                // else {
                // if (o.eClass().getName()
                // .equals(IDoradoElementConstants.CLIENT_EVENT)) {
                // svalue = "<![CDATA[" + svalue + "]]>";
                // }
                // else if (StringUtils.contains(svalue, "<")
                // || StringUtils.contains(svalue, ">")
                // || StringUtils.contains(svalue, "&")) {
                // svalue = "<![CDATA[" + svalue + "]]>";
                // }
                return svalue;
                // }
            }
        }
        return null;
    }

    protected void saveElement(EObject o, EStructuralFeature f) {
        EClass eClass = o.eClass();
        EClassifier eType = f.getEType();

        if (extendedMetaData != null && eClass != eType) {
            // Check if it's an anonymous type.
            //
            String name = extendedMetaData.getName(eClass);
            if (name.endsWith("_._type")) {
                String elementName = name.substring(0, name.indexOf("_._"));
                String prefix = helper.getPrefix(eClass.getEPackage());
                if (!"".equals(prefix)) {
                    elementName = prefix + ":" + elementName;
                }
                if (!toDOM) {
                    doc.startElement(elementName);
                } else {
                    currentNode = currentNode.appendChild(document
                            .createElementNS(helper.getNamespaceURI(prefix),
                                    elementName));
                    handler.recordValues(currentNode, o.eContainer(), f, o);
                }
                saveElementID(o);
                return;
            }
        }

        if (map != null) {
            XMLResource.XMLInfo info = map.getInfo(eClass);
            if (info != null
                    && info.getXMLRepresentation() == XMLResource.XMLInfo.ELEMENT) {
                if (!toDOM) {
                    String elementName = helper.getQName(eClass);
                    doc.startElement(elementName);
                } else {
                    helper.populateNameInfo(nameInfo, eClass);
                    if (currentNode == null) {
                        currentNode = document.createElementNS(
                                nameInfo.getNamespaceURI(),
                                nameInfo.getQualifiedName());
                        document.appendChild(currentNode);
                        handler.recordValues(currentNode, o.eContainer(), f, o);
                    } else {
                        currentNode = currentNode.appendChild(document
                                .createElementNS(nameInfo.getNamespaceURI(),
                                        nameInfo.getQualifiedName()));
                        handler.recordValues(currentNode, o.eContainer(), f, o);
                    }
                }
                saveElementID(o);
                return;
            }
        }
        boolean isAnyType = false;
        if (o instanceof AnyType) {
            isAnyType = true;
            helper.pushContext();
            for (Iterator i = ((AnyType) o).getAnyAttribute().iterator(); i
                    .hasNext(); ) {
                FeatureMap.Entry entry = (FeatureMap.Entry) i.next();
                if (ExtendedMetaData.XMLNS_URI.equals(extendedMetaData
                        .getNamespace(entry.getEStructuralFeature()))) {
                    String uri = (String) entry.getValue();
                    helper.addPrefix(extendedMetaData.getName(entry
                            .getEStructuralFeature()), uri == null ? "" : uri);
                }
            }
        }
        if (!toDOM) {
            String tagName;
            if (eClass != eType) {
                tagName = helper.getName(eClass);
            } else {
                tagName = helper.getQName(f);
            }
            doc.startElement(tagName);
        } else {
            helper.populateNameInfo(nameInfo, f);
            if (currentNode == null) {
                // this is a root element
                currentNode = document
                        .createElementNS(nameInfo.getNamespaceURI(),
                                nameInfo.getQualifiedName());
                document.appendChild(currentNode);
                handler.recordValues(currentNode, o.eContainer(), f, o);
            } else {
                currentNode = currentNode
                        .appendChild(document.createElementNS(
                                nameInfo.getNamespaceURI(),
                                nameInfo.getQualifiedName()));
                handler.recordValues(currentNode, o.eContainer(), f, o);
            }
        }

        if (saveTypeInfo ? xmlTypeInfo.shouldSaveType(eClass, eType, f)
                : eClass != eType && eClass != anyType) {
            if (eClass == anySimpleType) {
                saveTypeAttribute(((SimpleAnyType) o).getInstanceType());
            } else {
                saveTypeAttribute(eClass);
            }
        }

        saveElementID(o);
        if (isAnyType) {
            helper.popContext();
        }
    }

    protected Object writeTopObject(EObject top) {
        EClass eClass = top.eClass();
        if (!toDOM) {
            if (extendedMetaData == null
                    || featureTable.getDocumentRoot(eClass.getEPackage()) != eClass) {
                EStructuralFeature rootFeature = null;
                boolean shouldSaveType = false;
                if (elementHandler != null) {
                    EClassifier eClassifier = eClass == anySimpleType ? ((SimpleAnyType) top)
                            .getInstanceType() : eClass;
                    rootFeature = featureTable.getRoot(eClassifier);
                    if (rootFeature != null
                            && rootFeature.getEType() != eClassifier) {
                        shouldSaveType = true;
                    }
                }
                String name = rootFeature != null ? helper
                        .getQName(rootFeature)
                        : extendedMetaData != null && roots != null
                        && top.eContainmentFeature() != null ? helper
                        .getQName(top.eContainmentFeature()) : helper
                        .getQName(eClass);
                doc.startElement(name);
                Object mark = doc.mark();
                root = top;
                if (shouldSaveType) {
                    saveTypeAttribute(eClass);
                }
                saveElementID(top);
                return mark;
            } else {
                doc.startElement(null);
                root = top;
                saveFeatures(top);
                return null;
            }
        } else {
            if (extendedMetaData == null
                    || featureTable.getDocumentRoot(eClass.getEPackage()) != eClass) {
                EStructuralFeature rootFeature = null;
                boolean shouldSaveType = false;
                if (elementHandler != null) {
                    EClassifier eClassifier = eClass == anySimpleType ? ((SimpleAnyType) top)
                            .getInstanceType() : eClass;
                    rootFeature = featureTable.getRoot(eClassifier);
                    if (rootFeature != null
                            && rootFeature.getEType() != eClassifier) {
                        shouldSaveType = true;
                    }
                }
                if (rootFeature != null) {
                    helper.populateNameInfo(nameInfo, rootFeature);
                } else if (extendedMetaData != null && roots != null
                        && top.eContainmentFeature() != null) {
                    helper.populateNameInfo(nameInfo, top.eContainmentFeature());
                } else {
                    helper.populateNameInfo(nameInfo, eClass);
                }
                if (document.getLastChild() == null) {
                    currentNode = document.createElementNS(
                            nameInfo.getNamespaceURI(),
                            nameInfo.getQualifiedName());
                    currentNode = document.appendChild(currentNode);
                } else {
                    currentNode = currentNode.appendChild(document
                            .createElementNS(nameInfo.getNamespaceURI(),
                                    nameInfo.getQualifiedName()));
                }
                handler.recordValues(currentNode, null, null, top);
                root = top;
                if (shouldSaveType) {
                    saveTypeAttribute(eClass);
                }
                saveElementID(top);
                return null;
            } else {
                root = top;
                currentNode = document;
                saveFeatures(top);
                return null;
            }
        }
    }

    protected void saveElementID(EObject o) {
        super.saveElementID(o);
        // handlerLocator(o);
    }

    // protected void handlerLocator(EObject newObject) {
    // // getDataTypeElementSingleSimple(o, f)
    // if (locator != null) {
    // System.out.println(locator);
    // int lineNumber = locator.getLineNumber() - 1;
    // System.out.println(lineNumber);
    // DoradoECoreUtils.setXMLLineNumber(newObject, lineNumber);
    // }
    // }

    // protected void saveDataTypeElementSingle(EObject o, EStructuralFeature f)
    // {
    // Object value = helper.getValue(o, f);
    //
    // if (value == null) {
    // saveNil(o, f);
    // } else {
    // String svalue = getDatatypeValue(value, f, false);
    // if (!toDOM) {
    // // 将内容前后加上<![CDATA and ]]>
    // doc.saveDataValueElement(helper.getQName(f), svalue/*
    // * "<![CDATA["
    // * + svalue
    // * + "]]>"
    // */);
    // } else {
    // helper.populateNameInfo(nameInfo, f);
    // Element elem = document
    // .createElementNS(nameInfo.getNamespaceURI(),
    // nameInfo.getQualifiedName());
    // Node text = document.createTextNode(svalue);
    // elem.appendChild(text);
    // currentNode.appendChild(elem);
    // handler.recordValues(elem, o, f, value);
    // handler.recordValues(text, o, f, value);
    // }
    // }
    // }

    // protected void saveDataTypeSingle(EObject o, EStructuralFeature f) {

    // Object value = DoradoUtils.convertDataType(f, helper.getValue(o, f));

    // String svalue = getDatatypeValue(value, f, true);
    // if (svalue != null) {
    // if (!toDOM) {
    // doc.addAttribute(helper.getQName(f), svalue);
    // } else {
    // helper.populateNameInfo(nameInfo, f);
    // Attr attr = document.createAttributeNS(nameInfo
    // .getNamespaceURI(), nameInfo.getQualifiedName());
    // attr.setNodeValue(svalue);
    // ((Element) currentNode).setAttributeNodeNS(attr);
    // handler.recordValues(attr, o, f, value);
    // }
    // }
    // }

}
