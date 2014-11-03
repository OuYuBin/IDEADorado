package com.bstek.designer.core.surface

import com.bstek.designer.core.DoradoToolWindowContent
import com.intellij.openapi.project.Project

/**
 *
 * 定义关于属性内容管理
 * Created by robin on 14-7-6.
 */
trait DoradoPropertySheetToolWindowContent extends DoradoToolWindowContent{

  def refresh(updateProperties: Boolean)

  def expandFromState

  def updateInspections
}
