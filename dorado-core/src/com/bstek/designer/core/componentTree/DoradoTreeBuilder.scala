package com.bstek.designer.core.componentTree

import javax.swing.tree.DefaultTreeModel

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.designer.componentTree.{TreeDropListener, ExpandStateHandler, TreeEditableArea, TreeContentProvider}
import com.intellij.designer.designSurface.{EditableArea, ComponentGlassLayer, ComponentSelectionListener}
import com.intellij.ide.util.treeView.AbstractTreeBuilder

import scala.beans.BeanProperty

/**
 * Created by robin on 14-7-5.
 */
class DoradoTreeBuilder(tree: DoradoTree, designer: DoradoDesignerEditorPanel)
  extends AbstractTreeBuilder(tree, tree.getModel.asInstanceOf[DefaultTreeModel], new DoradoTreeStructure(designer), null)
  with ComponentSelectionListener {

  //private final val mySurfaceArea: EditableArea = null


  //var myTreeArea: TreeEditableArea = _


  //private final val myGlassLayer: ComponentGlassLayer = null


  //private final val myExpandStateHandler: ExpandStateHandler = null


  setPassthroughMode(true)
  setCanYieldUpdate(false)
  initRootNode

  //  val mySurfaceArea = designer.getSurfaceArea
  @BeanProperty val treeArea = new DoradoTreeEditableArea(tree, this)

  //  val myGlassLayer = new ComponentGlassLayer(tree, designer.getToolProvider, myTreeArea)
  //  val myExpandStateHandler = new ExpandStateHandler(tree, designer, this)
  tree.setArea(treeArea)

  //designer.handleTreeArea(myTreeArea)
  //  new TreeDropListener(tree, myTreeArea, designer.getToolProvider)
  //  selectFromSurface
  //  expandFromState
  //  addListeners
  //  myExpandStateHandler.hookListener


  //  def getTreeArea: TreeEditableArea = {
  //    return myTreeArea
  //  }
  //
  //
  //  override def dispose {
  //    removeListeners
  //    myTreeArea.unhookSelection
  //    myGlassLayer.dispose
  //    myExpandStateHandler.unhookListener
  //    super.dispose
  //  }
  //
  //
  //  private def addListeners {
  //    mySurfaceArea.addSelectionListener(this)
  //    myTreeArea.addSelectionListener(this)
  //  }
  //
  //
  //  private def removeListeners {
  //    mySurfaceArea.removeSelectionListener(this)
  //    myTreeArea.removeSelectionListener(this)
  //  }
  //
  //
  def selectionChanged(area: EditableArea) {
    //    try {
    //      removeListeners
    //      if (mySurfaceArea eq area) {
    //        try {
    //          myTreeArea.setCanvasSelection(true)
    //          myTreeArea.setSelection(mySurfaceArea.getSelection)
    //        }
    //        finally {
    //          myTreeArea.setCanvasSelection(false)
    //        }
    //      }
    //      else {
    //        mySurfaceArea.setSelection(myTreeArea.getSelection)
    //        mySurfaceArea.scrollToSelection
    //      }
    //    }
    //    finally {
    //      addListeners
    //    }
  }

  //
  //
  def selectFromSurface {
    //treeArea.setSelection(mySurfaceArea.getSelection)
  }

  //
  //
  //    def expandFromState {
  //      expand(myExpandStateHandler.getExpanded, null)
  //    }
}