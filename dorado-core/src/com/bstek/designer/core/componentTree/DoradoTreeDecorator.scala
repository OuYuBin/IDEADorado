package com.bstek.designer.core.componentTree

import com.intellij.designer.componentTree.{AttributeWrapper, TreeComponentDecorator}
import com.intellij.designer.model.RadComponent
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleColoredComponent

import scala.beans.BeanProperty

/**
 * Dorado7视图,模型文件树形结构装饰器
 * Created by robin on 14-3-14.
 */
class DoradoTreeDecorator(@BeanProperty val project: Project) extends TreeComponentDecorator {
  override def decorate(component: RadComponent, renderer: SimpleColoredComponent, wrapper: AttributeWrapper, full: Boolean) {

  }
}
