package com.bstek.designer.core

import com.bstek.designer.core.palette.DoradoPaletteToolWindowManager
import com.intellij.openapi.actionSystem.{AnActionEvent, ToggleAction}
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.wm.ToolWindowAnchor

/**
 * Created by robin on 14-7-6.
 */
abstract class DoradoToggleEditorModeAction(manager: AbstractDoradoToolWindowManager, project: Project, anchor: ToolWindowAnchor) extends ToggleAction(StringUtil.capitalize(anchor.toString), "Pin/unpin tool window to " + anchor + " side UI Designer Editor", null) {

  override def isSelected(e: AnActionEvent): Boolean = {
    anchor == manager.getEditorMode
  }

  override def setSelected(e: AnActionEvent, state: Boolean) {
    if (state) {
      manager.setEditorMode(anchor)
      val oppositeManager = getOppositeManager
      if (oppositeManager.getEditorMode == anchor) {
        oppositeManager.setEditorMode(if (anchor == ToolWindowAnchor.LEFT) ToolWindowAnchor.RIGHT else ToolWindowAnchor.LEFT)
      }
    } else {
      manager.setEditorMode(null)
    }
  }

  def getOppositeManager: AbstractDoradoToolWindowManager
}
