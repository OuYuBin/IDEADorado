package com.bstek.designer.core.palette

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.bstek.designer.core.{AbstractDoradoToolWindowManager, DoradoToolWindow}
import com.intellij.designer.DesignerCustomizations
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindowAnchor, ToolWindowManager}
import icons.UIDesignerNewIcons

/**
 * 调色板子窗口管理
 * Created by robin on 14-7-6.
 */
abstract class DoradoPaletteToolWindowManager(project: Project, fileManager: FileEditorManager) extends AbstractDoradoToolWindowManager(project, fileManager) {

  val doradoPaletteToolWindowContent: DoradoPaletteToolWindowContent = new DoradoPaletteToolWindowContent

  protected def initToolWindow() {
    doradoToolWindow = ToolWindowManager.getInstance(project).registerToolWindow("Dorado7 Palette", false, getAnchor, project, true)
    doradoToolWindow.setIcon(AllIcons.Toolwindows.ToolWindowPalette)

    //--扩充齿轮动作集
    initGearActions

    val contentManager = doradoToolWindow.getContentManager()
    val content = contentManager.getFactory().createContent(doradoPaletteToolWindowContent, null, false)
    content.setCloseable(false)
    content.setPreferredFocusableComponent(doradoPaletteToolWindowContent)
    contentManager.addContent(content)
    contentManager.setSelectedContent(content, true)
    doradoToolWindow.setAvailable(false, null)
  }

  protected def updateToolWindow(designer: DoradoDesignerEditorPanel) {
    //palettePanel.loadPalette(designer)
    if (designer == null) {
      doradoToolWindow.setAvailable(false, null)
    }
    else {
      doradoToolWindow.setAvailable(true, null)
      doradoToolWindow.show(null)
    }
  }

  protected def getAnchor: ToolWindowAnchor = {
    val customization: DesignerCustomizations = getCustomizations
    return if (customization != null) customization.getStructureAnchor else ToolWindowAnchor.RIGHT
  }


  override def disposeComponent {
    //palettePanel.dispose()
  }


  protected def createContent(designer: DoradoDesignerEditorPanel): DoradoToolWindow = {
    val doradoPaletteToolWindowContent: DoradoPaletteToolWindowContent = new DoradoPaletteToolWindowContent
    doradoPaletteToolWindowContent.loadPalette(designer)
    return createContent(designer, doradoPaletteToolWindowContent, "Dorado7 Palette", AllIcons.Toolwindows.ToolWindowPalette, doradoPaletteToolWindowContent, doradoPaletteToolWindowContent, 180, null)

  }

  protected def getComponentName: String = {
    "DoradoPaletteToolWindowManager"
  }

}


