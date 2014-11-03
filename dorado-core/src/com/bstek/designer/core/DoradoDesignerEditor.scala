package com.bstek.designer.core

import java.beans.PropertyChangeListener
import javax.swing.JComponent

import com.bstek.designer.core.surface.DoradoDesignerEditorPanel
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.fileEditor.{FileEditor, FileEditorLocation, FileEditorState, FileEditorStateLevel}
import com.intellij.openapi.module.{Module, ModuleUtilCore}
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import org.jetbrains.annotations.Nullable

/**
 * 主编辑器基类
 * Created by robin on 13-12-5.
 */
abstract class DoradoDesignerEditor(project: Project, val file: VirtualFile) extends UserDataHolderBase with FileEditor {

  var originalFile:VirtualFile = file
  if (file.isInstanceOf[LightVirtualFile]) {
    originalFile = (file.asInstanceOf[LightVirtualFile]).getOriginalFile
  }
  val module: Module = findModule(project, originalFile)
  if (module == null) {
    throw new IllegalArgumentException("No module for file " + file + " in project " + project)
  }
  //--设计器面板
  val doradoDesignerPanel = createDoradoDesignerPanel(project, module, file)


  //--子类需要尝试实现设计器面板
  protected def createDoradoDesignerPanel(project: Project, module: Module, file: VirtualFile): DoradoDesignerEditorPanel


  def getDoradoDesignerPanel: DoradoDesignerEditorPanel = {
    return doradoDesignerPanel
  }

  final def getComponent: JComponent = {
    return doradoDesignerPanel
  }

  @Nullable protected def findModule(project: Project, file: VirtualFile): Module = {
    return ModuleUtilCore.findModuleForFile(file, project)
  }

  final def getPreferredFocusedComponent: JComponent = {
    //return doradoDesignerPanel.getPreferredFocusedComponent
    return null
  }

  override def getName: String = {
    return "Designer"
  }

  def dispose {
    //doradoDesignerPanel.dispose
  }


  def selectNotify {
    doradoDesignerPanel.activate
  }


  def deselectNotify {
   // doradoDesignerPanel.deactivate
  }


  def isValid: Boolean = {
    //return doradoDesignerPanel.isEditorValid
    return true
  }


  def isModified: Boolean = {
    return false
  }

   def getState( level: FileEditorStateLevel): FileEditorState = {
    return doradoDesignerPanel.createState
  }


  def setState( state: FileEditorState) {
  }


  def addPropertyChangeListener(listener: PropertyChangeListener) {
  }


  def removePropertyChangeListener(listener: PropertyChangeListener) {
  }


  def getCurrentLocation: FileEditorLocation = {
    return null
  }


  def getStructureViewBuilder: StructureViewBuilder = {
    return null
  }

}
