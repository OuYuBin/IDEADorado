package com.bstek.designer.core.surface

import java.awt.Cursor
import java.util
import javax.swing.JComponent

import com.intellij.designer.designSurface._
import com.intellij.designer.designSurface.tools.InputTool
import com.intellij.designer.model.RadComponent
import com.intellij.openapi.actionSystem.{ActionGroup, DataKey}

/**
 * Created by robin on 14-7-15.
 */
trait IEditableArea {

  val DATA_KEY: DataKey[IEditableArea] = DataKey.create("DoradoEditableArea")

  def addSelectionListener(listener: ComponentSelectionListener)

  def removeSelectionListener(listener: ComponentSelectionListener)

  def getSelection: List[RadComponent]

  def isSelected(component: RadComponent): Boolean

  def select(component: RadComponent)

  def deselect(component: RadComponent)

  def appendSelection(component: RadComponent)

  def setSelection(components: List[RadComponent])

  def deselect(components: util.Collection[RadComponent])

  def deselectAll

  def scrollToSelection

  def setCursor(cursor: Cursor)

  def setDescription(text: String)

  def getNativeComponent: JComponent

  def findTarget(x: Int, y: Int, filter: ComponentTargetFilter): RadComponent

  def findTargetTool(x: Int, y: Int): InputTool

  def showSelection(value: Boolean)

  def getRootSelectionDecorator: ComponentDecorator

  def processRootOperation(context: OperationContext): EditOperation

  def getFeedbackLayer: FeedbackLayer

  def getRootComponent: RadComponent

  def isTree: Boolean

  def getFeedbackTreeLayer: FeedbackTreeLayer

  def getPopupActions: ActionGroup

  def getPopupPlace: String
}
