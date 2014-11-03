package com.bstek.designer.core.palette

import java.awt._
import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import javax.swing._
import javax.swing.plaf.basic.BasicListUI

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.designer.componentTree.TreeTransfer
import com.intellij.ide.dnd.{DnDAction, DnDDragStartBean, DnDManager, DnDSource}
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.components.JBList
import com.intellij.ui.{ColoredListCellRenderer, SimpleTextAttributes}
import com.intellij.util.ui.UIUtil

/**
 * Created by robin on 14-7-22.
 */
class DoradoPaletteItemsComponent(myGroup: DoradoPaletteGroup, myDesigner: DoradoDesignerEditorPanel) extends JBList {
  private final val DEPRECATED_ATTRIBUTES: SimpleTextAttributes = new SimpleTextAttributes(SimpleTextAttributes.STYLE_STRIKEOUT, null)
  //private final val myGroup: DoradoPaletteGroup = null
  //private final val myDesigner: DesignerEditorPanel = null
  private var myBeforeClickSelectedRow: Int = -1
  private var myNeedClearSelection: Boolean = false
  private var myTempWidth: Integer = null

  setModel(new DoradoListModel)

  class DoradoListModel extends AbstractListModel {
    def getSize: Int = {
      return myGroup.getItems.size
    }

    def getElementAt(index: Int): Object = {
      return myGroup.getItems.get(index)
    }
  }

  val renderer: ColoredListCellRenderer[Any] = new ColoredListCellRenderer[Any] {
    protected def customizeCellRenderer(list: javax.swing.JList, value: AnyRef, index: Int, selected: Boolean, hasFocus: Boolean) {
      clear
      val item: DoradoPaletteItem = value.asInstanceOf[DoradoPaletteItem]
      val enabled: Boolean = true
      setEnabled(enabled)
      if (enabled) {
        setIcon(item.getIcon)
      }
      else {
        setIcon(IconLoader.getDisabledIcon(item.getIcon))
      }
      val title: String = item.getTitle
      var tooltip: String = item.getTooltip
      var version: String = "7.4"
      val deprecatedIn: String = item.getDeprecatedVersion
      val deprecated: Boolean = false
      append(title, if (deprecated) DEPRECATED_ATTRIBUTES else SimpleTextAttributes.REGULAR_ATTRIBUTES)
      if (!version.isEmpty) {
        version = "<sup><i>" + version + "</i></sup>"
      }
      if (tooltip != null) {
        var deprecatedMessage: String = ""
        if (deprecated) {
          deprecatedMessage = String.format("<b>This item is deprecated in version \"%1$s\".<br>", "7.4")
          val hint: String = item.getDeprecatedHint
          if (!StringUtil.isEmpty(hint)) {
            deprecatedMessage += hint
          }
          deprecatedMessage += "</b><br><br>"
        }
        tooltip = "<html><body><center><b>" + StringUtil.escapeXml(title) + "</b>" + version + "</center><p style='width: 300px'>" + deprecatedMessage + tooltip + "</p></body></html>"
      }
      setToolTipText(tooltip)
    }
  }
  renderer.getIpad.left = 2 * UIUtil.getTreeLeftChildIndent
  renderer.getIpad.right = UIUtil.getTreeRightChildIndent
  setCellRenderer(renderer)
  setVisibleRowCount(0)
  setLayoutOrientation(JList.HORIZONTAL_WRAP)
  setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
  addMouseListener(new MouseAdapter {
    override def mousePressed(e: MouseEvent) {
      myNeedClearSelection = SwingUtilities.isLeftMouseButton(e) && myBeforeClickSelectedRow >= 0 && locationToIndex(e.getPoint) == myBeforeClickSelectedRow && !UIUtil.isControlKeyDown(e) && !e.isShiftDown
    }

    override def mouseReleased(e: MouseEvent) {
      if (SwingUtilities.isLeftMouseButton(e) && myBeforeClickSelectedRow >= 0 && locationToIndex(e.getPoint) == myBeforeClickSelectedRow && !UIUtil.isControlKeyDown(e) && !e.isShiftDown && myNeedClearSelection) {
        clearSelection
      }
    }
  })
  setDragEnabled(true)
  setTransferHandler(new TreeTransfer(classOf[DoradoPaletteItem]))
  DnDManager.getInstance.registerSource(new DnDSource {
    def canStartDragging(action: DnDAction, dragOrigin: Point): Boolean = {
      val index: Int = locationToIndex(dragOrigin)
      if (index != -1 && myDesigner != null) {
        val paletteItem: DoradoPaletteItem = myGroup.getItems.get(index)
        //myDesigner.activatePaletteItem(paletteItem)
      }
      return false
    }

    def startDragging(action: DnDAction, dragOrigin: Point): DnDDragStartBean = {
      return null
    }

    override def createDraggedImage(action: DnDAction, dragOrigin: Point):
    com.intellij.openapi.util.Pair[Image, Point] = {
      return null
    }

    def dragDropEnd {
    }

    def dropActionChanged(gestureModifiers: Int) {
    }
  }, this)
  initActions

  override def updateUI {
    setUI(new BasicListUI {
      protected override def updateLayoutState {
        super.updateLayoutState
        val insets: Insets = list.getInsets
        val listWidth: Int = list.getWidth - (insets.left + insets.right)
        if (listWidth >= cellWidth) {
          val columnCount: Int = listWidth / cellWidth
          cellWidth = if ((columnCount == 0)) 1 else listWidth / columnCount
        }
      }

      protected override def installListeners {
        //        addMouseListener(myListener = new MouseAdapter {
        //          override def mousePressed(e: MouseEvent) {
        //            myBeforeClickSelectedRow = list.getSelectedIndex
        //          }
        //        })
        //        super.installListeners
      }

      //
      protected override def uninstallListeners {
        //        if (myListener != null) {
        //          removeMouseListener(myListener)
        //        }
        //        super.uninstallListeners
      }

      //      private[palette] var myListener: MouseListener = null
    }
    )
    invalidate
  }

  override def getWidth: Int = {
    return if ((myTempWidth == null)) super.getWidth else myTempWidth.intValue
  }

  def getPreferredHeight(width: Int): Int = {
    myTempWidth = width
    try {
      return getUI.getPreferredSize(this).height
    }
    finally {
      myTempWidth = null
    }
  }

  def takeFocusFrom(indexToSelect: Int) {
    //    if (indexToSelect == -1) {
    //      indexToSelect = getModel.getSize - 1
    //    }
    //    else if (getModel.getSize == 0) {
    //      indexToSelect = -1
    //    }
    requestFocus
    setSelectedIndex(indexToSelect)
    if (indexToSelect >= 0) {
      ensureIndexIsVisible(indexToSelect)
    }
  }

  def restoreSelection(paletteItem: DoradoPaletteItem) {
    if (paletteItem == null) {
      clearSelection
    }
    else {
      val index: Int = myGroup.getItems.indexOf(paletteItem)
      if (index == -1) {
        clearSelection
      }
      else {
        takeFocusFrom(index)
      }
    }
  }

  private def initActions {
    val map: ActionMap = getActionMap
    map.put("selectPreviousRow", new MoveFocusAction(map.get("selectPreviousRow"), false))
    map.put("selectNextRow", new MoveFocusAction(map.get("selectNextRow"), true))
    //map.put("selectPreviousColumn", new MoveFocusAction(new ChangeColumnAction(map.get("selectPreviousColumn"), false), false))
    //map.put("selectNextColumn", new MoveFocusAction(new ChangeColumnAction(map.get("selectNextColumn"), true), true))
  }

  private class MoveFocusAction(defaultAction: Action, focusNext: Boolean) extends AbstractAction {

    def actionPerformed(e: ActionEvent) {
      //      val selIndexBefore: Int = getSelectedIndex
      //      myDefaultAction.actionPerformed(e)
      //      val selIndexCurrent: Int = getSelectedIndex
      //      if (selIndexBefore != selIndexCurrent) {
      //        return
      //      }
      //      if (myFocusNext && selIndexCurrent == 0) {
      //        return
      //      }
      //      val kfm: KeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager
      //      val container: Container = kfm.getCurrentFocusCycleRoot
      //      var policy: FocusTraversalPolicy = container.getFocusTraversalPolicy
      //      if (policy == null) {
      //        policy = kfm.getDefaultFocusTraversalPolicy
      //      }
      //      val next: Component = if (myFocusNext) policy.getComponentAfter(container, DoradoPaletteItemsComponent.this) else policy.getComponentBefore(container, DoradoPaletteItemsComponent.this)
      //      if (next.isInstanceOf[DoradoPaletteGroupComponent]) {
      //        clearSelection
      //        next.requestFocus
      //        (next.asInstanceOf[DoradoPaletteGroupComponent]).scrollRectToVisible(next.getBounds)
      //      }
          }
    }

    private class ChangeColumnAction(defaultAction: Action, selectNext: Boolean) extends AbstractAction {

      def actionPerformed(e: ActionEvent) {
        //      val selIndexBefore: Int = getSelectedIndex
        //      myDefaultAction.actionPerformed(e)
        //      var selIndexCurrent: Int = getSelectedIndex
        //      if (mySelectNext && selIndexBefore < selIndexCurrent || !mySelectNext && selIndexBefore > selIndexCurrent) {
        //        return
        //      }
        //      if (mySelectNext) {
        //        if (selIndexCurrent == selIndexBefore + 1) {
        //          selIndexCurrent += 1
        //        }
        //        if (selIndexCurrent < getModel.getSize - 1) {
        //          setSelectedIndex(selIndexCurrent + 1)
        //          scrollRectToVisible(getCellBounds(selIndexCurrent + 1, selIndexCurrent + 1))
        //        }
        //      }
        //      else if (selIndexCurrent > 0) {
        //        setSelectedIndex(selIndexCurrent - 1)
        //        scrollRectToVisible(getCellBounds(selIndexCurrent - 1, selIndexCurrent - 1))
        //      }
        //    }

      }

    }

  }
