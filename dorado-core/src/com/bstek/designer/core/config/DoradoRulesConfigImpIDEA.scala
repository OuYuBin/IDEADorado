package com.bstek.designer.core.config

import com.bstek.dorado.idesupport.model.Rule
import com.intellij.designer.model.RadComponent
import com.bstek.designer.core.model.DoradoMetaManager
import com.intellij.openapi.project.Project
import org.eclipse.emf.ecore.EClass

import scala.beans.BeanProperty

/**
 * Created by robin on 14-3-27.
 * 为了更好的切合IDEA IDE中图形编辑器的关于模型与图形对象关系,特增加此类,加入Dorado模型构建时随即附着在IDEA中自定义的Component类的完全限定名
 *
 */
class DoradoRulesConfigImpIDEA(@BeanProperty override val project: Project) extends DoradoRulesConfigImpl(project) {

  override def createSimpleEClass(simpleRule: Rule):org.eclipse.emf.ecore.EClass={
    val simpleEClass=super.createSimpleEClass(simpleRule)
    createComponent(simpleEClass)
    return simpleEClass
  }

  override def createComplexEClass(complexRules: java.util.Map[String, com.bstek.dorado.idesupport.model.Rule], complexRule: Rule, className: String): org.eclipse.emf.ecore.EClass = {
    val complexEClass = super.createComplexEClass(complexRules, complexRule, className)
    createComponent(complexEClass)
    return complexEClass
  }

  //--注册intellij IDEA的需要的Component对象
  def createComponent(eClass: EClass) {
    val modelMeta=modelRegister.get(eClass)
    val classLoader = getClass().getClassLoader()
    val modelValue = "com.bstek.designer.core.model.DoradoRadViewComponent"
    val model = classLoader.loadClass(modelValue)
    val metaModel = DoradoMetaManager.createModel(model.asInstanceOf[Class[com.intellij.designer.model.RadComponent]], modelMeta)
    modelMeta.setMetaModel(metaModel)
  }
}
