package com.bstek.designer.core.component

import com.intellij.openapi.project.Project
import com.intellij.openapi.components.ProjectComponent
import com.intellij.execution.application.ApplicationConfiguration

/**
 * @author robin
 *
 */
trait DoradoProjectComponent {

  def updateConfigRules(applicationConfiguration: ApplicationConfiguration)

}

