package com.bstek.designer.core.propertyTable

import java.awt.event.{MouseEvent, MouseAdapter}
import java.awt.{FlowLayout, Insets, GridBagConstraints, GridBagLayout}
import javax.swing.{JComponent, JScrollPane, JLabel, JPanel}
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}

import com.bstek.designer.core.propertyTable.actions.DoradoRestoreDefault
import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.designer.designSurface.EditableArea
import com.intellij.designer.propertyTable.{TablePanelActionPolicy, PropertyTableTab, RadPropertyTable}
import com.intellij.designer.propertyTable.actions._
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.ui.{IdeBorderFactory, SideBorder, ScrollPaneFactory, PopupHandler}
import com.intellij.util.ArrayUtil
import com.intellij.util.ui.UIUtil

import scala.beans.BeanProperty

/**
 * 属性编辑表格面板抽象构建
 * Created by robin on 14-7-6.
 */
abstract class DoradoPropertyTablePanel(@BeanProperty val project: Project) extends JPanel with ListSelectionListener {

  private final val BUTTON_KEY: String = "SWING_BUTTON_KEY"


  //private final val myPropertyTable: RadPropertyTable = null


  //rivate final val myActions: Array[AnAction] = null


  //private final val myTabPanel: JPanel = null


  //private final val myActionPanel: JPanel = null


  private var myTabs: Array[PropertyTableTab] = null

  private var myCurrentTab: PropertyTableTab = null
  //
  //
  //    private var myActionPolicy: TablePanelActionPolicy = null
  //
  //
  //    private final val myTitleLabel: JLabel = null


  //def this(project: Project) {
  //  this()
  //--属性表格
  val myPropertyTable = createDoradoPropertyTable
  //--设置布局
  setLayout(new GridBagLayout)
  var gridX: Int = 0
  //--设定标题
  val titleLabel = new JLabel("Properties")
  titleLabel.setFont(UIUtil.getLabelFont(UIUtil.FontSize.SMALL))
  //--设定布局约束
  add(titleLabel, new GridBagConstraints(({
    gridX += 1;
    gridX - 1
  }), 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 10), 0, 0))

  val actionManager: ActionManager = ActionManager.getInstance
  val actionGroup: DefaultActionGroup = new DefaultActionGroup
  //  //val showJavadoc: ShowJavadoc = new ShowJavadoc(myPropertyTable)
  //  showJavadoc.registerCustomShortcutSet(actionManager.getAction(IdeActions.ACTION_QUICK_JAVADOC).getShortcutSet, myPropertyTable)
  //  actionGroup.add(showJavadoc)
  //  actionGroup.addSeparator
  val restoreDefault: DoradoRestoreDefault = new DoradoRestoreDefault(myPropertyTable)
  restoreDefault.registerCustomShortcutSet(actionManager.getAction(IdeActions.ACTION_DELETE).getShortcutSet, myPropertyTable)
  actionGroup.add(restoreDefault)
  //  actionGroup.add(new ShowExpert(myPropertyTable))
  val myTabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0))
  add(myTabPanel, new GridBagConstraints(({
    gridX += 1;
    gridX - 1
  }), 0, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 0, 2, 0), 0, 0))
  val myActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0))
  add(myActionPanel, new GridBagConstraints(({
    gridX += 1;
    gridX - 1
  }), 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 2, 2), 0, 0))
  val myActions = actionGroup.getChildren(null)
  for (action <- myActions) {
    if (!action.isInstanceOf[Separator]) {
      val presentation: Presentation = action.getTemplatePresentation
      val button: ActionButton = new ActionButton(action, presentation, ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE)
      myActionPanel.add(button)
      presentation.putClientProperty(BUTTON_KEY, button)
    }
  }
  //  actionGroup.add(new ShowColumns(myPropertyTable))
  //  PopupHandler.installPopupHandler(myPropertyTable, actionGroup, ActionPlaces.GUI_DESIGNER_PROPERTY_INSPECTOR_POPUP, actionManager)
  myPropertyTable.getSelectionModel.addListSelectionListener(this)

  //  valueChanged(null)
  val scrollPane: JScrollPane = ScrollPaneFactory.createScrollPane(myPropertyTable)
  scrollPane.setBorder(IdeBorderFactory.createBorder(SideBorder.TOP))
  myPropertyTable.initQuickFixManager(scrollPane.getViewport)
  add(scrollPane, new GridBagConstraints(0, 1, gridX, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0))
  myPropertyTable.setPropertyTablePanel(this)
  addMouseListener(new MouseAdapter {
    override def mouseReleased(e: MouseEvent) {
      IdeFocusManager.getInstance(project).requestFocus(myPropertyTable, true)
    }
  })


  def setArea(designer: DoradoDesignerEditorPanel, area: EditableArea) {
    //        val tabs: Array[PropertyTableTab] = if (designer == null) null : designer.getPropertyTableTabs
    //        if (!Comparing.equal(myTabs, tabs)) {
    //          myTabs = tabs
    //          myTabPanel.removeAll
    //          if (tabs != null && tabs.length > 1) {
    //            if (!ArrayUtil.contains(myCurrentTab, tabs)) {
    //              myCurrentTab = tabs(0)
    //            }
    //            for (tab <- tabs) {
    //              myTabPanel.add(new TableTabAction(this, tab).getButton)
    //            }
    //          }
    //          else {
    //            myCurrentTab = null
    //          }
    //          myTitleLabel.setVisible(myCurrentTab == null)
    //          myTabPanel.revalidate
    //        }
    //        val policy: TablePanelActionPolicy = if (designer == null) TablePanelActionPolicy.EMPTY else designer.getTablePanelActionPolicy
    //        if (!Comparing.equal(myActionPolicy, policy)) {
    //          myActionPolicy = policy
    //          for (action <- myActions) {
    //            if (action.isInstanceOf[Separator]) {
    //              continue //todo: continue is not supported
    //            }
    //            val visible: Boolean = policy.showAction(action)
    //            val presentation: Presentation = action.getTemplatePresentation
    //            presentation.setVisible(visible)
    //            val button: JComponent = presentation.getClientProperty(BUTTON_KEY).asInstanceOf[JComponent]
    //            if (button != null) {
    //              button.setVisible(visible)
    //            }
    //          }
    //          myActionPanel.revalidate
    //        }
    //--加入监听的节奏
    myPropertyTable.setArea(designer, area)
  }


  def getPropertyTable: DoradoRadPropertyTable = {
    return myPropertyTable
  }

  def getCurrentTab: PropertyTableTab = {
    return myCurrentTab
  }


  def setCurrentTab(currentTab: PropertyTableTab) {
    myCurrentTab = currentTab
    for (component <- myTabPanel.getComponents) {
      val button: ActionButton = component.asInstanceOf[ActionButton]
      val action: TableTabAction = button.getAction.asInstanceOf[TableTabAction]
      action.updateState
    }
    myPropertyTable.update
  }


  def valueChanged(e: ListSelectionEvent) {
    //updateActions
  }


  def updateActions {
    for (action <- myActions) {
      if (action.isInstanceOf[IPropertyTableAction]) {
        (action.asInstanceOf[IPropertyTableAction]).update
      }
    }
  }

  def createDoradoPropertyTable: DoradoRadPropertyTable
}