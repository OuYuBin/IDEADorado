package com.bstek.designer.core.actions

import com.intellij.openapi.actionSystem.{DataConstants, AnAction, AnActionEvent}
import com.bstek.designer.core.component.DoradoProjectComponentImpl
import com.intellij.openapi.project.Project
import com.intellij.execution.application.{ApplicationConfiguration, ApplicationConfigurationType}
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.module.ModuleManager
import com.bstek.designer.core.config.Dorado7RulesConfigImpl
import com.intellij.execution.RunManager

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-16
 * Time: 下午3:44
 * To change this template use File | Settings | File Templates.
 */
class UpdateConfigRulesAction extends AnAction {

  val MAIN_CLASS_NAME = "com.bstek.dorado.idesupport.StandaloneRuleSetExporter"

  def actionPerformed(actionEvent: AnActionEvent) {
    val dataContext = actionEvent.getDataContext()
    val project = dataContext.getData(DataConstants.PROJECT)
    val doradoProject = project.asInstanceOf[Project]
    val doradoProjectComponent = doradoProject.getComponent(classOf[DoradoProjectComponentImpl])

    val applicationConfigurationType = new ApplicationConfigurationType()
    val applicationConfiguration = new ApplicationConfiguration("Dorado 7", doradoProject, applicationConfigurationType)
    val psiClass = applicationConfiguration.getConfigurationModule.findClass(MAIN_CLASS_NAME)
    applicationConfiguration.setMainClass(psiClass)
    val module = ModuleManager.getInstance(doradoProject).getModules()(0)
    applicationConfiguration.setModule(module)
    doradoProjectComponent.updateConfigRules(applicationConfiguration)
  }

}
