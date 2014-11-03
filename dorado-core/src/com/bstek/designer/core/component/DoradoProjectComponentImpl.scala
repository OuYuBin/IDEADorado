package com.bstek.designer.core.component

import java.io._

import com.bstek.designer.core.actions.{UpdateConfigIconsProcessor, UpdateConfigRulesProcessor}
import com.bstek.designer.core.config.Dorado7RulesConfigImpl
import com.intellij.execution._
import com.intellij.execution.application.ApplicationConfiguration
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.{RunManagerImpl, RunnerAndConfigurationSettingsImpl}
import com.intellij.execution.process.{DefaultJavaProcessHandler, ProcessHandler}
import com.intellij.javaee.facet.JavaeeFacetUtil
import com.intellij.javaee.web.WebRoot
import com.intellij.javaee.web.facet.WebFacet
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import org.apache.commons.lang.StringUtils

import scala.beans.BeanProperty

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-15
 * Time: 下午11:58
 * To change this template use File | Settings | File Templates.
 */
class DoradoProjectComponentImpl(val project: Project) extends ProjectComponent with DoradoProjectComponent {


  @BeanProperty var dorado7RulesConfig: Dorado7RulesConfigImpl = _
  var webRoot: WebRoot = _

  def updateConfigRules(applicationConfiguration: ApplicationConfiguration): Unit = {
    val javaeeFacets = JavaeeFacetUtil.getInstance().getAllJavaeeFacets(project)
    var foundWebRoot = false
    var i: Int = 0
    val k = javaeeFacets.length
    while (i < k && !foundWebRoot) {
      var facet = javaeeFacets(i)
      if (facet.isInstanceOf[WebFacet]) {
        webRoot = facet.asInstanceOf[WebFacet].getWebRoots().get(0)
        foundWebRoot = true
      }
      i = i + 1
    }
    if (webRoot != null) {
      val rootUrl = webRoot.getDirectoryUrl
      val webRootPath = VfsUtil.toUri(rootUrl)
      val args = project.getBasePath + "/.rules" + " " + "file:" + webRootPath.getRawPath + "/WEB-INF/dorado-home"

      applicationConfiguration.setProgramParameters(StringUtils.replace(args, "\\", "/"))
      val runManager = RunManagerImpl.getInstanceImpl(project)
      runManager.createRunConfiguration("update-config-rules", applicationConfiguration.getFactory())
      val settings = new RunnerAndConfigurationSettingsImpl(runManager, applicationConfiguration, false)
      val executor = DefaultRunExecutor.getRunExecutorInstance()
      val conn = project.getMessageBus().connect()
      conn.subscribe(ExecutionManager.EXECUTION_TOPIC, new ExecutionAdapter() {
        override def processTerminating(runProfile: RunProfile, handler: ProcessHandler) {
          if (runProfile == applicationConfiguration && handler.isProcessTerminating) {
            val commandLine = (handler.asInstanceOf[DefaultJavaProcessHandler]).getCommandLine
            val classPathes = getClassPathes(commandLine)
            val rootFolder = new File(project.getBasePath + File.separator+ ".idea")
            if (classPathes.length > 0 && rootFolder.exists())
              scheduleRegistIcon(rootFolder, classPathes)
          }
        }

        override def processTerminated(runProfile: RunProfile, handler: ProcessHandler) {
          if (runProfile == applicationConfiguration && handler.isProcessTerminated)
            scheduleUpdate()
          handler.destroyProcess()

        }
      })
      ProgramRunnerUtil.executeConfiguration(project, settings, executor)
    }
  }

  def getClassPathes(commandLine: String): Array[String] = {
    StringUtils.substringBetween(commandLine, "-classpath ", " ").split(":")

  }


  def scheduleUpdate() {
    new UpdateConfigRulesProcessor(project).run()
  }

  def scheduleRegistIcon(rootFolder: File, classPathes: Array[String]) {
    new UpdateConfigIconsProcessor(rootFolder, classPathes).run()
  }


  def disposeComponent() = {
  }

  def initComponent() = {
  }

  def projectClosed() = {
  }

  def projectOpened() = {
  }

  def getComponentName: String = {
    "DoradoProjectComponent"
  }

}
