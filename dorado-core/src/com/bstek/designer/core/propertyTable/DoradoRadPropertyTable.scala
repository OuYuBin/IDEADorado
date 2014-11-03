package com.bstek.designer.core.propertyTable

import java.awt.Dimension
import java.util.Collections
import javax.swing.JViewport
import javax.swing.event.{ListSelectionEvent, ListSelectionListener, TableModelEvent, TableModelListener}

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.codeInsight.daemon.impl.SeverityRegistrar
import com.intellij.designer.designSurface.{DesignerEditorPanel, EditableArea, QuickFixManager, ComponentSelectionListener}
import com.intellij.designer.model._
import com.intellij.designer.propertyTable.{PropertyTableTab, PropertyTablePanel, PropertyTable}
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.actionSystem.{DataProvider, PlatformDataKeys}
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.project.Project
import com.intellij.util.ThrowableRunnable

import scala.beans.BeanProperty

/**
 * Created by robin on 14-7-23.
 */
abstract class DoradoRadPropertyTable(myProject: Project) extends PropertyTable with DataProvider with ComponentSelectionListener {

  @BeanProperty var myArea: EditableArea = null

  @BeanProperty var myDesigner: DoradoDesignerEditorPanel = _


  //private var myQuickFixManager: QuickFixManager = null


  @BeanProperty var myPropertyTablePanel: DoradoPropertyTablePanel =_


  //def this(project: Project) {
  // this()
  setShowVerticalLines(true)
  setIntercellSpacing(new Dimension(1, 1))
  getModel.addTableModelListener(new TableModelListener {
    def tableChanged(e: TableModelEvent) {
      if (myPropertyTablePanel != null) {
        //myPropertyTablePanel.updateActions
      }
    }
  })
  //}


  private final val myListener: ListSelectionListener = new ListSelectionListener {
    def valueChanged(e: ListSelectionEvent) {
      if (myDesigner != null) {
        myDesigner.setSelectionProperty(getCurrentKey, getSelectionProperty.asInstanceOf[Property[_]])
      }
    }
  }


  protected def addSelectionListener {
    getSelectionModel.addListSelectionListener(myListener)
  }


  protected def removeSelectionListener {
    getSelectionModel.removeListSelectionListener(myListener)
  }


  def initQuickFixManager(viewPort: JViewport) {
    //myQuickFixManager = new QuickFixManager(this, viewPort)
  }


  def setPropertyTablePanel(propertyTablePanel: DoradoPropertyTablePanel) {
    myPropertyTablePanel = propertyTablePanel
  }


  def getData(dataId: String): AnyRef = {
    if (PlatformDataKeys.FILE_EDITOR.is(dataId) && myDesigner != null) {
      return myDesigner.getEditor
    }
    return null
  }


  protected def getErrorAttributes(severity: HighlightSeverity): TextAttributesKey = {
    return SeverityRegistrar.getSeverityRegistrar(myProject).getHighlightInfoTypeBySeverity(severity).getAttributesKey
  }


  def setArea(designer: DoradoDesignerEditorPanel, area: EditableArea) {
    myDesigner = designer
    //myQuickFixManager.setDesigner(designer)
    if (myArea != null) {
      myArea.removeSelectionListener(this)
    }
    myArea = area
    if (myArea != null) {
      myArea.addSelectionListener(this)
    }
    update
  }


  def selectionChanged(area: EditableArea) {
    update
  }


  def updateInspections {
    //myQuickFixManager.update
  }

  protected def getCurrentKey: String = {
    val tab: PropertyTableTab = myPropertyTablePanel.getCurrentTab
    return if (tab == null) null else tab.getKey
  }


  protected def doRestoreDefault(runnable: ThrowableRunnable[Exception]): Boolean = {
    return myDesigner.getToolProvider.execute(runnable, "restore", false)
  }


  protected def doSetValue(runnable: ThrowableRunnable[Exception]): Boolean = {
    return myDesigner.getToolProvider.execute(runnable, "Set", false)
  }


  protected override def getPropertyContext: PropertyContext = {
    return myDesigner
  }
}
