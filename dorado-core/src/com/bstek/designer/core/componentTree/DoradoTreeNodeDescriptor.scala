package com.bstek.designer.core.componentTree

import com.intellij.ide.util.treeView.NodeDescriptor

import scala.beans.BeanProperty


/**
 * Created by robin on 14-7-11.
 */
class DoradoTreeNodeDescriptor(parentDescriptor: NodeDescriptor[_], val element: Nothing,
                               name: String) extends NodeDescriptor(null, parentDescriptor) {


  override def getElement: Nothing = {
    element
  }

  def update: Boolean = {
    return true
  }
}

