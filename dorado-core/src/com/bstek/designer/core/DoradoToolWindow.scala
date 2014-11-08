package com.bstek.designer.core

import java.awt._
import java.awt.event._
import java.awt.image.BufferedImage
import javax.swing._

import com.intellij.designer.{LightToolWindowManager, AbstractToolWindowManager, LightToolWindowContent}
import com.intellij.icons.AllIcons
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.actionSystem.impl.{ActionManagerImpl, MenuItemPresentationFactory}
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ThreeComponentsSplitter
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.wm._
import com.intellij.openapi.wm.ex.ToolWindowEx
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi
import com.intellij.openapi.wm.impl.{AnchoredButton, InternalDecorator, StripeButtonUI}
import com.intellij.ui._
import com.intellij.ui.components.panels.Wrapper
import com.intellij.ui.tabs.TabsUtil
import com.intellij.util.ui.{EmptyIcon, UIUtil}
import icons.DoradoIcons

/**
 * 子窗口基本结构及样式,利用Scala重新编写,实现Android Studio中的LightToolWindow功能
 * Created by robin on 14-7-4.
 */
class DoradoToolWindow(val content: DoradoToolWindowContent
                       ,
                       val title: String
                       ,
                       val icon: Icon
                       ,
                       val component: JComponent
                       ,
                       val focusedComponent: JComponent
                       ,
                       val contentSplitter: ThreeComponentsSplitter
                       ,
                       var anchor: ToolWindowAnchor
                       ,
                       val manager: AbstractDoradoToolWindowManager
                       ,
                       val project: Project
                       ,
                       val propertiesComponent: PropertiesComponent
                       ,
                       val key: String
                       ,
                       val defaultWidth: Int
                       ,
                       val actions: Array[AnAction]) extends JPanel(new BorderLayout) {

  //--定位模式
  private val toggleAutoHideModeAction: TogglePinnedModeAction = new TogglePinnedModeAction
  //--停靠模式
  private val toggleDockModeAction: ToggleDockModeAction = new ToggleDockModeAction
  //--浮动模式
  private val toggleFloatingModeAction: ToggleFloatingModeAction = new ToggleFloatingModeAction
  //--侧边模式
  private val toggleSideModeAction: ToggleSideModeAction = new ToggleSideModeAction
  val showStateKey = LightToolWindowManager.EDITOR_MODE + key + ".SHOW"
  val widthKey = LightToolWindowManager.EDITOR_MODE + key + ".WIDTH"
  var currentWidth: Int = 0
  final val LEFT_MIN_KEY: String = "left"
  final val RIGHT_MIN_KEY: String = "right"
  final val IGNORE_WIDTH_KEY: String = "ignore_width"
  var myShowContent: Boolean = _


  val header: HeaderPanel = new HeaderPanel
  header.setLayout(new BorderLayout)
  add(header, BorderLayout.NORTH)


  //--标题区域
  val titleLabel: JLabel = new JLabel(title)
  titleLabel.setBorder(IdeBorderFactory.createEmptyBorder(2, 5, 2, 10))
  titleLabel.setFont(UIUtil.getLabelFont(UIUtil.FontSize.SMALL))
  header.add(titleLabel, BorderLayout.CENTER)

  //--功能动作集区域
  val actionPanel: JPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0))
  actionPanel.setBorder(IdeBorderFactory.createEmptyBorder(3, 0, 2, 0))
  actionPanel.setOpaque(false)
  header.add(actionPanel, BorderLayout.EAST)
  if (actions != null) {
    for (action <- actions) {
      addAction(actionPanel, action)
    }
    actionPanel.add(new JLabel(AllIcons.General.Divider))
  }

  addAction(actionPanel, new GearAction)
  addAction(actionPanel, new HideAction)

  //--主要内容区域
  val contentWrapper: JPanel = new JPanel(new BorderLayout)
  contentWrapper.setBorder(IdeBorderFactory.createBorder(SideBorder.TOP))
  contentWrapper.add(component, BorderLayout.CENTER)
  add(contentWrapper, BorderLayout.CENTER)

  addMouseListener(new MouseAdapter {
    override def mouseReleased(e: MouseEvent) {
      IdeFocusManager.getInstance(project).requestFocus(focusedComponent, true)
    }
  })

  addMouseListener(new PopupHandler {
    def invokePopup(component: Component, x: Int, y: Int) {
      showGearPopup(component, x, y)
    }
  })

  val myMinimizeButton: AnchoredButton = new AnchoredButton(this.title, this.icon) {

    override def updateUI {
      setUI(StripeButtonUI.createUI(this))
      setFont(UIUtil.getLabelFont(UIUtil.FontSize.SMALL))
    }

    def getMnemonic2: Int = {
      return 0
    }

    def getAnchor: ToolWindowAnchor = {
      return anchor
    }
  }

  myMinimizeButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      myMinimizeButton.setSelected(false)
      updateContent(true, true)
    }
  })
  myMinimizeButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5))
  myMinimizeButton.setFocusable(false)
  myMinimizeButton.setRolloverEnabled(true)
  myMinimizeButton.setOpaque(false)


  val myMinimizeComponent: JPanel = new JPanel {
    override def doLayout {
      val size: Dimension = myMinimizeButton.getPreferredSize
      myMinimizeButton.setBounds(0, 0, getWidth, size.height)
    }
  }
  myMinimizeComponent.add(myMinimizeButton)
  configureBorder
  configureWidth(defaultWidth)
  updateContent(propertiesComponent.getBoolean(showStateKey, true), false)


  private final val myWidthListener: ComponentListener = new ComponentAdapter {
    override def componentResized(e: ComponentEvent) {
      val width: Int = if (isLeft) contentSplitter.getFirstSize else contentSplitter.getLastSize
      if (width > 0 && width != currentWidth && contentSplitter.getInnerComponent.getClientProperty(IGNORE_WIDTH_KEY) == null) {
        currentWidth = width
        propertiesComponent.setValue(widthKey, Integer.toString(width))
      }
    }
  }


  //--头部容器定义
  class HeaderPanel extends JPanel {
    private var activeImage: BufferedImage = null
    private var bufferedimage: BufferedImage = null

    override def getPreferredSize: Dimension = {
      val size: Dimension = super.getPreferredSize
      return new Dimension(size.width, TabsUtil.getTabsHeight)
    }

    override def getMinimumSize: Dimension = {
      val size: Dimension = super.getMinimumSize
      return new Dimension(size.width, TabsUtil.getTabsHeight)
    }

    protected def _paintComponent(g: Graphics) {
      val r: Rectangle = getBounds
      var image: Image = null
      if (isActive) {
        if (activeImage == null || activeImage.getHeight != r.height) {
          activeImage = drawToBuffer(true, r.height)
        }
        image = activeImage
      }
      else {
        if (bufferedimage == null || bufferedimage.getHeight != r.height) {
          bufferedimage = drawToBuffer(false, r.height)
        }
        image = bufferedimage
      }
      val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
      val clipBounds: Rectangle = g2d.getClip.getBounds
      var x: Int = clipBounds.x
      while (x < clipBounds.x + clipBounds.width) {
        {
          g2d.drawImage(image, x, 0, null)
        }
        x += 150
      }
    }

  }

  //--动作集添加动作
  private def addAction(actionPanel: JPanel, action: AnAction) {
    actionPanel.add(new ActionButton(action))
  }

  private class ActionButton(val action: AnAction) extends Wrapper with ActionListener {

    val presentation: Presentation = action.getTemplatePresentation
    val button: InplaceButton = new InplaceButton(KeymapUtil.createTooltipText(presentation.getText, action), EmptyIcon.ICON_16, this) {
      override def isActive: Boolean = {
        return DoradoToolWindow.this.isActive
      }
    }
    button.setHoveringEnabled(!SystemInfo.isMac)
    setContent(button)
    val icon: Icon = presentation.getIcon
    val hoveredIcon: Icon = presentation.getHoveredIcon
    button.setIcons(icon, icon, if (hoveredIcon == null) icon else hoveredIcon)


    def actionPerformed(e: ActionEvent) {
      val inputEvent: InputEvent = if (e.getSource.isInstanceOf[InputEvent]) e.getSource.asInstanceOf[InputEvent] else null
      action.actionPerformed(AnActionEvent.createFromInputEvent(action, inputEvent, ActionPlaces.UNKNOWN))
    }

  }

  private class GearAction extends AnAction {
    val presentation: Presentation = getTemplatePresentation
    presentation.setIcon(AllIcons.General.Gear)
    presentation.setHoveredIcon(AllIcons.General.GearHover)

    def actionPerformed(e: AnActionEvent) {
      var x: Int = 0
      var y: Int = 0
      val inputEvent: InputEvent = e.getInputEvent
      if (inputEvent.isInstanceOf[MouseEvent]) {
        x = (inputEvent.asInstanceOf[MouseEvent]).getX
        y = (inputEvent.asInstanceOf[MouseEvent]).getY
      }
      showGearPopup(inputEvent.getComponent, x, y)
    }
  }

  private def showGearPopup(component: Component, x: Int, y: Int) {
    val popupMenu: ActionPopupMenu = (ActionManager.getInstance.asInstanceOf[ActionManagerImpl]).createActionPopupMenu(ToolWindowContentUi.POPUP_PLACE, createGearPopupGroup, new MenuItemPresentationFactory(true))
    popupMenu.getComponent.show(component, x, y)
  }

  private def createGearPopupGroup: DefaultActionGroup = {
    val group: DefaultActionGroup = new DefaultActionGroup
    group.add(manager.createGearActions)
    group.addSeparator
    val `type`: ToolWindowType = manager.getToolWindow.getType
    if (`type` eq ToolWindowType.DOCKED) {
      group.add(toggleAutoHideModeAction)
      group.add(toggleDockModeAction)
      group.add(toggleFloatingModeAction)
      group.add(toggleSideModeAction)
    }
    else if (`type` eq ToolWindowType.FLOATING) {
      group.add(toggleAutoHideModeAction)
      group.add(toggleFloatingModeAction)
    }
    else if (`type` eq ToolWindowType.SLIDING) {
      group.add(toggleDockModeAction)
      group.add(toggleFloatingModeAction)
    }
    return group
  }

  //--停靠模式动作实现
  private class ToggleDockModeAction extends ToggleAction {
    copyFrom(ActionManager.getInstance.getAction(InternalDecorator.TOGGLE_DOCK_MODE_ACTION_ID))

    def isSelected(e: AnActionEvent): Boolean = {
      return manager.getToolWindow.getType eq ToolWindowType.DOCKED
    }

    def setSelected(e: AnActionEvent, state: Boolean) {
      val window: ToolWindow = manager.getToolWindow
      val `type`: ToolWindowType = window.getType
      if (`type` eq ToolWindowType.DOCKED) {
        window.setType(ToolWindowType.SLIDING, null)
      }
      else if (`type` eq ToolWindowType.SLIDING) {
        window.setType(ToolWindowType.DOCKED, null)
      }
      manager.setEditorMode(null)
    }
  }

  //--浮动模式动作实现
  private class ToggleFloatingModeAction extends ToggleAction {
    copyFrom(ActionManager.getInstance.getAction(InternalDecorator.TOGGLE_FLOATING_MODE_ACTION_ID))

    def isSelected(e: AnActionEvent): Boolean = {
      return manager.getToolWindow.getType eq ToolWindowType.FLOATING
    }

    def setSelected(e: AnActionEvent, state: Boolean) {
      val window: ToolWindow = manager.getToolWindow
      val `type`: ToolWindowType = window.getType
      if (`type` eq ToolWindowType.FLOATING) {
        window.setType((window.asInstanceOf[ToolWindowEx]).getInternalType, null)
      }
      else {
        window.setType(ToolWindowType.FLOATING, null)
      }
      manager.setEditorMode(null)
    }
  }

  //--侧边模式动作实现
  private class ToggleSideModeAction extends ToggleAction {
    copyFrom(ActionManager.getInstance.getAction(InternalDecorator.TOGGLE_SIDE_MODE_ACTION_ID))

    def isSelected(e: AnActionEvent): Boolean = {
      return manager.getToolWindow.isSplitMode
    }

    def setSelected(e: AnActionEvent, state: Boolean) {
      manager.getToolWindow.setSplitMode(state, null)
      manager.setEditorMode(null)
    }
  }

  //--定位模式实现
  private class TogglePinnedModeAction extends ToggleAction {
    copyFrom(ActionManager.getInstance.getAction(InternalDecorator.TOGGLE_PINNED_MODE_ACTION_ID))

    def isSelected(e: AnActionEvent): Boolean = {
      return !manager.getToolWindow.isAutoHide
    }

    def setSelected(e: AnActionEvent, state: Boolean) {
      val window: ToolWindow = manager.getToolWindow
      window.setAutoHide(!window.isAutoHide)
      manager.setEditorMode(null)
    }
  }


  private class HideAction extends AnAction {
    val presentation: Presentation = getTemplatePresentation
    presentation.setText(UIBundle.message("tool.window.hide.action.name"))
    if (isLeft) {
      presentation.setIcon(AllIcons.General.HideLeftPart)
      presentation.setHoveredIcon(AllIcons.General.HideLeftPartHover)
    }
    else {
      presentation.setIcon(AllIcons.General.HideRightPart)
      presentation.setHoveredIcon(AllIcons.General.HideRightPartHover)
    }

    def actionPerformed(e: AnActionEvent) {
      updateContent(false, true)
    }
  }

  private def isLeft: Boolean = {
    return anchor eq ToolWindowAnchor.LEFT
  }

  private def configureBorder {
    val borderStyle: Int = if (isLeft) SideBorder.RIGHT else SideBorder.LEFT
    setBorder(IdeBorderFactory.createBorder(borderStyle | SideBorder.BOTTOM))
    myMinimizeComponent.setBorder(IdeBorderFactory.createBorder(borderStyle))
  }

  private def configureWidth(defaultWidth: Int) {
    currentWidth = propertiesComponent.getOrInitInt(widthKey, defaultWidth)
    updateWidth
    contentSplitter.getInnerComponent.addComponentListener(myWidthListener)
  }

  private def updateWidth {
    if (isLeft) {
      contentSplitter.setFirstSize(currentWidth)
    }
    else {
      contentSplitter.setLastSize(currentWidth)
    }
  }

  def updateAnchor(newAnchor: ToolWindowAnchor) {
    val minimizeParent: JComponent = contentSplitter.getInnerComponent
    minimizeParent.putClientProperty(IGNORE_WIDTH_KEY, java.lang.Boolean.TRUE)
    if (myShowContent) {
      val oldWindow: AnyRef = if (isLeft) contentSplitter.getFirstComponent else contentSplitter.getLastComponent
      if (oldWindow eq this) {
        setContentComponent(null)
      }
    }
    else {
      val key: String = getMinKey
      if (minimizeParent.getClientProperty(key) eq myMinimizeComponent) {
        minimizeParent.putClientProperty(key, null)
      }
      minimizeParent.putClientProperty(if (isLeft) RIGHT_MIN_KEY else LEFT_MIN_KEY, myMinimizeComponent)
      minimizeParent.revalidate
    }
    anchor = newAnchor
    configureBorder
    updateWidth
    if (myShowContent) {
      setContentComponent(this)
    }
    minimizeParent.putClientProperty(IGNORE_WIDTH_KEY, null)
  }


  private def updateContent(show: Boolean, flag: Boolean) {
    myShowContent = show
    val key: String = getMinKey
    val minimizeParent: JComponent = contentSplitter.getInnerComponent
    if (show) {
      minimizeParent.putClientProperty(key, null)
      minimizeParent.remove(myMinimizeComponent)
    }
    setContentComponent(if (show) this else null)
    if (!show) {
      minimizeParent.putClientProperty(key, myMinimizeComponent)
      minimizeParent.add(myMinimizeComponent)
    }
    minimizeParent.revalidate
    if (flag) {
      propertiesComponent.setValue(showStateKey, show.toString)
    }
  }

  private def isActive: Boolean = {
    val fm: IdeFocusManager = IdeFocusManager.getInstance(project)
    val component: Component = fm.getFocusedDescendantFor(this)
    if (component != null) {
      return true
    }
    val owner: Component = fm.getLastFocusedFor(WindowManager.getInstance.getIdeFrame(project))
    return owner != null && SwingUtilities.isDescendingFrom(owner, this)
  }

  private def drawToBuffer(active: Boolean, height: Int): BufferedImage = {
    val width: Int = 150
    val image: BufferedImage = UIUtil.createImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g: Graphics2D = image.createGraphics
    UIUtil.drawHeader(g, 0, width, height, active, true, false, false)
    g.dispose
    return image
  }

  private def getMinKey: String = {
    return if (isLeft) LEFT_MIN_KEY else RIGHT_MIN_KEY
  }

  private def setContentComponent(component: JComponent) {
    if (isLeft) {
      contentSplitter.setFirstComponent(component)
    }
    else {
      contentSplitter.setLastComponent(component)
    }
  }

  def getContent: AnyRef = {
    return content
  }

  def dispose {
    val minimizeParent: JComponent = contentSplitter.getInnerComponent
    minimizeParent.removeComponentListener(myWidthListener)
    setContentComponent(null)
    content.dispose
    if (!myShowContent) {
      minimizeParent.putClientProperty(getMinKey, null)
      minimizeParent.remove(myMinimizeComponent)
      minimizeParent.revalidate
    }
  }
}