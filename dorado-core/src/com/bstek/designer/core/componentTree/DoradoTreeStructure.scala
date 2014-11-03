package com.bstek.designer.core.componentTree

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.designer.componentTree.TreeNodeDescriptor
import com.intellij.designer.model.RadComponent
import com.intellij.ide.util.treeView.{NodeDescriptor, AbstractTreeStructure}

/**
 * 树形结构构造对象
 * Created by robin on 14-7-10.
 */
class DoradoTreeStructure(designer: DoradoDesignerEditorPanel) extends AbstractTreeStructure {

  var treeRoot: AnyRef = new Object

  override def getRootElement: AnyRef = {
    treeRoot
  }

  override def getParentElement(element: scala.Any): AnyRef = {
    if (element.isInstanceOf[RadComponent]) {
      val component = element.asInstanceOf[RadComponent]
      return component.getParent()
    }
    return null
  }

  override def getChildElements(element: scala.Any): Array[AnyRef] = {
    if (element == treeRoot) {
      return designer.getTreeRoots
    }
    if (element.isInstanceOf[RadComponent]) {
      val component = element.asInstanceOf[RadComponent]
      return component.getTreeChildren()
    }
    throw new IllegalArgumentException("Unknown element: " + element)
  }

  override def createDescriptor(element: scala.Any, parentDescriptor: NodeDescriptor[_]): NodeDescriptor[_] = {
    if (element == treeRoot || element.isInstanceOf[RadComponent]) {
      val descriptor: TreeNodeDescriptor = new TreeNodeDescriptor(parentDescriptor, element)
      descriptor.setWasDeclaredAlwaysLeaf(isAlwaysLeaf(element))
      return descriptor
    }


    throw new IllegalArgumentException("Unknown element: " + element)
  }

  override def hasSomethingToCommit: Boolean = {
    false
  }

  override def commit(): Unit = ???
}
