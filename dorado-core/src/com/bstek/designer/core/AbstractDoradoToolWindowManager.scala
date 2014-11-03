package com.bstek.designer.core

import javax.swing.{JComponent, Icon}

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.designer._
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.{AnAction, ActionGroup, DefaultActionGroup}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.fileEditor.{FileEditor, FileEditorManagerEvent, FileEditorManagerListener, FileEditorManager}
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.{ToolWindowAnchor, ToolWindow}
import com.intellij.openapi.wm.ex.ToolWindowEx
import com.intellij.util.ParameterizedRunnable
import com.intellij.util.messages.MessageBusConnection
import com.intellij.util.ui.update.{Update, MergingUpdateQueue}
import org.jetbrains.annotations.Nullable

/**
 * 子窗口管理器抽象基类,利用scala改写自AbstractToolWindowManager
 * Created by robin on 14-7-4.
 */
abstract class AbstractDoradoToolWindowManager(val project: Project, val fileEditorManager: FileEditorManager) extends ProjectComponent {

  final val EDITOR_MODE: String = "DORADO7_DESIGNER_EDITOR_MODE."
  private final val myWindowQueue: MergingUpdateQueue = new MergingUpdateQueue(getComponentName, 200, true, null)
  @volatile
  var doradoToolWindow: ToolWindow = null
  @volatile
  private var myToolWindowReady: Boolean = false
  @volatile
  private var myToolWindowDisposed: Boolean = false
  private var leftEditorModeAction: DoradoToggleEditorModeAction = null
  private var rightEditorModeAction: DoradoToggleEditorModeAction = null
  private var myConnection: MessageBusConnection = null

  val myPropertiesComponent: PropertiesComponent = PropertiesComponent.getInstance(project)
  val myEditorModeKey: String = EDITOR_MODE + getComponentName + ".STATE"


  private final val myListener: FileEditorManagerListener = new FileEditorManagerListener {
    def fileOpened(source: FileEditorManager, file: VirtualFile) {
      bindToDesigner(getActiveDesigner)
    }

    def fileClosed(source: FileEditorManager, file: VirtualFile) {
      ApplicationManager.getApplication.invokeLater(new Runnable {
        def run {
          bindToDesigner(getActiveDesigner)
        }
      })
    }

    def selectionChanged(event: FileEditorManagerEvent) {
      bindToDesigner(getDesigner(event.getNewEditor))
    }
  }


  def projectOpened {
    initToolWindow
    StartupManager.getInstance(project).registerPostStartupActivity(new Runnable {
      def run {
        myToolWindowReady = true
        if (getEditorMode == null) {
          initListeners
          bindToDesigner(getActiveDesigner)
        }
      }
    })
  }

  def projectClosed {
    if (!myToolWindowDisposed) {
      disposeComponent
      myToolWindowDisposed = true
      doradoToolWindow = null
    }
  }

  private def initListeners {
    myConnection = project.getMessageBus.connect(project)
    myConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, myListener)
  }

  private def removeListeners {
    myConnection.disconnect
    myConnection = null
  }

  @Nullable private def getDesigner(editor: FileEditor): DoradoDesignerEditorPanel = {
    if (editor.isInstanceOf[DoradoDesignerEditor]) {
      val designerEditor: DoradoDesignerEditor = editor.asInstanceOf[DoradoDesignerEditor]
      return designerEditor.getDoradoDesignerPanel
    }
    return null
  }

  @Nullable def getActiveDesigner: DoradoDesignerEditorPanel = {
    for (editor <- fileEditorManager.getSelectedEditors) {
      val designer: DoradoDesignerEditorPanel = getDesigner(editor)
      if (designer != null) {
        return designer
      }
    }
    return null
  }

  @Nullable protected def getCustomizations: DesignerCustomizations = {
    return DesignerCustomizations.EP_NAME.findExtension(classOf[DesignerCustomizations])
  }

  private def bindToDesigner(designer: DoradoDesignerEditorPanel) {
    myWindowQueue.cancelAllUpdates
    myWindowQueue.queue(new Update(("update")) {
      def run {
        if (!myToolWindowReady || myToolWindowDisposed) {
          return
        }
        if (doradoToolWindow == null) {
          if (designer == null) {
            return
          }
          initToolWindow
        }
        updateToolWindow(designer)
      }
    })
  }

  protected def initToolWindow

  protected def updateToolWindow(@Nullable designer: DoradoDesignerEditorPanel)

  protected final def initGearActions {
    val toolWindow: ToolWindowEx = doradoToolWindow.asInstanceOf[ToolWindowEx]
    toolWindow.setAdditionalGearActions(new DefaultActionGroup(createGearActions))
  }

  protected def getAnchor: ToolWindowAnchor

  def initComponent {
  }

  def disposeComponent {
  }

  final def createGearActions: ActionGroup = {
    val group: DefaultActionGroup = new DefaultActionGroup("In Editor Mode", true)
    if (leftEditorModeAction == null) {
      leftEditorModeAction = createLeftDoradoToggleEditorModeAction
    }
    group.add(leftEditorModeAction)
    if (rightEditorModeAction == null) {
      rightEditorModeAction = createRightDoradoToggleEditorModeAction
    }
    group.add(rightEditorModeAction)
    return group
  }

  def createLeftDoradoToggleEditorModeAction: DoradoToggleEditorModeAction

  def createRightDoradoToggleEditorModeAction: DoradoToggleEditorModeAction

  final def bind(designer: DoradoDesignerEditorPanel) {
    if (isEditorMode) {
      createAction.run(designer)
    }
  }

  final def dispose(designer: DoradoDesignerEditorPanel) {
    if (isEditorMode) {
      disposeContent(designer)
    }
  }

  protected final def getContent(designer: DoradoDesignerEditorPanel): AnyRef = {
    val toolWindow: DoradoToolWindow = designer.getClientProperty(getComponentName).asInstanceOf[DoradoToolWindow]
    return toolWindow.getContent
  }

  protected def createContent(designer: DoradoDesignerEditorPanel): DoradoToolWindow

  protected final def createContent(designer: DoradoDesignerEditorPanel, content: DoradoToolWindowContent, title: String, icon: Icon, component: JComponent, focusedComponent: JComponent, defaultWidth: Int, actions: Array[AnAction]): DoradoToolWindow = {
    return new DoradoToolWindow(content, title, icon, component, focusedComponent, designer.getDoradoContentSplitter, getEditorMode, this, project, myPropertiesComponent, getComponentName, defaultWidth, actions)
  }

  protected final def disposeContent(designer: DoradoDesignerEditorPanel) {
    val key: String = getComponentName
    val toolWindow: DoradoToolWindow = designer.getClientProperty(key).asInstanceOf[DoradoToolWindow]
    designer.putClientProperty(key, null)
    toolWindow.dispose
  }

  private final val createAction: ParameterizedRunnable[DoradoDesignerEditorPanel] = new ParameterizedRunnable[DoradoDesignerEditorPanel] {
    def run(designer: DoradoDesignerEditorPanel) {
      designer.putClientProperty(getComponentName, createContent(designer))
    }
  }
  private final val myUpdateAnchorAction: ParameterizedRunnable[DoradoDesignerEditorPanel] = new ParameterizedRunnable[DoradoDesignerEditorPanel] {
    def run(designer: DoradoDesignerEditorPanel) {
      val toolWindow: DoradoToolWindow = designer.getClientProperty(getComponentName).asInstanceOf[DoradoToolWindow]
      toolWindow.updateAnchor(getEditorMode)
    }
  }
  private final val myDisposeAction: ParameterizedRunnable[DoradoDesignerEditorPanel] = new ParameterizedRunnable[DoradoDesignerEditorPanel] {
    def run(designer: DoradoDesignerEditorPanel) {
      disposeContent(designer)
    }
  }

  private def runUpdateContent(action: ParameterizedRunnable[DoradoDesignerEditorPanel]) {
    for (editor <- fileEditorManager.getAllEditors) {
      val designer: DoradoDesignerEditorPanel = getDesigner(editor)
      if (designer != null) {
        action.run(designer)
      }
    }
  }

  protected final def isEditorMode: Boolean = {
    return getEditorMode != null
  }

  final def getEditorMode: ToolWindowAnchor = {
    val value: String = myPropertiesComponent.getValue(myEditorModeKey)
    if (value == null) {
      return getAnchor
    }
    return if ((value == "ToolWindow")) null else ToolWindowAnchor.fromText(value)
  }

  final def setEditorMode(newState: ToolWindowAnchor) {
    val oldState: ToolWindowAnchor = getEditorMode
    myPropertiesComponent.setValue(myEditorModeKey, if (newState == null) "ToolWindow" else newState.toString)
    if (oldState != null && newState != null) {
      runUpdateContent(myUpdateAnchorAction)
    }
    else if (newState != null) {
      removeListeners
      updateToolWindow(null)
      runUpdateContent(createAction)
    }
    else {
      runUpdateContent(myDisposeAction)
      initListeners
      bindToDesigner(getActiveDesigner)
    }
  }

  private[designer] final def getToolWindow: ToolWindow = {
    return doradoToolWindow
  }
}
