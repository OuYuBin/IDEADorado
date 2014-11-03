package com.bstek.designer.core

import java.awt.Dimension
import java.awt.event.{ComponentAdapter, ComponentEvent}

import com.bstek.designer.core.componentTree.{DoradoTreeBuilder, DoradoTree}
import com.bstek.designer.core.propertyTable.DoradoPropertyTablePanel
import com.bstek.designer.core.surface.{DoradoPropertySheetToolWindowContent, DoradoDesignerEditorPanel}
import com.intellij.designer.DesignerToolWindowContent
import com.intellij.designer.propertyTable.{PropertyTablePanel, RadPropertyTable}
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Splitter
import com.intellij.openapi.util.Disposer
import com.intellij.ui.{IdeBorderFactory, SideBorder, ScrollPaneFactory}
import com.intellij.util.ui.tree.TreeUtil

import scala.beans.BeanProperty

/**
 *
 * 设计器属性编辑子窗口内容构建,早期直接使用Andriod Studio中设计的DesignerToolWindow,但该类为final,其工具栏无法进行扩展定义,故需要重新定义该类方便自由控制
 * Created by robin on 14-6-28.
 */
abstract class DoradoComponentTreeToolWindowContent(project: Project, updateOrientation: Boolean) extends DoradoPropertySheetToolWindowContent {

  var doradoTreeBuilder: DoradoTreeBuilder = _
  @BeanProperty var doradoTree = new DoradoTree
  val doradoTreeScrollPane = ScrollPaneFactory.createScrollPane(doradoTree)
  doradoTreeScrollPane.setBorder(IdeBorderFactory.createBorder(SideBorder.BOTTOM))
  doradoTreeScrollPane.setPreferredSize(new Dimension(250, -1))
  //  componentTree.initQuickFixManager(treeScrollPane.getViewport())

  //--需要通过子类复写createDoradoPropertyTablePanel来完成关于属性表格的子类化
  var doradoPropertyTablePanel = createDoradoPropertyTablePanel(project)

  val toolWindowPanel = new Splitter(true, 0.42f)
  toolWindowPanel.setDividerWidth(3)
  toolWindowPanel.setShowDividerControls(false)
  toolWindowPanel.setShowDividerIcon(false)
  toolWindowPanel.setFirstComponent(doradoTreeScrollPane)
  toolWindowPanel.setSecondComponent(doradoPropertyTablePanel)

  if (updateOrientation) {
    toolWindowPanel.addComponentListener(new ComponentAdapter() {
      override def componentResized(event: ComponentEvent) {
        val size = toolWindowPanel.getSize()
        val newVertical = size.width < size.height
        if (toolWindowPanel.getOrientation() != newVertical) {
          toolWindowPanel.setOrientation(newVertical)
        }
      }
    })
  }


  def update(designer: DoradoDesignerEditorPanel) {
    clearTreeBuilder
    doradoTree.newModel
    if (designer == null) {
      doradoTree.setDesignerPanel(null)
      doradoPropertyTablePanel.setArea(null, null)
    }
    else {
      doradoTree.setDesignerPanel(designer)
      //--初始化构建TreeBuilder
      doradoTreeBuilder = new DoradoTreeBuilder(doradoTree, designer)
      //--关联树形区域与属性编辑区域
      doradoPropertyTablePanel.setArea(designer, doradoTreeBuilder.getTreeArea())
    }
  }

  override def dispose() {
    clearTreeBuilder()
    toolWindowPanel.dispose()
    doradoTree = null
    doradoPropertyTablePanel = null
  }

  def clearTreeBuilder() {
    if (doradoTreeBuilder != null) {
      Disposer.dispose(doradoTreeBuilder)
      doradoTreeBuilder = null
    }
  }

  def getToolWindowPanel: Splitter = {
    toolWindowPanel
  }

  def createActions(): Array[AnAction] = {


    val expandAll = new AnAction("Expand All", null, AllIcons.Actions.Expandall) {
      override def actionPerformed(event: AnActionEvent) {
        if (doradoTreeBuilder != null) {
          doradoTreeBuilder.expandAll(null)
        }
      }
    }

    val collapseAll = new AnAction("Collapse All", null, AllIcons.Actions.Collapseall) {
      override def actionPerformed(event: AnActionEvent) {
        if (doradoTreeBuilder != null) {
          TreeUtil.collapseAll(doradoTree, 1)
        }
      }
    }

    return Array(expandAll, collapseAll)
  }


  def getPropertyTable(): RadPropertyTable = {
    //return propertyTablePanel.getPropertyTable()
    return null
  }

  override def expandFromState() {
    //    if (componentTreeBuilder != null) {
    //      componentTreeBuilder.expandFromState()
    //    }
  }

  override def refresh(updateProperties: Boolean) {
    if (doradoTreeBuilder != null) {
      if (updateProperties) {
        //doradoTreeBuilder.selectFromSurface()
        doradoTreeBuilder.queueUpdate()
      }
      else {
        doradoTreeBuilder.queueUpdate()
      }
    }
  }

  @Override
  override def updateInspections() {
    //    if (componentTree != null) {
    //      componentTree.updateInspections()
    //    }
    //    if (propertyTablePanel != null) {
    //      propertyTablePanel.getPropertyTable().updateInspections()
    //    }
  }

  def createDoradoPropertyTablePanel(project: Project): DoradoPropertyTablePanel

}
