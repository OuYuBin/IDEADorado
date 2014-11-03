package com.bstek.designer.core.componentTree

import java.awt.{Graphics, Component, Insets, Color}
import java.awt.event.{ActionEvent, MouseEvent}
import javax.swing.border.LineBorder
import javax.swing.plaf.TreeUI
import javax.swing._
import javax.swing.tree.{DefaultTreeModel, TreePath, DefaultMutableTreeNode}

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInsight.daemon.impl.SeverityRegistrar
import com.intellij.designer.actions.{DesignerActionPanel, StartInplaceEditing}
import com.intellij.designer.componentTree.{AttributeWrapper, QuickFixManager, TreeNodeDescriptor}
import com.intellij.designer.designSurface.DesignerEditorPanel.ErrorInfo
import com.intellij.designer.designSurface.{FeedbackTreeLayer, EditableArea}
import com.intellij.designer.model.RadComponent
import com.intellij.facet.frameworks.actions.AbstractAction
import com.intellij.openapi.actionSystem.{PlatformDataKeys, DataProvider}
import com.intellij.openapi.editor.colors.{EditorColorsManager, TextAttributesKey}
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.ui.{ColoredTreeCellRenderer, SimpleTextAttributes}
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil

/**
 * 树型构造器
 * Created by robin on 14-7-5.
 */
class DoradoTree extends Tree with DataProvider {
  //  private final val myInplaceEditingAction: StartInplaceEditing = null
  //
  //
  //  private var myQuickFixManager: QuickFixManager = null
  //
  //
  private var myDesigner: DoradoDesignerEditorPanel = _
  //
  //
  var area: DoradoTreeEditableArea = _
  //
  //
  //  private var myMarkComponent: RadComponent = null
  //
  //
  //  private var myMarkFeedback: Int = 0
  //
  //
  ////  def this() {
  ////    this()
  newModel
  setScrollsOnExpand(true)
  installCellRenderer
  setRootVisible(false)
  setShowsRootHandles(true)

  //    ToolTipManager.sharedInstance.registerComponent(this)
  //    TreeUtil.installActions(this)
  //    //val myInplaceEditingAction = DesignerActionPanel.createInplaceEditingAction(this)
  //  //}
  //
  //
  //  override def setUI(ui: TreeUI) {
  ////    super.setUI(ui)
  ////    getActionMap.put("selectAll", new AbstractAction {
  ////      def actionPerformed(e: ActionEvent) {
  ////        if (myDesigner != null) {
  ////          myDesigner.getActionPanel.createSelectAllAction(myDesigner.getSurfaceArea).actionPerformed(null)
  ////        }
  ////      }
  ////    })
  //  }
  //
  //
  def newModel {
    setModel(new DefaultTreeModel(new DefaultMutableTreeNode))
  }

  //
  //
  //  def initQuickFixManager(viewPort: JViewport) {
  //    myQuickFixManager = new QuickFixManager(this, viewPort)
  //  }
  //
  //
  //  def updateInspections {
  //    myQuickFixManager.update
  //  }
  //
  //
  def setDesignerPanel(designer: DoradoDesignerEditorPanel) {
    myDesigner = designer
    //      myMarkComponent = null
    //      myArea = null
    //      myInplaceEditingAction.setDesignerPanel(designer)
    //      myQuickFixManager.setDesigner(designer)
  }

  //
  //
  def setArea(area: DoradoTreeEditableArea) {
    this.area = area
    //myQuickFixManager.setEditableArea(area)
  }

  //
  //
  //  def mark(component: RadComponent, feedback: Int) {
  //    myMarkComponent = component
  //    myMarkFeedback = feedback
  //    repaint
  //  }
  //
  //
  def getData(dataId: String): AnyRef = {
    //    if (EditableArea.DATA_KEY.is(dataId)) {
    //      return myArea
    //    }
    //    if (myDesigner != null) {
    //      if (PlatformDataKeys.FILE_EDITOR.is(dataId)) {
    //        return myDesigner.getEditor
    //      }
    //      return myDesigner.getActionPanel.getData(dataId)
    //    }
    return null
  }

  //
  def extractComponent(value: AnyRef): RadComponent = {
    val node: DefaultMutableTreeNode = value.asInstanceOf[DefaultMutableTreeNode]
    val userObject: AnyRef = node.getUserObject
    if (myDesigner != null && userObject.isInstanceOf[TreeNodeDescriptor]) {
      val descriptor: TreeNodeDescriptor = userObject.asInstanceOf[TreeNodeDescriptor]
      val element: AnyRef = descriptor.getElement
      if (element.isInstanceOf[RadComponent]) {
        return element.asInstanceOf[RadComponent]
      }
    }
    return null
  }

  //
  //
  //  def getEdgeSize: Int = {
  //    return Math.max(5, (getCellRenderer.asInstanceOf[JComponent]).getPreferredSize.height / 2 - 3)
  //  }
  //
  //
  //  override def getToolTipText(event: MouseEvent): String = {
  ////    val path: TreePath = getPathForLocation(event.getX, event.getY)
  ////    if (path != null) {
  ////      val component: RadComponent = extractComponent(path.getLastPathComponent)
  ////      if (component != null) {
  ////        val errorInfos: List[ErrorInfo] = RadComponent.getError(component)
  ////        if (!errorInfos.isEmpty) {
  ////          return errorInfos.get(0).getName
  ////        }
  ////      }
  ////    }
  //    return super.getToolTipText(event)
  //  }
  //
  //   private def getHighlightDisplayLevel(project: Project, component: RadComponent): HighlightDisplayLevel = {
  //    var displayLevel: HighlightDisplayLevel = null
  //    val severityRegistrar: SeverityRegistrar = SeverityRegistrar.getSeverityRegistrar(project)
  //
  //    import java.awt.Color
  //    import java.awt.Insets
  //    import javax.swing.border.LineBorder
  //
  //    import com.intellij.designer.designSurface.FeedbackTreeLayer
  //
  //    import scala.collection.JavaConversions._
  //
  //    for (errorInfo <- RadComponent.getError(component)) {
  //      if (displayLevel == null || severityRegistrar.compare(errorInfo.getLevel.getSeverity, displayLevel.getSeverity) > 0) {
  //        displayLevel = errorInfo.getLevel
  //      }
  //    }
  //    return displayLevel
  //  }
  //
  //
  private def getAttributeWrapper(component: RadComponent): AttributeWrapper = {
    var wrapper: AttributeWrapper = AttributeWrapper.DEFAULT
    //      val level: HighlightDisplayLevel = getHighlightDisplayLevel(myDesigner.getProject, component)
    //      if (level != null) {
    //        val attributesKey: TextAttributesKey = SeverityRegistrar.getSeverityRegistrar(myDesigner.getProject).getHighlightInfoTypeBySeverity(level.getSeverity).getAttributesKey
    //        val textAttributes: TextAttributes = EditorColorsManager.getInstance.getGlobalScheme.getAttributes(attributesKey)
    //        wrapper = new AttributeWrapper {
    //          def getAttribute(attributes: SimpleTextAttributes): SimpleTextAttributes = {
    //            val bgColor: Color = textAttributes.getBackgroundColor
    //            try {
    //              textAttributes.setBackgroundColor(null)
    //              return SimpleTextAttributes.fromTextAttributes(TextAttributes.merge(attributes.toTextAttributes, textAttributes))
    //            }
    //            finally {
    //              textAttributes.setBackgroundColor(bgColor)
    //            }
    //          }
    //        }
    //      }
    return wrapper
  }

  private def installCellRenderer {
    setCellRenderer(new ColoredTreeCellRenderer {
      def customizeCellRenderer(tree: JTree, value: AnyRef, selected: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean) {
        try {
          val component: RadComponent = extractComponent(value)
          if (component != null) {
            myDesigner.getTreeDecorator.decorate(component, this, getAttributeWrapper(component), true)
            //              if (myMarkComponent eq component) {
            //                if (myMarkFeedback == FeedbackTreeLayer.INSERT_SELECTION) {
            //                  setBorder(BorderFactory.createLineBorder(Color.RED, 1))
            //                }
            //                else {
            //                  setBorder(new InsertBorder(myMarkFeedback))
            //                }
            //              }
            //              else {
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1))
            //}
          }
        }
        catch {
          case e: RuntimeException => {
            if (myDesigner == null) {
              throw e
            }
            //myDesigner.showError("Tree paint operation", e)
          }
        }
      }
    })
  }

  //
  //  private class InsertBorder(myMode:Int) extends LineBorder(Color.BLACK,2) {
  ////    def this(mode: Int) {
  ////      this()
  ////      `super`(Color.BLACK, 2)
  ////      myMode = mode
  ////    }
  //
  //    override def getBorderInsets(component: Component): Insets = {
  //      return getBorderInsets(component, new Insets(0, 0, 0, 0))
  //    }
  //
  //    override def getBorderInsets(component: Component, insets: Insets): Insets = {
  //      insets.top = if (myMode == FeedbackTreeLayer.INSERT_BEFORE) thickness else 0
  //      insets.left = ({
  //        insets.right = thickness;
  //        insets.right
  //      })
  //      insets.bottom = if (myMode == FeedbackTreeLayer.INSERT_AFTER) thickness else 0
  //      return insets
  //    }
  //
  //    override def paintBorder(c: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {
  //      val oldColor: Color = g.getColor
  //      g.setColor(getLineColor)
  //      if (myMode == FeedbackTreeLayer.INSERT_BEFORE) {
  //        g.fillRect(x, y, width, thickness)
  //        g.fillRect(x, y, thickness, 2 * thickness)
  //        g.fillRect(x + width - thickness, y, thickness, 2 * thickness)
  //      }
  //      else {
  //        g.fillRect(x, y + height - thickness, width, thickness)
  //        g.fillRect(x, y + height - 2 * thickness, thickness, 2 * thickness)
  //        g.fillRect(x + width - thickness, y + height - 2 * thickness, thickness, 2 * thickness)
  //      }
  //      g.setColor(oldColor)
  //    }
  //
  //    //private final val myMode: Int = 0
  //  }

}