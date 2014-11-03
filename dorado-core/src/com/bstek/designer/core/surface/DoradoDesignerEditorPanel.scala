package com.bstek.designer.core.surface

import java.awt._
import java.util
import java.util.List
import javax.swing._

import com.bstek.designer.core.palette.{DoradoPaletteGroup, DoradoPaletteToolWindowContent}
import com.bstek.designer.core.{AbstractDoradoToolWindowManager, DoradoDesignerEditor}
import com.intellij.designer._
import com.intellij.designer.actions.DesignerActionPanel
import com.intellij.designer.componentTree.TreeComponentDecorator
import com.intellij.designer.designSurface._
import com.intellij.designer.designSurface.tools._
import com.intellij.designer.model._
import com.intellij.designer.palette.PaletteItem
import com.intellij.designer.propertyTable.InplaceContext
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.module.{Module, ModuleUtilCore}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ThreeComponentsSplitter
import com.intellij.openapi.util.registry.Registry
import com.intellij.openapi.vfs.{ReadonlyStatusHandler, VirtualFile}
import com.intellij.ui.components.JBLayeredPane
import com.intellij.util.ui.{AsyncProcessIcon, UIUtil}
import com.intellij.util.{ArrayUtil, ThrowableRunnable}
import org.jetbrains.annotations.{NonNls, Nullable}

import scala.beans.BeanProperty


/**
 * 编辑器主面板基类
 * Created by robin on 13-12-5.
 */
abstract class DoradoDesignerEditorPanel(@BeanProperty val editor: DoradoDesignerEditor, @BeanProperty val project: Project, val module: Module, @BeanProperty val file: VirtualFile) extends JPanel with DataProvider with ModuleProvider with RadPropertyContext {

  private final val LOG: Logger = Logger.getInstance("#com.intellij.designer.designSurface.DesignerEditorPanel")

  protected final val LAYER_COMPONENT: Integer = JLayeredPane.DEFAULT_LAYER


  protected final val LAYER_DECORATION: Integer = JLayeredPane.POPUP_LAYER


  protected final val LAYER_FEEDBACK: Integer = JLayeredPane.DRAG_LAYER


  //protected final val LAYER_GLASS: Integer = LAYER_FEEDBACK + 100


  //protected final val LAYER_INPLACE_EDITING: Integer = LAYER_GLASS + 100


  //private final val LAYER_PROGRESS: Integer = LAYER_INPLACE_EDITING + 100


  private final val DESIGNER_CARD: String = "designer"


  private final val ERROR_CARD: String = "error"


  private final val ERROR_STACK_CARD: String = "stack"


  private final val ERROR_NO_STACK_CARD: String = "no_stack"


  private final val myEditor: DoradoDesignerEditor = null


  private final val myProject: Project = null


  private var myModule: Module = null


  //protected final val myFile: VirtualFile = null


  private final val myLayout: CardLayout = new CardLayout


  private final val doradoContentSplitter: ThreeComponentsSplitter = new ThreeComponentsSplitter


  private final val myPanel: JPanel = new JPanel(myLayout)


  private var myDesignerCard: JPanel = null


  protected var myActionPanel: DesignerActionPanel = null


  protected var myHorizontalCaption: CaptionPanel = null


  protected var myVerticalCaption: CaptionPanel = null


  protected var myScrollPane: JScrollPane = null


  protected var myLayeredPane: JLayeredPane = null


  protected var myGlassLayer: GlassLayer = null


  private var myDecorationLayer: DecorationLayer = null


  private var myFeedbackLayer: FeedbackLayer = null


  private var myInplaceEditingLayer: InplaceEditingLayer = null


  protected var myToolProvider: ToolProvider = null

  @BeanProperty var mySurfaceArea: EditableArea = null


  @BeanProperty var myRootComponent: RadComponent = _


  protected var myQuickFixManager: QuickFixManager = null


  private var myActivePaletteItem: PaletteItem = null


  private var myExpandedComponents: List[_] = null


  private val mySelectionPropertyMap: java.util.Map[String, com.intellij.designer.model.Property[_]] = new util.HashMap[String, com.intellij.designer.model.Property[_]]()


  //private var myExpandedState: Array[Array[Int]] = nullArray


  private var mySelectionState: Array[Array[Int]] = null


  //private final val mySourceSelectionState: Map[String, Array[Array[Int]]] = new FixedHashMap(16)


  //private var myWarnAction: DesignerEditorPanel#FixableMessageAction = null


  private var myErrorPanel: JPanel = null


  protected var myErrorMessages: JPanel = null


  private var myErrorStackPanel: JPanel = null


  private var myErrorStackLayout: CardLayout = null


  private var myErrorStack: JTextArea = null


  private var myProgressPanel: JPanel = null


  private var myProgressIcon: AsyncProcessIcon = null


  private var myProgressMessage: JLabel = null

  //--初始化面板界面
  initUI

  //myToolProvider.loadDefaultTool


  private def initUI {
    setLayout(new BorderLayout)
    doradoContentSplitter.setDividerWidth(0)
    doradoContentSplitter.setDividerMouseZoneSize(Registry.intValue("ide.splitter.mouseZone"))
    add(doradoContentSplitter, BorderLayout.CENTER)
    createDesignerCard
    //    createErrorCard
    //    createProgressPanel
    //--将编辑器附属的两个子窗口通过事件分派线程完成操作,达到界面流畅运行
    UIUtil.invokeLaterIfNeeded(new Runnable {
      def run {
        val designer: DoradoDesignerEditorPanel = DoradoDesignerEditorPanel.this
        //--通过属性窗体管理类完成编辑器与属性工具窗体的绑定
        //--通过复写getDoradoDesignerWindowManager来完成子类个性化的属性设计器
        getDoradoDesignerWindowManager.bind(designer)
        //--通过调色版窗体管理类完成编辑器与调色板工具窗体的绑定
        getPaletteWindowManager.bind(designer)
      }
    })
  }


  private def createDesignerCard {
    val panel: JPanel = new JPanel(new FillLayout)
    doradoContentSplitter.setInnerComponent(panel)
    //    val myLayeredPane = new MyLayeredPane
    //    mySurfaceArea = createEditableArea
    myToolProvider = createToolProvider
    //
    //    myGlassLayer = new GlassLayer(myToolProvider, mySurfaceArea)
    //    myLayeredPane.add(myGlassLayer, LAYER_GLASS)
    //    myDecorationLayer = createDecorationLayer
    //
    //    myLayeredPane.add(myDecorationLayer, LAYER_DECORATION)
    //    myFeedbackLayer = createFeedbackLayer
    //
    //    myLayeredPane.add(myFeedbackLayer, LAYER_FEEDBACK)
    //    myInplaceEditingLayer = createInplaceEditingLayer
    //
    //    myLayeredPane.add(myInplaceEditingLayer, LAYER_INPLACE_EDITING)
    //
    //    val content: JPanel = new JPanel(new GridBagLayout)
    //    val gbc: GridBagConstraints = new GridBagConstraints
    //    gbc.gridx = 0
    //    gbc.gridy = 1
    //    gbc.fill = GridBagConstraints.VERTICAL
    //
    //    myVerticalCaption = createCaptionPanel(false)
    //    content.add(myVerticalCaption, gbc)
    //    gbc.gridx = 1
    //    gbc.gridy = 0
    //    gbc.fill = GridBagConstraints.HORIZONTAL
    //
    //    myHorizontalCaption = createCaptionPanel(true)
    //    content.add(myHorizontalCaption, gbc)
    //    gbc.gridx = 1
    //    gbc.gridy = 1
    //    gbc.weightx = 1
    //    gbc.weighty = 1
    //    gbc.fill = GridBagConstraints.BOTH
    //    myScrollPane = createScrollPane(myLayeredPane)
    //    content.add(myScrollPane, gbc)
    //
    //    myHorizontalCaption.attachToScrollPane(myScrollPane)
    //    myVerticalCaption.attachToScrollPane(myScrollPane)
    //    myQuickFixManager = new QuickFixManager(this, myGlassLayer, myScrollPane.getViewport)
    //myActionPanel = createActionPanel
    //myWarnAction = new DesignerEditorPanel#FixableMessageAction
    panel.add(new JPanel);
    panel.add(myPanel)

    //myDesignerCard = content

    //    myPanel.add(myDesignerCard, DESIGNER_CARD)
    //    mySurfaceArea.addSelectionListener(new ComponentSelectionListener {
    //      def selectionChanged(area: EditableArea) {
    //        storeSourceSelectionState
    //      }
    //    })
  }


  final def getDoradoContentSplitter: ThreeComponentsSplitter = {
    return doradoContentSplitter
  }

  //
  //
  //  protected def createEditableArea: EditableArea = {
  //    return new DesignerEditableArea
  //  }
  //
  //
  protected def createToolProvider: ToolProvider = {
    return new DesignerToolProvider
  }

  //
  //
  //  protected def createDecorationLayer: DecorationLayer = {
  //    return new DecorationLayer(this, mySurfaceArea)
  //  }
  //
  //
  //  protected def createFeedbackLayer: FeedbackLayer = {
  //    return new FeedbackLayer
  //  }
  //
  //
  //  protected def createInplaceEditingLayer: InplaceEditingLayer = {
  //    return new InplaceEditingLayer(this)
  //  }
  //
  //
  //  protected def createCaptionPanel(horizontal: Boolean): CaptionPanel = {
  //    return new CaptionPanel(this, horizontal, true)
  //  }
  //
  //
  //  protected def createScrollPane(@NotNull content: JLayeredPane): JScrollPane = {
  //    val scrollPane: JScrollPane = ScrollPaneFactory.createScrollPane(content)
  //    scrollPane.setBackground(new JBColor(Color.WHITE, UIUtil.getListBackground))
  //    return scrollPane
  //  }
  //
  //
  //    protected def createActionPanel: DesignerActionPanel = {
  //      return new DesignerActionPanel(this, myGlassLayer)
  //    }

  //    final def getActivePaletteItem: PaletteItem = {
  //      return myActivePaletteItem
  //    }
  //

  //    final def activatePaletteItem(paletteItem: PaletteItem) {
  //      myActivePaletteItem = paletteItem
  //      if (paletteItem != null) {
  //        myToolProvider.setActiveTool(new CreationTool(true, createCreationFactory(paletteItem)))
  //      }
  //      else if (myToolProvider.getActiveTool.isInstanceOf[CreationTool]) {
  //        myToolProvider.loadDefaultTool
  //      }
  //    }

  //
  //  protected final def showDesignerCard {
  //    myErrorMessages.removeAll
  //    myErrorStack.setText(null)
  //    myLayeredPane.revalidate
  //    myHorizontalCaption.update
  //    myVerticalCaption.update
  //    myLayout.show(myPanel, DESIGNER_CARD)
  //  }
  //
  //
  //  private def createErrorCard {
  //    myErrorPanel = new JPanel(new BorderLayout)
  //    myErrorMessages = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 10, 5, true, false))
  //    myErrorPanel.add(myErrorMessages, BorderLayout.PAGE_START)
  //    myErrorStack = new JTextArea(50, 20)
  //    myErrorStack.setEditable(false)
  //    myErrorStackLayout = new CardLayout
  //    myErrorStackPanel = new JPanel(myErrorStackLayout)
  //    myErrorStackPanel.add(new JLabel, ERROR_NO_STACK_CARD)
  //    myErrorStackPanel.add(ScrollPaneFactory.createScrollPane(myErrorStack), ERROR_STACK_CARD)
  //    myErrorPanel.add(myErrorStackPanel, BorderLayout.CENTER)
  //    myPanel.add(myErrorPanel, ERROR_CARD)
  //  }
  //
  //
  final def showError(message: String, e: Throwable) {
    //    if (isProjectClosed) {
    //      return
    //    }
    //    while (e.isInstanceOf[InvocationTargetException]) {
    //      //if (e.getCause == null) {
    //      //  break //todo: break is not supported
    //      // }
    //      val e = e.getCause
    //    }
    //    val info: DesignerEditorPanel.ErrorInfo = new DesignerEditorPanel.ErrorInfo
    //    info.myMessage = ({
    //      info.myDisplayMessage = message;
    //      info.myDisplayMessage
    //    })
    //    info.myThrowable = e
    //    configureError(info)
    //    if (info.myShowMessage) {
    //      showErrorPage(info)
    //    }
    //    if (info.myShowLog) {
    //      LOG.error(LogMessageEx.createEvent(info.myDisplayMessage, info.myMessage + "\n" + ExceptionUtil.getThrowableText(info.myThrowable), getErrorAttachments(info)))
    //    }
  }

  //
  //
  //  protected def getErrorAttachments(info: DesignerEditorPanel.ErrorInfo): Array[Attachment] = {
  //    return Array[Attachment](AttachmentFactory.createAttachment(myFile))
  //  }
  //
  //
  //  protected def configureError(@NotNull info: DesignerEditorPanel.ErrorInfo)
  //
  //
  //  protected def showErrorPage(info: DesignerEditorPanel.ErrorInfo) {
  //    storeState
  //    hideProgress
  //    myRootComponent = null
  //    myErrorMessages.removeAll
  //    if (info.myShowStack) {
  //      val stream: ByteArrayOutputStream = new ByteArrayOutputStream
  //      info.myThrowable.printStackTrace(new PrintStream(stream))
  //      myErrorStack.setText(stream.toString)
  //      myErrorStackLayout.show(myErrorStackPanel, ERROR_STACK_CARD)
  //    }
  //    else {
  //      myErrorStack.setText(null)
  //      myErrorStackLayout.show(myErrorStackPanel, ERROR_NO_STACK_CARD)
  //    }
  //    addErrorMessage(new DesignerEditorPanel.FixableMessageInfo(true, info.myDisplayMessage, "", "", null, null), Messages.getErrorIcon)
  //
  //    import scala.collection.JavaConversions._
  //
  //    for (message <- info.myMessages) {
  //      addErrorMessage(message, if (message.myErrorIcon) Messages.getErrorIcon else Messages.getWarningIcon)
  //    }
  //    myErrorPanel.revalidate
  //    myLayout.show(myPanel, ERROR_CARD)
  //    getDesignerToolWindow.refresh(true)
  //    repaint
  //  }
  //
  //
  //  protected def addErrorMessage(message: DesignerEditorPanel.FixableMessageInfo, icon: Icon) {
  //    if (message.myLinkText.length > 0 || message.myAfterLinkText.length > 0) {
  //      val warnLabel: HyperlinkLabel = new HyperlinkLabel
  //      warnLabel.setOpaque(false)
  //      warnLabel.setHyperlinkText(message.myBeforeLinkText, message.myLinkText, message.myAfterLinkText)
  //      warnLabel.setIcon(icon)
  //      if (message.myQuickFix != null) {
  //        warnLabel.addHyperlinkListener(new HyperlinkListener {
  //          def hyperlinkUpdate(e: HyperlinkEvent) {
  //            if (e.getEventType eq HyperlinkEvent.EventType.ACTIVATED) {
  //              message.myQuickFix.run
  //            }
  //          }
  //        })
  //      }
  //      myErrorMessages.add(warnLabel)
  //    }
  //    else {
  //      val warnLabel: JBLabel = new JBLabel
  //      warnLabel.setOpaque(false)
  //      warnLabel.setText("<html><body>" + message.myBeforeLinkText.replace("\n", "<br>") + "</body></html>")
  //      warnLabel.setIcon(icon)
  //      myErrorMessages.add(warnLabel)
  //    }
  //    if (message.myAdditionalFixes != null && message.myAdditionalFixes.size > 0) {
  //      val fixesPanel: JPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0))
  //      fixesPanel.setBorder(IdeBorderFactory.createEmptyBorder(3, 0, 10, 0))
  //      fixesPanel.setOpaque(false)
  //      fixesPanel.add(Box.createHorizontalStrut(icon.getIconWidth))
  //
  //      import scala.collection.JavaConversions._
  //
  //      for (pair <- message.myAdditionalFixes) {
  //        val fixLabel: HyperlinkLabel = new HyperlinkLabel
  //        fixLabel.setOpaque(false)
  //        fixLabel.setHyperlinkText(pair.getFirst)
  //        val fix: Runnable = pair.getSecond
  //        fixLabel.addHyperlinkListener(new HyperlinkListener {
  //          def hyperlinkUpdate(e: HyperlinkEvent) {
  //            if (e.getEventType eq HyperlinkEvent.EventType.ACTIVATED) {
  //              fix.run
  //            }
  //          }
  //        })
  //        fixesPanel.add(fixLabel)
  //      }
  //      myErrorMessages.add(fixesPanel)
  //    }
  //  }
  //
  //
  //  protected final def showWarnMessages(messages: List[DesignerEditorPanel.FixableMessageInfo]) {
  //    if (messages == null) {
  //      myWarnAction.hide
  //    }
  //    else {
  //      myWarnAction.show(messages)
  //    }
  //  }
  //
  //
  //  private def createProgressPanel {
  //    myProgressIcon = new AsyncProcessIcon("Designer progress")
  //    myProgressMessage = new JLabel
  //    val progressBlock: JPanel = new JPanel
  //    progressBlock.add(myProgressIcon)
  //    progressBlock.add(myProgressMessage)
  //    progressBlock.setBorder(IdeBorderFactory.createRoundedBorder)
  //    myProgressPanel = new JPanel(new GridBagLayout)
  //    myProgressPanel.add(progressBlock, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0))
  //    myProgressPanel.setOpaque(false)
  //  }
  //
  //
  //  protected final def showProgress(message: String) {
  //    myProgressMessage.setText(message)
  //    if (myProgressPanel.getParent == null) {
  //      myGlassLayer.setEnabled(false)
  //      myProgressIcon.resume
  //      myLayeredPane.add(myProgressPanel, LAYER_PROGRESS)
  //      myLayeredPane.repaint
  //    }
  //  }
  //
  //
  //  protected final def hideProgress {
  //    myGlassLayer.setEnabled(true)
  //    myProgressIcon.suspend
  //    myLayeredPane.remove(myProgressPanel)
  //  }
  //
  //
  final def getModule: Module = {
    if (myModule.isDisposed) {
      myModule = findModule(myProject, getFile)
      if (myModule == null) {
        throw new IllegalArgumentException("No module for file " + getFile + " in project " + myProject)
      }
    }
    return myModule
  }

  protected def findModule(project: Project, file: VirtualFile): Module = {
    return ModuleUtilCore.findModuleForFile(file, project)
  }

  //  final def getEditor: DoradoDesignerEditor = {
  //    return myEditor
  //  }


  final def isProjectClosed: Boolean = {
    return project.isDisposed || !project.isOpen
  }

  //
  //
  //  def getSurfaceArea: EditableArea = {
  //    return mySurfaceArea
  //  }
  //
  //
  def getToolProvider: ToolProvider = {
    return myToolProvider
  }

  //
  //
  //  def getActionPanel: DesignerActionPanel = {
  //    return myActionPanel
  //  }
  //
  //
  //  def getInplaceEditingLayer: InplaceEditingLayer = {
  //    return myInplaceEditingLayer
  //  }
  //
  //
  //  def getPreferredFocusedComponent: JComponent = {
  //    return if (myDesignerCard.isVisible) myGlassLayer else myErrorPanel
  //  }
  //
  //
  //  def getExpandedComponents: List[_] = {
  //    return myExpandedComponents
  //  }
  //
  //
  //  def setExpandedComponents(expandedComponents: List[_]) {
  //    myExpandedComponents = expandedComponents
  //  }
  //
  //
  def getSelectionProperty(key: String): com.intellij.designer.model.Property[_] = {
    return mySelectionPropertyMap.get(key)
  }

  def setSelectionProperty(key: String, selectionProperty: com.intellij.designer.model.Property[_]) {
    mySelectionPropertyMap.put(key, selectionProperty)
  }

  //
  //
  //  protected def storeState {
  //    if (myRootComponent != null && myExpandedState == null && mySelectionState == null) {
  //      myExpandedState = new Array[Array[Int]](if (myExpandedComponents == null) 0 else myExpandedComponents.size)
  //      for (i <- 0 to myExpandedState.length) {
  //        val path: IntArrayList = new IntArrayList
  //        componentToPath(myExpandedComponents.get(i).asInstanceOf[RadComponent], path)
  //        myExpandedState(i) = path.toArray
  //      }
  //      mySelectionState = getSelectionState
  //      myExpandedComponents = null
  //      val tool: InputTool = myToolProvider.getActiveTool
  //
  //      if (!(tool.isInstanceOf[MarqueeTracker]) && !(tool.isInstanceOf[CreationTool]) && !(tool.isInstanceOf[PasteTool])) {
  //        myToolProvider.loadDefaultTool
  //      }
  //
  //    }
  //  }
  //
  //
  //  private def storeSourceSelectionState {
  //    if (!CommonEditActionsProvider.isDeleting) {
  //      mySourceSelectionState.put(getEditorText, getSelectionState)
  //    }
  //  }
  //
  //
  //  private def getSelectionState: Array[Array[Int]] = {
  //    return getSelectionState(mySurfaceArea.getSelection)
  //  }
  //
  //
  //  protected def getSelectionState(selection: List[RadComponent]): Array[Array[Int]] = {
  //    val selectionState: Array[Array[Int]] = new Array[Array[Int]](selection.size)
  //
  //    for (i <- 0 to selectionState.lenght) {
  //      val path: IntArrayList = new IntArrayList
  //      componentToPath(selection.get(i), path)
  //      selectionState(i) = path.toArray
  //    }
  //    return selectionState
  //  }
  //
  //
  //  private def componentToPath(component: RadComponent, path: IntArrayList) {
  //    val parent: RadComponent = component.getParent
  //    if (parent != null) {
  //      path.add(0, parent.getChildren.indexOf(component))
  //      componentToPath(parent, path)
  //    }
  //  }
  //
  //
  protected def restoreState {
    val toolManager: DoradoPropertySheetToolWindowContent = getDesignerToolWindow
    //      if (myExpandedState != null) {
    //        val expanded: List[RadComponent] = new util.ArrayList[RadComponent]
    //        for (path <- myExpandedState) {
    //          pathToComponent(expanded, myRootComponent, path, 0)
    //        }
    //        myExpandedComponents = expanded
    //        toolManager.expandFromState
    //        myExpandedState = null
    //      }
    val selection: List[RadComponent] = new util.ArrayList[RadComponent]
    //      val selectionState: Array[Array[Int]] = mySourceSelectionState.get(getEditorText)
    //      if (selectionState != null) {
    //        for (path <- selectionState) {
    //          pathToComponent(selection, myRootComponent, path, 0)
    //        }
    //      }
    //      if (selection.isEmpty) {
    //        if (mySelectionState != null) {
    //          for (path <- mySelectionState) {
    //            pathToComponent(selection, myRootComponent, path, 0)
    //          }
    //        }
    //      }
    if (selection.isEmpty) {
      toolManager.refresh(true)
    }
    //      else {
    //        mySurfaceArea.setSelection(selection)
    //      }
    //      mySelectionState = null
  }

  //
  //
  //  protected def pathToComponent(components: List[RadComponent], component: RadComponent, path: Array[Int], index: Int) {
  //    if (index == path.length) {
  //      components.add(component)
  //    }
  //    else {
  //      val children: List[RadComponent] = component.getChildren
  //      val componentIndex: Int = path(index)
  //      if (0 <= componentIndex && componentIndex < children.size) {
  //        pathToComponent(components, children.get(componentIndex), path, index + 1)
  //      }
  //    }
  //  }
  //
  //  def getPlatformTarget: String
  //
  //
  //  protected def findTarget(x: Int, y: Int, filter: ComponentTargetFilter): RadComponent = {
  //    if (myRootComponent != null) {
  //      val visitor: FindComponentVisitor = new FindComponentVisitor(myLayeredPane, filter, x, y)
  //      myRootComponent.accept(visitor, false)
  //      return visitor.getResult
  //    }
  //    return null
  //  }
  //
  //
  //  protected def getRootSelectionDecorator: ComponentDecorator
  //
  //  protected def processRootOperation(context: OperationContext): EditOperation = {
  //    return null
  //  }


  protected def execute(operation: ThrowableRunnable[Exception], updateProperties: Boolean): Boolean = {
    if (!ReadonlyStatusHandler.ensureFilesWritable(getProject(), file)) {
      return false
    }
    try {
      //myPsiChangeListener.stop()
      operation.run()
      ApplicationManager.getApplication().invokeLater(new Runnable() {
        override def run() {
          //var active = myPsiChangeListener.isActive()
          // if (active) {
          //  myPsiChangeListener.stop()
          //}
          //updateRenderer(updateProperties)
          //if (active) {
          //  myPsiChangeListener.start()
          //}
        }
      });
      return true
    }
    catch {
      case e: Throwable => showError("Execute Command", e);
        return false
    }
    finally {
      //myPsiChangeListener.start()
    }

  }


  protected def executeWithReparse(operation: ThrowableRunnable[Exception]) {

  }


  protected def execute(operations: List[EditOperation]) {

  }


  def getPaletteGroups: java.util.List[DoradoPaletteGroup]

  //  /**
  //   * Returns a suitable version label from the version attribute from a {@link PaletteItem} version
  //   */
  //  @NotNull def getVersionLabel(version: String): String = {
  //    return StringUtil.notNullize(version)
  //  }
  //
  //
  //  def isDeprecated(deprecatedIn: String): Boolean = {
  //    return !StringUtil.isEmpty(deprecatedIn)
  //  }
  //
  //
  protected def createDefaultTool: InputTool = {
    return new SelectionTool
  }

  //
  //  @NotNull protected def createCreationFactory(paletteItem: PaletteItem): ComponentCreationFactory
  //
  //  def createPasteFactory(xmlComponents: String): ComponentPasteFactory
  //
  //
  def getEditorText: String

  def activate {
  }

  //
  //
  //  def deactivate {
  //  }
  //
  def createState: DesignerEditorState = {
    return new DesignerEditorState(getFile)
  }

  //
  //
  //  def isEditorValid: Boolean = {
  //    return myFile.isValid
  //  }
  //
  //
  //  def getData(dataId: String): AnyRef = {
  //    return myActionPanel.getData(dataId)
  //  }
  //
  //
  //  def dispose {
  //    Disposer.dispose(myProgressIcon)
  //    getDesignerWindowManager.dispose(this)
  //    getPaletteWindowManager.dispose(this)
  //    Disposer.dispose(myContentSplitter)
  //  }
  //
  //
  protected def getDoradoDesignerWindowManager: AbstractDoradoToolWindowManager

  protected def getPaletteWindowManager: AbstractDoradoToolWindowManager

  protected def getDesignerToolWindow: DoradoPropertySheetToolWindowContent

  protected def getPaletteToolWindow: DoradoPaletteToolWindowContent

  //  def getWrapInProvider: WrapInProvider[_ <: RadComponent] = {
  //    return null
  //  }
  //
  //  @Nullable def getRootComponent: RadComponent = {
  //    return myRootComponent
  //  }
  //
  //
  def getTreeRoots: Array[AnyRef] = {
    return if (myRootComponent == null) ArrayUtil.EMPTY_OBJECT_ARRAY else Array[AnyRef](myRootComponent)
  }

  def getTreeDecorator: TreeComponentDecorator

  //
  //
  //  def handleTreeArea(treeArea: TreeEditableArea) {
  //  }
  //
  //  @NotNull def getTablePanelActionPolicy: TablePanelActionPolicy = {
  //    return TablePanelActionPolicy.ALL
  //  }
  //
  //  @Nullable def getPropertyTableTabs: Array[PropertyTableTab] = {
  //    return null
  //  }
  //
  //
  def isZoomSupported: Boolean = {
    return false
  }

  def zoom(`type`: ZoomType) {
  }

  def setZoom(zoom: Double) {
  }

  def getZoom: Double = {
    return 1
  }

  //
  //
  //  protected def viewZoomed {
  //    myQuickFixManager.hideHint
  //  }
  //
  //
  //  def loadInspections(progress: ProgressIndicator) {
  //  }
  //
  //
  //  def updateInspections {
  //    myQuickFixManager.update
  //  }
  //
  private final val MINIMIZE_WIDTH: Int = 25

  private final class FillLayout extends LayoutManager2 {
    def addLayoutComponent(comp: Component, constraints: AnyRef) {
    }

    def getLayoutAlignmentX(target: Container): Float = {
      return 0.5f
    }

    def getLayoutAlignmentY(target: Container): Float = {
      return 0.5f
    }

    def invalidateLayout(target: Container) {
    }

    def addLayoutComponent(name: String, comp: Component) {
    }

    def removeLayoutComponent(comp: Component) {
    }

    def maximumLayoutSize(target: Container): Dimension = {
      return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)
    }

    def preferredLayoutSize(parent: Container): Dimension = {
      val toolbar: Component = parent.getComponent(0)
      val toolbarSize: Dimension = if (toolbar.isVisible) toolbar.getPreferredSize else new Dimension
      val contentSize: Dimension = parent.getComponent(1).getPreferredSize
      var extraWidth: Int = 0
      val jParent: JComponent = parent.asInstanceOf[JComponent]
      if (jParent.getClientProperty(LightToolWindow.LEFT_MIN_KEY) != null) {
        extraWidth += MINIMIZE_WIDTH
      }
      if (jParent.getClientProperty(LightToolWindow.RIGHT_MIN_KEY) != null) {
        extraWidth += MINIMIZE_WIDTH
      }
      return new Dimension(Math.max(toolbarSize.width, contentSize.width + extraWidth), toolbarSize.height + contentSize.height)
    }

    def minimumLayoutSize(parent: Container): Dimension = {
      val toolbar: Component = parent.getComponent(0)
      val toolbarSize: Dimension = if (toolbar.isVisible) toolbar.getMinimumSize else new Dimension
      val contentSize: Dimension = parent.getComponent(1).getMinimumSize
      var extraWidth: Int = 0
      val jParent: JComponent = parent.asInstanceOf[JComponent]
      if (jParent.getClientProperty(LightToolWindow.LEFT_MIN_KEY) != null) {
        extraWidth += MINIMIZE_WIDTH
      }
      if (jParent.getClientProperty(LightToolWindow.RIGHT_MIN_KEY) != null) {
        extraWidth += MINIMIZE_WIDTH
      }
      return new Dimension(Math.max(toolbarSize.width, contentSize.width + extraWidth), toolbarSize.height + contentSize.height)
    }

    def layoutContainer(parent: Container) {
      var leftWidth: Int = 0
      var rightWidth: Int = 0
      val jParent: JComponent = parent.asInstanceOf[JComponent]
      val left: JComponent = jParent.getClientProperty(LightToolWindow.LEFT_MIN_KEY).asInstanceOf[JComponent]
      if (left != null) {
        leftWidth = MINIMIZE_WIDTH
      }
      val right: JComponent = jParent.getClientProperty(LightToolWindow.RIGHT_MIN_KEY).asInstanceOf[JComponent]
      if (right != null) {
        rightWidth = MINIMIZE_WIDTH
      }
      val extraWidth: Int = leftWidth + rightWidth
      val width: Int = parent.getWidth - extraWidth
      val height: Int = parent.getHeight
      val toolbar: Component = parent.getComponent(0)
      val toolbarSize: Dimension = if (toolbar.isVisible) toolbar.getPreferredSize else new Dimension
      toolbar.setBounds(leftWidth, 0, width, toolbarSize.height)
      parent.getComponent(1).setBounds(leftWidth, toolbarSize.height, width, height - toolbarSize.height)
      if (left != null) {
        left.setBounds(0, 0, leftWidth, height)
      }
      if (right != null) {
        right.setBounds(width + leftWidth, 0, rightWidth, height)
      }
    }
  }

  protected def getSceneSize(target: Component): Dimension = {
    var width: Int = 0
    var height: Int = 0
    if (myRootComponent != null) {
      val bounds: Rectangle = myRootComponent.getBounds(target)
      width = Math.max(width, bounds.getMaxX.asInstanceOf[Int])
      height = Math.max(height, bounds.getMaxY.asInstanceOf[Int])

      import scala.collection.JavaConversions._

      for (component <- myRootComponent.getChildren) {
        val childBounds: Rectangle = component.getBounds(target)
        width = Math.max(width, childBounds.getMaxX.asInstanceOf[Int])
        height = Math.max(height, childBounds.getMaxY.asInstanceOf[Int])
      }
    }
    width += 50
    height += 40
    return new Dimension(width, height)
  }

  //
  //  protected class DesignerEditableArea extends ComponentEditableArea {
  //    def this() {
  //      this()
  //      `super`(myLayeredPane)
  //    }
  //
  //    protected override def fireSelectionChanged {
  //      super.fireSelectionChanged
  //      myLayeredPane.revalidate
  //      myLayeredPane.repaint
  //    }
  //
  //    override def scrollToSelection {
  //      val selection: List[RadComponent] = getSelection
  //      if (selection.size == 1) {
  //        val bounds: Rectangle = selection.get(0).getBounds(myLayeredPane)
  //        if (bounds != null) {
  //          myLayeredPane.scrollRectToVisible(bounds)
  //        }
  //      }
  //    }
  //
  //    def findTarget(x: Int, y: Int, @Nullable filter: ComponentTargetFilter): RadComponent = {
  //      return DoradoDesignerEditorPanel.this.findTarget(x, y, filter)
  //    }
  //
  //    def findTargetTool(x: Int, y: Int): InputTool = {
  //      return myDecorationLayer.findTargetTool(x, y)
  //    }
  //
  //    def showSelection(value: Boolean) {
  //      myDecorationLayer.showSelection(value)
  //    }
  //
  //    def getRootSelectionDecorator: ComponentDecorator = {
  //      return DoradoDesignerEditorPanel.this.getRootSelectionDecorator
  //    }
  //
  //    @Nullable def processRootOperation(context: OperationContext): EditOperation = {
  //      return DoradoDesignerEditorPanel.this.processRootOperation(context)
  //    }
  //
  //    def getFeedbackLayer: FeedbackLayer = {
  //      return myFeedbackLayer
  //    }
  //
  //    def getRootComponent: RadComponent = {
  //      return myRootComponent
  //    }
  //
  //    def getPopupActions: ActionGroup = {
  //      return myActionPanel.getPopupActions(this)
  //    }
  //
  //    def getPopupPlace: String = {
  //      return ActionPlaces.GUI_DESIGNER_EDITOR_POPUP
  //    }
  //  }
  //
  protected class DesignerToolProvider extends ToolProvider {
    def loadDefaultTool {
      setActiveTool(createDefaultTool)
    }

    override def setActiveTool(tool: InputTool) {
      if (getActiveTool.isInstanceOf[CreationTool] && !(tool.isInstanceOf[CreationTool])) {
        getPaletteToolWindow.clearActiveItem
      }
      if (!(tool.isInstanceOf[SelectionTool])) {
        hideInspections
      }
      super.setActiveTool(tool)
    }

    def execute(operation: ThrowableRunnable[Exception], command: String, updateProperties: Boolean): Boolean = {
      val is: Array[Boolean] = Array(true)
      CommandProcessor.getInstance.executeCommand(getProject, new Runnable {
        def run {
          is(0) = DoradoDesignerEditorPanel.this.execute(operation, updateProperties)
        }
      }, command, null)
      return is(0)
    }

    def executeWithReparse(operation: ThrowableRunnable[Exception], command: String) {
      CommandProcessor.getInstance.executeCommand(getProject, new Runnable {
        def run {
          DoradoDesignerEditorPanel.this.executeWithReparse(operation)
        }
      }, command, null)
    }

    def execute(operations: List[EditOperation], command: String) {
      CommandProcessor.getInstance.executeCommand(getProject, new Runnable {
        def run {
          DoradoDesignerEditorPanel.this.execute(operations)
        }
      }, command, null)
    }

    def startInplaceEditing(@Nullable inplaceContext: InplaceContext) {
      myInplaceEditingLayer.startEditing(inplaceContext)
    }

    def hideInspections {
      myQuickFixManager.hideHint
    }

    def showError(@NonNls message: String, e: Throwable) {
      DoradoDesignerEditorPanel.this.showError(message, e)
    }

    def isZoomSupported: Boolean = {
      return DoradoDesignerEditorPanel.this.isZoomSupported
    }

    def zoom(`type`: ZoomType) {
      DoradoDesignerEditorPanel.this.zoom(`type`)
    }

    def setZoom(zoom: Double) {
      DoradoDesignerEditorPanel.this.setZoom(zoom)
    }

    def getZoom: Double = {
      return DoradoDesignerEditorPanel.this.getZoom
    }
  }

  private final class MyLayeredPane extends JBLayeredPane with Scrollable {
    override def doLayout {
      {
        var i: Int = getComponentCount - 1
        while (i >= 0) {
          {
            val component: Component = getComponent(i)
            component.setBounds(0, 0, getWidth, getHeight)
          }
          ({
            i -= 1;
            i + 1
          })
        }
      }
    }

    override def getMinimumSize: Dimension = {
      return getPreferredSize
    }

    override def getPreferredSize: Dimension = {
      val bounds: Rectangle = myScrollPane.getViewport.getBounds
      val size: Dimension = getSceneSize(this)
      size.width = Math.max(size.width, bounds.width)
      size.height = Math.max(size.height, bounds.height)
      return size
    }

    def getPreferredScrollableViewportSize: Dimension = {
      return getPreferredSize
    }

    def getScrollableUnitIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int = {
      return 10
    }

    def getScrollableBlockIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int = {
      if (orientation == SwingConstants.HORIZONTAL) {
        return visibleRect.width - 10
      }
      return visibleRect.height - 10
    }

    def getScrollableTracksViewportWidth: Boolean = {
      return false
    }

    def getScrollableTracksViewportHeight: Boolean = {
      return false
    }
  }

  //
  //  private class FixableMessageAction extends AbstractComboBoxAction[DesignerEditorPanel.FixableMessageInfo] {
  //    def this() {
  //      this()
  //      myActionPanel.getActionGroup.add(myActionGroup)
  //      val presentation: Presentation = getTemplatePresentation
  //      presentation.setDescription("Warnings")
  //      presentation.setIcon(AllIcons.Ide.Warning_notifications)
  //    }
  //
  //    def show(messages: List[DesignerEditorPanel.FixableMessageInfo]) {
  //      if (!myIsAdded) {
  //        myTitle = Integer.toString(messages.size)
  //        setItems(messages, null)
  //        myActionGroup.add(this)
  //        myActionPanel.update
  //        myIsAdded = true
  //      }
  //    }
  //
  //    def hide {
  //      if (myIsAdded) {
  //        myActionGroup.remove(this)
  //        myActionPanel.update
  //        myIsAdded = false
  //      }
  //    }
  //
  //    @NotNull protected override def createPopupActionGroup(button: JComponent): DefaultActionGroup = {
  //      val actionGroup: DefaultActionGroup = new DefaultActionGroup
  //      import scala.collection.JavaConversions._
  //      for (message <- myItems) {
  //        var action: AnAction = null
  //        if ((message.myQuickFix != null && (message.myLinkText.length > 0 || message.myAfterLinkText.length > 0)) || (message.myAdditionalFixes != null && message.myAdditionalFixes.size > 0)) {
  //          val defaultAction: Array[AnAction] = new Array[AnAction](1)
  //          val popupGroup: DefaultActionGroup = new DefaultActionGroup {
  //            override def canBePerformed(context: DataContext): Boolean = {
  //              return true
  //            }
  //
  //            override def actionPerformed(e: AnActionEvent) {
  //              defaultAction(0).actionPerformed(e)
  //            }
  //          }
  //          popupGroup.setPopup(true)
  //          action = popupGroup
  //          if (message.myQuickFix != null && (message.myLinkText.length > 0 || message.myAfterLinkText.length > 0)) {
  //            val popupAction: AnAction = new AnAction {
  //              def actionPerformed(e: AnActionEvent) {
  //                message.myQuickFix.run
  //              }
  //            }
  //            popupAction.getTemplatePresentation.setText(cleanText(message.myLinkText + message.myAfterLinkText))
  //            popupGroup.add(popupAction)
  //            defaultAction(0) = popupAction
  //          }
  //          if (message.myAdditionalFixes != null && message.myAdditionalFixes.size > 0) {
  //            import scala.collection.JavaConversions._
  //            for (pair <- message.myAdditionalFixes) {
  //              val popupAction: AnAction = new AnAction {
  //                def actionPerformed(e: AnActionEvent) {
  //                  pair.second.run
  //                }
  //              }
  //              popupAction.getTemplatePresentation.setText(cleanText(pair.first))
  //              popupGroup.add(popupAction)
  //              if (defaultAction(0) == null) {
  //                defaultAction(0) = popupAction
  //              }
  //            }
  //          }
  //        }
  //        else {
  //          action = new EmptyAction(true)
  //        }
  //        actionGroup.add(action)
  //        update(message, action.getTemplatePresentation, true)
  //      }
  //      return actionGroup
  //    }
  //
  //    protected def update(item: DesignerEditorPanel.FixableMessageInfo, presentation: Presentation, popup: Boolean) {
  //      if (popup) {
  //        presentation.setText(cleanText(item.myBeforeLinkText))
  //      }
  //      else {
  //        presentation.setText(myTitle)
  //      }
  //    }
  //
  //    private def cleanText(text: String): String = {
  //      if (text != null) {
  //        var text = text.trim
  //        text = StringUtil.replace(text, "&nbsp;", " ")
  //        text = StringUtil.replace(text, "\n", " ")
  //        val builder: StringBuilder = new StringBuilder
  //        val length: Int = text.length
  //        var whitespace: Boolean = false
  //        for (i <- 0 to length) {
  //          val ch: Char = text.charAt(i)
  //          if (ch == ' ') {
  //            if (!whitespace) {
  //              whitespace = true
  //              builder.append(ch)
  //            }
  //          }
  //          else {
  //            whitespace = false
  //            builder.append(ch)
  //          }
  //        }
  //        text = builder.toString
  //      }
  //      return text
  //    }
  //
  //    protected def selectionChanged(item: DesignerEditorPanel.FixableMessageInfo): Boolean = {
  //      return false
  //    }
  //
  //    private final val myActionGroup: DefaultActionGroup = new DefaultActionGroup
  //    private var myTitle: String = null
  //    private var myIsAdded: Boolean = false
  //  }
  //
  //  final class ErrorInfo {
  //    var myMessage: String = null
  //    var myDisplayMessage: String = null
  //    final val myMessages: List[DesignerEditorPanel.FixableMessageInfo] = new util.ArrayList[DesignerEditorPanel.FixableMessageInfo]
  //    var myThrowable: Throwable = null
  //    var myShowMessage: Boolean = true
  //    var myShowStack: Boolean = true
  //    var myShowLog: Boolean = false
  //  }
  //
  //  final class FixableMessageInfo(errorIcon: Boolean, beforeLinkText: String, linkText: String, afterLinkText: String, quickFix: Runnable, additionalFixes: List[Pair[String, Runnable]]) {
  //  }
  //
  //  private class FixedHashMap(size: Int) extends util.HashMap[_, _] {
  //
  //    private final val myKeys: List[_] = new util.LinkedList[_]
  //
  //    override def put(key: AnyRef, value: AnyRef): AnyRef = {
  //      if (!myKeys.contains(key)) {
  //        if (myKeys.size >= mySize) {
  //          remove(myKeys.remove(0))
  //        }
  //        myKeys.add(key)
  //      }
  //      return super.put(key, value)
  //    }
  //
  //    override def get(key: AnyRef): AnyRef = {
  //      if (myKeys.contains(key)) {
  //        val index: Int = myKeys.indexOf(key)
  //        val last: Int = myKeys.size - 1
  //        myKeys.set(index, myKeys.get(last))
  //        myKeys.set(last, key)
  //      }
  //      return super.get(key)
  //    }
  //
  //
  //  }

}
