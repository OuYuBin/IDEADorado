package com.bstek.designer.editor.palette

import com.bstek.designer.core.DoradoToggleEditorModeAction
import com.bstek.designer.core.palette.{DoradoPaletteToolWindowContent, DoradoPaletteToolWindowManager}
import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.bstek.designer.editor.ViewToggleEditorModeAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor

/**
 * Created by robin on 14-7-24.
 */

object ViewPaletteToolWindowManager {

  def getInstance(designer: DoradoDesignerEditorPanel): DoradoPaletteToolWindowContent = {
    val manager: ViewPaletteToolWindowManager = getInstance(designer.getProject)
    if (manager.isEditorMode) {
      return manager.getContent(designer).asInstanceOf[DoradoPaletteToolWindowContent]
    }
    return manager.doradoPaletteToolWindowContent
  }

  def getInstance(project: Project): ViewPaletteToolWindowManager = {
    return project.getComponent(classOf[ViewPaletteToolWindowManager])
  }
}

class ViewPaletteToolWindowManager(project: Project, fileManager: FileEditorManager) extends DoradoPaletteToolWindowManager(project, fileManager) {

  override def createRightDoradoToggleEditorModeAction: DoradoToggleEditorModeAction = {
    return new ViewToggleEditorModeAction(this, project, ToolWindowAnchor.RIGHT)
  }

  override def createLeftDoradoToggleEditorModeAction: DoradoToggleEditorModeAction = {
    return new ViewToggleEditorModeAction(this, project, ToolWindowAnchor.LEFT)
  }
}
