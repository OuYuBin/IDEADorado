package com.bstek.designer.editor

import com.bstek.designer.core.palette.DoradoPaletteToolWindowManager
import com.bstek.designer.core.{AbstractDoradoToolWindowManager, DoradoToggleEditorModeAction}
import com.bstek.designer.editor.palette.ViewPaletteToolWindowManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor

/**
 * Created by robin on 14-7-24.
 */
class ViewToggleEditorModeAction(manager: AbstractDoradoToolWindowManager, project: Project, anchor: ToolWindowAnchor) extends DoradoToggleEditorModeAction(manager, project, anchor) {

  override def getOppositeManager: AbstractDoradoToolWindowManager = {
    val designerManager = ViewComponentTreeToolWindowManager.getInstance(project)
    val paletteManager = ViewPaletteToolWindowManager.getInstance(project)
    if (manager == designerManager) paletteManager else designerManager;
  }
}
