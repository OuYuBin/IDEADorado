package com.bstek.designer.core.palette

import java.awt.event.{ActionEvent, ActionListener, KeyEvent}
import java.awt._
import javax.swing._

import com.intellij.designer.palette.PaletteItemsComponent
import com.intellij.icons.AllIcons
import com.intellij.ui.Gray
import com.intellij.util.ui.UIUtil

/**
 * Created by robin on 14-7-22.
 */
class DoradoPaletteGroupComponent(group: DoradoPaletteGroup) extends JCheckBox {
  private var myItemsComponent: DoradoPaletteItemsComponent = null

  setText(group.getName)
  setSelected(true)
  setIcon(AllIcons.Nodes.TreeClosed)
  setSelectedIcon(AllIcons.Modules.SourceRoot)
  setFont(getFont.deriveFont(Font.BOLD))
  setFocusPainted(false)
  setMargin(new Insets(0, 3, 0, 3))
  setOpaque(true)
  addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      myItemsComponent.setVisible(isSelected)
    }
  })
  initActions

  override def getBackground: Color = {
    if (isFocusOwner) {
      return UIUtil.getListSelectionBackground
    }
    if (UIUtil.isUnderDarcula) {
      return Gray._100
    }
    return super.getBackground
  }

  override def getForeground: Color = {
    if (isFocusOwner) {
      return UIUtil.getListSelectionForeground
    }
    return super.getForeground
  }

  def getItemsComponent: DoradoPaletteItemsComponent = {
    return myItemsComponent
  }

  def setItemsComponent(itemsComponent: DoradoPaletteItemsComponent) {
    myItemsComponent = itemsComponent
  }

  private def initActions {
    val inputMap: InputMap = getInputMap(JComponent.WHEN_FOCUSED)
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "moveFocusDown")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "moveFocusUp")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "collapse")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "expand")
    val actionMap: ActionMap = getActionMap
    actionMap.put("moveFocusDown", new MoveFocusAction(true))
    actionMap.put("moveFocusUp", new MoveFocusAction(false))
    actionMap.put("collapse", new ExpandAction(false))
    actionMap.put("expand", new ExpandAction(true))
  }

  private class MoveFocusAction(moveDown: Boolean) extends AbstractAction {

    def actionPerformed(e: ActionEvent) {
      val kfm: KeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager
      val container: Container = kfm.getCurrentFocusCycleRoot
      var policy: FocusTraversalPolicy = container.getFocusTraversalPolicy
      if (policy == null) {
        policy = kfm.getDefaultFocusTraversalPolicy
      }
      var next: Component = if (moveDown) policy.getComponentAfter(container, DoradoPaletteGroupComponent.this) else policy.getComponentBefore(container, DoradoPaletteGroupComponent.this)
      if (next.isInstanceOf[DoradoPaletteItemsComponent]) {
        val list: DoradoPaletteItemsComponent = next.asInstanceOf[DoradoPaletteItemsComponent]
        if (list.getModel.getSize != 0) {
          list.takeFocusFrom(if (list eq myItemsComponent) 0 else -1)
          return
        }
        else {
          next = if (moveDown) policy.getComponentAfter(container, next) else policy.getComponentBefore(container, next)
        }
      }
      if (next.isInstanceOf[DoradoPaletteGroupComponent]) {
        next.requestFocus
      }
    }

    private final val myMoveDown: Boolean = false
  }

  private class ExpandAction(expand: Boolean) extends AbstractAction {

    def actionPerformed(e: ActionEvent) {
      if (expand != isSelected) {
        setSelected(expand)
        if (myItemsComponent != null) {
          myItemsComponent.setVisible(isSelected)
        }
      }
    }
  }

}
