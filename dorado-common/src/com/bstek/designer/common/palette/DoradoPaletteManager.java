package com.bstek.designer.common.palette;

import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import com.bstek.designer.core.config.DoradoConfigRulesModelMeta;
import com.bstek.designer.core.palette.DoradoPaletteGroup;
import com.bstek.designer.core.palette.DoradoPaletteItem;
import com.bstek.dorado.idesupport.model.Rule;
import com.intellij.openapi.project.Project;
import org.eclipse.emf.ecore.*;

import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-11-20
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
public class DoradoPaletteManager {

    public Dorado7RulesConfigImpl config;

    public Project project;

    public List<EClass> childrenClasses;

    public DoradoPaletteManager(Project project, Dorado7RulesConfigImpl config) {
        this.config = config;
        this.project = project;
    }

    public void updateDoradoPalette() {
        EPackage ePackage = config.getEPackage();
        EClass eClass = (EClass) ePackage.getEClassifier("View");
        childrenClasses = sortAndFilterChildrenFeatures(getChildrenClasses(eClass));
    }

    public Collection getChildrenClasses(EClass eClass) {
        List list = new ArrayList();
        for (EReference eReference : eClass.getEAllReferences()) {
            if (eReference.isContainment()) {
                list.addAll(getChildrenConcreteSubclasses(eReference));
            }
        }
        return list;
    }

    public List<EClass> getChildrenConcreteSubclasses(EReference eReference) {
        EClass eClass = eReference.getEReferenceType();
        List<EClass> result = new ArrayList<EClass>();
        for (Iterator iter = config.getEPackage().getEClassifiers().iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if (object instanceof EClass) {
                if (eClass.isSuperTypeOf((EClass) object)) {
                    result.add((EClass) object);
                }
            }
        }
        return result;
    }

    public List sortAndFilterChildrenFeatures(Collection<EClass> childrenClasses) {
        List list = new ArrayList();
        for (EClass eClass : childrenClasses) {
            list.add(eClass);
        }
        Collections.sort(list, new DoradoPaletteChildComparator(config));
        return list;
    }

    public Map createDoradoPaletteGroup() {
        updateDoradoPalette();
        Map groupMap = new LinkedHashMap();
        String groupName = "Others";
        for (EClass eClass : childrenClasses) {
            DoradoConfigRulesModelMeta modelMeta = config.getMetaOfModel(eClass);
            if (modelMeta != null) {
                Rule rule = modelMeta.getRule();
                if (rule != null) {
                    String category = modelMeta.getCategory();
                    if (category != null) {
                        groupName = category;
                    }
                    if (groupMap.get(groupName) == null) {
                        DoradoPaletteGroup group = new DoradoPaletteGroup(groupName);
                        groupMap.put(groupName, group);
                    }
                }
            }
        }
        return groupMap;
    }


    public Map createDoradoPaletteGroupItem() {
            Map groupMap = createDoradoPaletteGroup();
        List list = new ArrayList();
        for (EClass eClass : childrenClasses) {
            DoradoConfigRulesModelMeta modelMeta = config.getMetaOfModel(eClass);
            if (modelMeta != null) {
                Rule rule = modelMeta.getRule();
                if (rule != null) {
                    String category = rule.getCategory();
                    if (category == null) {
                        category = "Others";
                    }
                    String iconPath = project.getBasePath() + File.separator + ".idea" + rule.getIcon();
                    DoradoPaletteItem item = new DoradoPaletteItem(rule.getLabel(), iconPath, rule.getLabel(),"7.4","","");
                    DoradoPaletteGroup group = (DoradoPaletteGroup) groupMap.get(category);
                    group.addItem(item);
                }
            }
        }

        return groupMap;
    }


    public List getDoradoPaletteGroup() {
        List list = new ArrayList();
        Map groupMap = createDoradoPaletteGroupItem();
        for (Iterator iter = groupMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            list.add(entry.getValue());
        }
        return list;
    }
}
