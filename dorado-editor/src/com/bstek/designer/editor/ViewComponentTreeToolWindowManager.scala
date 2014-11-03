package com.bstek.designer.editor

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.bstek.designer.core.{DoradoToggleEditorModeAction, DoradoComponentTreeToolWindowContent, DoradoComponentTreeToolWindowManager, DoradoToolWindowContent}
import com.intellij.openapi.actionSystem.{DefaultActionGroup, ActionGroup}
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor

/**
 * Created by robin on 14-7-24.
 */
//--伴生对象
object ViewComponentTreeToolWindowManager {

  def getInstance(designer: DoradoDesignerEditorPanel): DoradoComponentTreeToolWindowContent = {
    val manager: ViewComponentTreeToolWindowManager = getInstance(designer.getProject)
    if (manager.isEditorMode) {
      return manager.getContent(designer).asInstanceOf[DoradoComponentTreeToolWindowContent]
    }
    return manager.doradoToolWindowContent
  }

  def getInstance(project: Project): ViewComponentTreeToolWindowManager = {
    return project.getComponent(classOf[ViewComponentTreeToolWindowManager])
  }
}


class ViewComponentTreeToolWindowManager(project: Project, fileEditorManager: FileEditorManager) extends DoradoComponentTreeToolWindowManager(project, fileEditorManager) {

  override def createDoradoToolWindowContent: DoradoComponentTreeToolWindowContent = {
    return new ViewComponentTreeToolWindowContent(project, true)
  }

  override def createRightDoradoToggleEditorModeAction: DoradoToggleEditorModeAction = {
    return new ViewToggleEditorModeAction(this, project, ToolWindowAnchor.RIGHT)
  }

  override def createLeftDoradoToggleEditorModeAction: DoradoToggleEditorModeAction = {
    return new ViewToggleEditorModeAction(this, project, ToolWindowAnchor.LEFT)
  }
}
