package com.bstek.designer.core

import com.intellij.ide.palette.{PaletteGroup, PaletteItemProvider}
import com.intellij.openapi.project.Project
import java.beans.{PropertyChangeSupport, PropertyChangeEvent, PropertyChangeListener}
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.{PsiFile, PsiManager}
import com.bstek.designer.core.config.Dorado7RulesConfigImpl

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-11-13
 * Time: 下午3:34
 * To change this template use File | Settings | File Templates.
 */
abstract class DoradoPaletteProvider(@scala.beans.BeanProperty val project: Project) extends PaletteItemProvider {

  def removeListener(p1: PropertyChangeListener)

  def addListener(p1: PropertyChangeListener)

  def getActiveGroups(virtualFile: VirtualFile): Array[PaletteGroup] = {
    val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
    return getActiveGroups(psiFile)
  }

  def getActiveGroups(psi: PsiFile): Array[PaletteGroup]

}
