package com.bstek.designer.common.model;

import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import com.bstek.designer.core.config.DoradoConfigRulesModelMeta;
import com.bstek.designer.core.model.DoradoMetaModel;
import com.bstek.designer.core.model.DoradoRadViewComponent;
import com.bstek.designer.core.model.DoradoRuleConfigManager;
import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.Rule;
import com.intellij.android.designer.model.RadViewLayout;
import com.intellij.designer.model.MetaModel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * XML文件模型递归访问者对象,并让XmlTag对象接受这个访问者访问
 * Created by robin on 13-12-9.
 */
public class DoradoModelParser extends XmlRecursiveElementVisitor implements IDoradoModelParser {

    XmlFile xmlFile;

    String xmlText;

    private DoradoRadViewComponent rootComponent;

    private DoradoRadViewComponent currentComponent;

    private DoradoRuleConfigManager metaManager;

    private Project project;

    public DoradoModelParser(Project project, XmlFile xmlFile) {
        this.xmlFile = xmlFile;
        this.metaManager = getMetaManger(project);
        this.project = project;
        parse();
    }

    //--利用访问者模式将各节点信息安插访问者,以便可以通过该访问者获取更多的节点对象信息
    //--XmlTag Intellij IDEA Open API中提供的关于PSI对xml进行的扩展,提供大量关于xml中各元素信息,需要掌握并善佳灵活运用
    private void parse() {
        xmlText = ApplicationManager.getApplication().runReadAction(new Computable<String>() {
            @Override
            public String compute() {
                XmlTag root = xmlFile.getRootTag();
                if (root != null && root.getFirstChild() != null) {
                    //--接受访问者,生成根节点RadComponent对象
                    root.accept(DoradoModelParser.this);
                    return xmlFile.getText();
                }
                return "";
            }
        });
    }


    //--覆写关于访问标记对象逻辑
    @Override
    public void visitXmlTag(XmlTag tag) {
        try {
            //--通过EMF元模型获取dorado7规则信息
            Dorado7RulesConfigImpl dorado7RulesConfigImpl = metaManager.getRulesConfig(project);
            EPackage ePackage = dorado7RulesConfigImpl.getEPackage();
            String tagName = tag.getName();
            String featureName = getFeatureByTagName(currentComponent, tagName);
            //--限定不对Property与ClientEvent进行处理
            if (featureName != null) {
                EClass eClass = (EClass) ePackage.getEClassifier(featureName);
                DoradoConfigRulesModelMeta modelMeta = dorado7RulesConfigImpl.getMetaOfModel(eClass);
                MetaModel metaModel = modelMeta.getMetaModel();
                DoradoRadViewComponent component = null;
                if (modelMeta != null) {
                    //--手动生成IDEA IDE中图形化编辑器必备的RadViewComponent对象
                    component = createComponent(tag, metaModel);
                }
                if (rootComponent == null) {
                    rootComponent = component;
                }
                if (currentComponent != null) {
                    currentComponent.add(component, null);
                }
                //--将新生成的component设为当前component
                currentComponent = component;
                //--迭代产生组合数据
                super.visitXmlTag(tag);
                currentComponent = (DoradoRadViewComponent) component.getParent();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private String getFeatureByTagName(DoradoRadViewComponent owenComponent, String tagName) {
        if (tagName.equals("View"))
            return "DefaultView";
        else if (tagName.equals("Property") || tagName.equals("ClientEvent")) {
            return null;
        } else {
            if (owenComponent != null) {
                DoradoConfigRulesModelMeta modelMeta = ((DoradoMetaModel) owenComponent.getMetaModel()).getModelMeta();
                if (modelMeta != null) {
                    Rule rule = modelMeta.getRule();
                    Map<String, Child> ownChildren = rule.getChildren();
                    for (Iterator<Child> iter = ownChildren.values().iterator(); iter
                            .hasNext(); ) {
                        Child child =  iter.next();
                        Set<Rule> subChildrenRules = child.getConcreteRules();
                        for (Iterator<Rule> subIter = subChildrenRules.iterator(); subIter
                                .hasNext(); ) {
                            Rule subChildRule =  subIter.next();
                            if (subChildRule.getNodeName().equals(tagName)) {
                                return subChildRule.getName();
                            }
                        }
                    }
                }
            }
        }
        return tagName;
    }

    public DoradoRadViewComponent getRootComponent() {
        return rootComponent;
    }

    public DoradoRuleConfigManager getMetaManger(Project project) {
        return null;
    }

    public static DoradoRadViewComponent createComponent(XmlTag tag, MetaModel metaModel) throws Exception {
        DoradoRadViewComponent component = (DoradoRadViewComponent) metaModel.getModel().newInstance();
        //--设定自定义component类携带附加信息
        component.setMetaModel(metaModel);
        //--需要自定义的component类携带关于XML中本身的信息
        component.setXmlTag(tag);
        component.setLayout(RadViewLayout.INSTANCE);
        return component;
    }

}
