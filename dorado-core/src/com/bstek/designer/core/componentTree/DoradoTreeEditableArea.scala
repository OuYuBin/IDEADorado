package com.bstek.designer.core.componentTree

import java.awt.{Rectangle, Cursor}
import java.util
import javax.swing.JComponent
import javax.swing.event.{TreeSelectionEvent, EventListenerList, TreeSelectionListener}
import javax.swing.tree.{DefaultMutableTreeNode, TreePath}

import com.intellij.designer.designSurface.tools.InputTool
import com.intellij.designer.designSurface._
import com.intellij.designer.model.RadComponent
import com.intellij.ide.util.treeView.AbstractTreeBuilder
import com.intellij.openapi.actionSystem.{ActionGroup, ActionPlaces}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.ArrayUtil

/**
 * 树形编辑区域实现对象,组织自身与编辑器选择等动作相应接口通讯实现
 * Created by robin on 14-7-15.
 */
class DoradoTreeEditableArea(tree: DoradoTree, treeBuilder: AbstractTreeBuilder) extends EditableArea with FeedbackTreeLayer with TreeSelectionListener {
  private final val myListenerList: EventListenerList = new EventListenerList


  //private final val tree: ComponentTree = null


  //private final val treeBuilder: AbstractTreeBuilder = null


  //private final val myActionPanel: DesignerActionPanel = null


  private var myCanvasSelection: Boolean = false


  //def this(tree: ComponentTree, treeBuilder: AbstractTreeBuilder, actionPanel: DesignerActionPanel) {
  //  this()
  //  tree = tree
  //  treeBuilder = treeBuilder
  //  myActionPanel = actionPanel
  hookSelection

  //}


  private def hookSelection {
    tree.getSelectionModel.addTreeSelectionListener(this)
  }


  def unhookSelection {
    tree.getSelectionModel.removeTreeSelectionListener(this)
  }


  def addSelectionListener(listener: ComponentSelectionListener) {
    myListenerList.add(classOf[ComponentSelectionListener], listener)
  }


  def removeSelectionListener(listener: ComponentSelectionListener) {
    myListenerList.remove(classOf[ComponentSelectionListener], listener)
  }


  private def fireSelectionChanged {
    for (listener <- myListenerList.getListeners(classOf[ComponentSelectionListener])) {
      listener.selectionChanged(this)
    }
  }


  def valueChanged(e: TreeSelectionEvent) {
    if (!treeBuilder.isSelectionBeingAdjusted && ApplicationManager.getApplication.isDispatchThread) {
      fireSelectionChanged
    }
  }

  def getSelection: java.util.List[RadComponent] = {
    return new util.ArrayList[RadComponent](getRawSelection)
  }


  def isSelected(component: RadComponent): Boolean = {
    return getRawSelection.contains(component)
  }


  def select(component: RadComponent) {
    setRawSelection(component)
  }


  def deselect(component: RadComponent) {
    val selection: util.Collection[RadComponent] = getRawSelection
    selection.remove(component)
    setRawSelection(selection)
  }


  def appendSelection(component: RadComponent) {
    val selection: util.Collection[RadComponent] = getRawSelection
    selection.add(component)
    setRawSelection(selection)
  }


  def setSelection(components: java.util.List[RadComponent]) {
    setRawSelection(components)
  }


  def deselect(components: util.Collection[RadComponent]) {
    val selection: util.Collection[RadComponent] = getRawSelection
    selection.removeAll(components)
    setRawSelection(selection)
  }


  def deselectAll {
    setRawSelection(null)
  }


  def scrollToSelection {
  }


  private def getRawSelection: util.Collection[RadComponent] = {
    return treeBuilder.getSelectedElements(classOf[RadComponent])
  }


  private def setRawSelection(value: AnyRef) {
    unhookSelection
    treeBuilder.queueUpdate
    if (value == null) {
      treeBuilder.select(ArrayUtil.EMPTY_OBJECT_ARRAY, null)
      tree.clearSelection
    }
    else if (value.isInstanceOf[RadComponent]) {
      treeBuilder.select(value)
    }
    else {
      val collection: util.Collection[_] = value.asInstanceOf[util.Collection[_]]
      treeBuilder.select(collection.toArray, null)
      if (collection.isEmpty) {
        tree.clearSelection
      }
    }
    treeBuilder.queueUpdate
    hookSelection
    fireSelectionChanged
  }


  def isCanvasSelection: Boolean = {
    return myCanvasSelection
  }


  def setCanvasSelection(canvasSelection: Boolean) {
    myCanvasSelection = canvasSelection
  }


  def setCursor(cursor: Cursor) {
    tree.setCursor(cursor)
  }


  def setDescription(text: String) {
  }

  def getNativeComponent: JComponent = {
    return tree
  }


  def findTarget(x: Int, y: Int, filter: ComponentTargetFilter): RadComponent = {
//    val path: TreePath = tree.getPathForLocation(x, y)
//    if (path != null) {
//      var component: RadComponent = tree.extractComponent(path.getLastPathComponent)
//      if (filter != null) {
//        while (component != null) {
//          if (filter.preFilter(component) && filter.resultFilter(component)) {
//            break //todo: break is not supported
//          }
//          component = component.getParent
//        }
//      }
//      return component
//    }
    return null
  }


  def findTargetTool(x: Int, y: Int): InputTool = {
    return null
  }


  def showSelection(value: Boolean) {
  }


  def getRootSelectionDecorator: ComponentDecorator = {
    return null
  }


  def processRootOperation(context: OperationContext): EditOperation = {
    return null
  }


  def getFeedbackLayer: FeedbackLayer = {
    return null
  }


  def getRootComponent: RadComponent = {
    return null
  }


  def isTree: Boolean = {
    return true
  }


  def getFeedbackTreeLayer: FeedbackTreeLayer = {
    return this
  }


  def getPopupActions: ActionGroup = {
    return null
    //return myActionPanel.getPopupActions(this)
  }


  def getPopupPlace: String = {
    return ActionPlaces.GUI_DESIGNER_COMPONENT_TREE_POPUP
  }


  private def getPath(component: RadComponent): TreePath = {
    val node: DefaultMutableTreeNode = treeBuilder.getNodeForElement(component)
    return if (node == null) null else new TreePath(node.getPath)
  }


  def mark(component: RadComponent, feedback: Int) {
//    if (component != null) {
//      val path: TreePath = getPath(component)
//      if (feedback == INSERT_SELECTION) {
//        tree.scrollPathToVisible(path)
//        if (!tree.isExpanded(path)) {
//          treeBuilder.expand(component, null)
//        }
//      }
//      else {
//        tree.scrollRowToVisible(tree.getRowForPath(path) + (if (feedback == INSERT_BEFORE) -1 else 1))
//      }
//    }
//    tree.mark(component, feedback)
  }


  def unmark {
    //tree.mark(null, -1)
  }


  def isBeforeLocation(component: RadComponent, x: Int, y: Int): Boolean = {
//    val bounds: Rectangle = tree.getPathBounds(getPath(component))
//    return bounds != null && y - bounds.y < tree.getEdgeSize
    return true
  }


  def isAfterLocation(component: RadComponent, x: Int, y: Int): Boolean = {
//    val bounds: Rectangle = tree.getPathBounds(getPath(component))
//    return bounds != null && bounds.getMaxY - y < tree.getEdgeSize
    return true
  }
}
