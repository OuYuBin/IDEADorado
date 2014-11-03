package com.bstek.designer.core.palette

import java.awt.{GridLayout, Component}
import java.awt.dnd.{DragSource, DragSourceListener, DragSourceDropEvent, DragSourceAdapter}
import java.awt.event.{KeyEvent, FocusListener, FocusEvent, FocusAdapter}
import java.util
import java.util.Collections
import javax.swing.{JComponent, KeyStroke, JScrollPane, JPanel}
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}

import com.bstek.designer.core.DoradoToolWindowContent
import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.designer.palette._
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.ScrollPaneFactory

/**
 * Created by robin on 14-7-6.
 */
class DoradoPaletteToolWindowContent extends JPanel(new GridLayout(1, 1)) with DataProvider with DoradoToolWindowContent {

  private final val myPaletteContainer: JPanel = new DoradoPaletteContainer


  private var myGroupComponents: java.util.List[DoradoPaletteGroupComponent] = Collections.emptyList()


  private var myItemsComponents: java.util.List[DoradoPaletteItemsComponent] = Collections.emptyList()


  private var myGroups: java.util.List[DoradoPaletteGroup] = Collections.emptyList()


  private var myDesigner: DoradoDesignerEditorPanel = _

  val scrollPane: JScrollPane = ScrollPaneFactory.createScrollPane(myPaletteContainer)
  scrollPane.setBorder(null)
  add(scrollPane)


  new AnAction {
    def actionPerformed(e: AnActionEvent) {
      clearActiveItem
    }
  }.registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)), scrollPane)

  //--判断是否是无头模式环境
  if (!ApplicationManager.getApplication.isHeadlessEnvironment) {
    DragSource.getDefaultDragSource.addDragSourceListener(myDragSourceListener)
  }


  private final val myFocusListener: FocusListener = new FocusAdapter {
    override def focusGained(e: FocusEvent) {

      //      for (itemsComponent <- myItemsComponents.toArray) {
      //        itemsComponent.clearSelection
      //      }
    }
  }


  private final val mySelectionListener: ListSelectionListener = new ListSelectionListener {
    def valueChanged(event: ListSelectionEvent) {
      notifySelection(event)
    }
  }


  private final val myDragSourceListener: DragSourceListener = new DragSourceAdapter {
    override def dragDropEnd(event: DragSourceDropEvent) {
      val component: Component = event.getDragSourceContext.getComponent
      //      if (!event.getDropSuccess && component.isInstanceOf[PaletteItemsComponent] && myDesigner != null && myDesigner.getRootPane eq (component.asInstanceOf[JComponent]).getRootPane) {
      //        myDesigner.getToolProvider.loadDefaultTool
      //      }
    }
  }


  def dispose {
    if (!ApplicationManager.getApplication.isHeadlessEnvironment) {
      DragSource.getDefaultDragSource.removeDragSourceListener(myDragSourceListener)
    }
  }

  def getActiveItem: DoradoPaletteItem = {


    for (groupComponent <- myGroupComponents.toArray) {
      //      if (groupComponent.isSelected) {
      //        val paletteItem: PaletteItem = groupComponent.getItemsComponent.getSelectedValue.asInstanceOf[PaletteItem]
      //        if (paletteItem != null) {
      //          return paletteItem
      //        }
      //      }
    }
    return null
  }


  def clearActiveItem {
    if (getActiveItem != null) {

      //      for (itemsComponent <- myItemsComponents) {
      //        itemsComponent.clearSelection
      //      }
      notifySelection(null)
    }
  }


  def refresh {
    //repaint
  }


  def isEmpty: Boolean = {
    return myGroups.isEmpty
  }


  def loadPalette(designer: DoradoDesignerEditorPanel) {
    if (myDesigner == null && designer == null) {
      return
    }
    if (myDesigner != null && designer != null && (myGroups == designer.getPaletteGroups)) {
      myDesigner = designer
      restoreSelection
      return
    }

    for (i <- 0 until myItemsComponents.size()) {
      myItemsComponents.get(i).removeListSelectionListener(mySelectionListener)
    }
    myDesigner = designer
    myPaletteContainer.removeAll
    if (designer == null) {
      myGroups = new util.ArrayList[DoradoPaletteGroup]
      myGroupComponents = new util.ArrayList[DoradoPaletteGroupComponent]
      myItemsComponents = new util.ArrayList[DoradoPaletteItemsComponent]
    }
    else {
      myGroups = designer.getPaletteGroups
      myGroupComponents = new util.ArrayList[DoradoPaletteGroupComponent]
      myItemsComponents = new util.ArrayList[DoradoPaletteItemsComponent]
    }

    for (i <- 0 until myGroups.size()) {
      val groupComponent: DoradoPaletteGroupComponent = new DoradoPaletteGroupComponent(myGroups.get(i))
      val itemsComponent: DoradoPaletteItemsComponent = new DoradoPaletteItemsComponent(myGroups.get(i), designer)
      groupComponent.setItemsComponent(itemsComponent)
      groupComponent.addFocusListener(myFocusListener)
      myGroupComponents.add(groupComponent)

      itemsComponent.addListSelectionListener(mySelectionListener)
      myItemsComponents.add(itemsComponent)

      myPaletteContainer.add(groupComponent)
      myPaletteContainer.add(itemsComponent)
    }
    myPaletteContainer.revalidate
    if (myDesigner != null) {
      restoreSelection
    }
  }


  private def restoreSelection {
    //    val paletteItem: PaletteItem = myDesigner.getActivePaletteItem
    //
    //
    //    for (itemsComponent <- myItemsComponents) {
    //      itemsComponent.restoreSelection(paletteItem)
    //    }
  }


  private def notifySelection(event: ListSelectionEvent) {
    //    if (event != null) {
    //      val sourceItemsComponent: PaletteItemsComponent = event.getSource.asInstanceOf[PaletteItemsComponent] {
    //        var i: Int = event.getFirstIndex
    //        while (i <= event.getLastIndex) {
    //          {
    //            if (sourceItemsComponent.isSelectedIndex(i)) {
    //
    //              for (itemsComponent <- myItemsComponents) {
    //                if (itemsComponent ne sourceItemsComponent) {
    //                  itemsComponent.clearSelection
    //                }
    //              }
    //              val paletteItem: PaletteItem = sourceItemsComponent.getSelectedValue.asInstanceOf[PaletteItem]
    //              if (paletteItem != null && !paletteItem.isEnabled) {
    //                sourceItemsComponent.clearSelection
    //              }
    //              break //todo: break is not supported
    //            }
    //          }
    //          ({
    //            i += 1;
    //            i - 1
    //          })
    //        }
    //      }
    //    }
    //    if (myDesigner != null) {
    //      myDesigner.activatePaletteItem(getActiveItem)
    //    }
  }


  def getData(dataId: String): AnyRef = {
    //    if (PlatformDataKeys.FILE_EDITOR.is(dataId) && myDesigner != null) {
    //      return myDesigner.getEditor
    //    }
    return null
  }
}
