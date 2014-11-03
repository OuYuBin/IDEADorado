package com.bstek.designer.core.config

import com.intellij.openapi.project.Project
import com.bstek.designer.core.component.DoradoProjectComponentImpl

/**
 *
 */
class Dorado7RulesConfigImpl private(project: Project) extends DoradoRulesConfigImpIDEA(project) {

  initialize()
}

//--伴生对象,实现单例
object Dorado7RulesConfigImpl {

  def apply(project: Project) = {
    val doradoProject = project.asInstanceOf[Project]
    val doradoProjectComponent = doradoProject.getComponent(classOf[DoradoProjectComponentImpl])
    val dorado7RulesConfig: Dorado7RulesConfigImpl = new Dorado7RulesConfigImpl(project)
    doradoProjectComponent.setDorado7RulesConfig(dorado7RulesConfig)
  }
}
