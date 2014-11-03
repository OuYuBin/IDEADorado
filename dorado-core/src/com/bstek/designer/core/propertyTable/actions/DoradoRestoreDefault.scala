package com.bstek.designer.core.propertyTable.actions

import com.bstek.designer.core.propertyTable.DoradoRadPropertyTable
import com.intellij.designer.model.{PropertiesContainer, Property}
import com.intellij.designer.propertyTable.actions.IPropertyTableAction
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{Presentation, AnAction, AnActionEvent}

/**
 * Created by robin on 14-7-28.
 */
class DoradoRestoreDefault(table: DoradoRadPropertyTable) extends AnAction with IPropertyTableAction {
  //private final val myTable: RadPropertyTable = null


  //def this (table: RadPropertyTable) {
  //this ()
  //myTable = table
  val presentation: Presentation = getTemplatePresentation
  val text: String = "Restore"
  presentation.setText(text)
  presentation.setDescription(text)
  presentation.setIcon(AllIcons.General.Reset)

  //}


  override def update(e: AnActionEvent) {
    setEnabled(table, e.getPresentation)
  }


  def update {
    setEnabled(table, getTemplatePresentation)
  }


  private def setEnabled(table: DoradoRadPropertyTable, presentation: Presentation) {
//    try {
//      val property: Property[_] = table.getSelectionProperty
//      presentation.setEnabled(property != null && !table.isDefault(property))
//    }
//    catch {DoradoListModel
//      case e: Exception => {
//        presentation.setEnabled(false)
//      }
//    }
    //return false
  }


  def actionPerformed(e: AnActionEvent) {
    table.restoreDefaultValue
    setEnabled(table, getTemplatePresentation)
  }
}
