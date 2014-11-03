package com.bstek.designer.core

import _root_.icons.DoradoIcons
import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.designer._
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ex.ToolWindowEx
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi
import com.intellij.openapi.wm.{ToolWindowAnchor, ToolWindowManager}
import com.intellij.ui.content.{Content, ContentManager}
import org.jetbrains.annotations.Nullable

import scala.beans.BeanProperty

/**
 * Dorado7属性编辑子窗口管理抽象父类
 * Created by robin on 14-6-280.
 *
 * @author robin
 *
 */

abstract class DoradoComponentTreeToolWindowManager(project: Project, fileEditorManager: FileEditorManager) extends AbstractDoradoToolWindowManager(project, fileEditorManager) {

  val doradoToolWindowContent: DoradoComponentTreeToolWindowContent = createDoradoToolWindowContent

  //--构建子窗口
  protected def initToolWindow {
    //--已编程方式注册一个子窗口
    doradoToolWindow = ToolWindowManager.getInstance(project).registerToolWindow("Dorado7 ComponentTree", false, getAnchor, project, true)
    doradoToolWindow.setIcon(DoradoIcons.DORADO7_PROPERTY_SHEET)
    //--判断是否为无头环境
    if (!ApplicationManager.getApplication.isHeadlessEnvironment) {
      doradoToolWindow.getComponent.putClientProperty(ToolWindowContentUi.HIDE_ID_LABEL, "true")
    }
    //--设定工具栏动作集
    (doradoToolWindow.asInstanceOf[ToolWindowEx]).setTitleActions(doradoToolWindowContent.createActions: _*)
    //--扩充齿轮装备动作集
    initGearActions

    val contentManager: ContentManager = doradoToolWindow.getContentManager
    val content: Content = contentManager.getFactory.createContent(doradoToolWindowContent.getToolWindowPanel, "Dorado7 Properties", false)
    content.setCloseable(false)
    //content.setPreferredFocusableComponent(doradoToolWindowContent.getComponentTree)
    contentManager.addContent(content)
    contentManager.setSelectedContent(content, true)
    doradoToolWindow.setAvailable(false, null)
  }

  protected def getAnchor: ToolWindowAnchor = {
    val customization: DesignerCustomizations = getCustomizations
    return if (customization != null) customization.getStructureAnchor else ToolWindowAnchor.LEFT
  }

  protected def updateToolWindow(@Nullable designer: DoradoDesignerEditorPanel) {
    doradoToolWindowContent.update(designer)
    if (designer == null) {
      doradoToolWindow.setAvailable(false, null)
    }
    else {
      doradoToolWindow.setAvailable(true, null)
      doradoToolWindow.show(null)
    }
  }

  override def disposeComponent {
    doradoToolWindowContent.dispose
  }

  def getComponentName: String = {
    return "DoradoDesignerToolWindowManager"
  }

  protected def createContent(designer: DoradoDesignerEditorPanel): DoradoToolWindow = {
    val toolWindowContent: DoradoComponentTreeToolWindowContent = createDoradoToolWindowContent
    toolWindowContent.update(designer)
    return createContent(designer, toolWindowContent, "Dorado7 ComponentTree", DoradoIcons.DORADO7_PROPERTY_SHEET, toolWindowContent.getToolWindowPanel, null, 320, toolWindowContent.createActions)
  }

  def createDoradoToolWindowContent:DoradoComponentTreeToolWindowContent

}

