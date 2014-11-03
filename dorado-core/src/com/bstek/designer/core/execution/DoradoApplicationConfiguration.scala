package com.bstek.designer.core.execution

import com.intellij.execution.application.{ApplicationConfigurationType, ApplicationConfiguration}
import com.intellij.openapi.project.Project

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-18
 * Time: 下午4:33
 * To change this template use File | Settings | File Templates.
 */
class DoradoApplicationConfiguration(val name: String, val project: Project, val applicationConfigurationType: ApplicationConfigurationType) extends ApplicationConfiguration(name, project, applicationConfigurationType) {


}
