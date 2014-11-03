package com.bstek.designer.core.surface

import com.intellij.designer.designSurface.DesignerEditorPanel
import com.intellij.openapi.application.ModalityState
import com.intellij.psi.{PsiTreeChangeEvent, PsiManager, PsiFile, PsiTreeChangeAdapter}
import com.intellij.util.Alarm
import com.intellij.util.containers.ComparatorUtil

/**
 * Created by robin on 14-7-11.
 */
class DoradoExternalPSIChangeListener(val designer: DoradoDesignerEditorPanel, val file: PsiFile, val delayMillis: Int, val runnable: Runnable) extends PsiTreeChangeAdapter {

  private final val alarm: Alarm = new Alarm
  //protected final val myDesigner: DesignerEditorPanel = null
  //private final val myFile: PsiFile = null
  //private final val myDelayMillis: Int = 0
  //private final val myRunnable: Runnable = null
  //@volatile
  protected var myRunState: Boolean = false
  //@volatile
  private var myInitialize: Boolean = false
  private var myContent: String = null
  protected var myUpdateRenderer: Boolean = false

  //def this(designer: DesignerEditorPanel, file: PsiFile, delayMillis: Int, runnable: Runnable) {
  //this()
  //myDesigner = designer
  //myFile = file
  //myDelayMillis = delayMillis
  //myRunnable = runnable
  //myContent = myDesigner.getEditorText
  PsiManager.getInstance(designer.getProject).addPsiTreeChangeListener(this)

  //}

  def setInitialize {
    myInitialize = true
  }

  def start {
    if (!myRunState) {
      myRunState = true
    }
  }

  def dispose {
    PsiManager.getInstance(designer.getProject).removePsiTreeChangeListener(this)
    stop
  }

  def stop {
    if (myRunState) {
      myRunState = false
      clear
    }
  }

  def activate {
    if (!myRunState) {
      start
      if (!ComparatorUtil.equalsNullable(myContent, designer.getEditorText) || designer.getRootComponent == null) {
        myUpdateRenderer = false
        addRequest
      }
      myContent = null
    }
  }

  def deactivate {
    if (myRunState) {
      stop
      myContent = designer.getEditorText
    }
    myUpdateRenderer = false
  }

  def addRequest {
    addRequest(runnable)
  }

  def isActive: Boolean = {
    return myRunState
  }

  def isUpdateRenderer: Boolean = {
    return myUpdateRenderer
  }

  def ensureUpdateRenderer: Boolean = {
    if (myRunState) {
      return myInitialize && !designer.isProjectClosed
    }
    myUpdateRenderer = true
    return false
  }

  def addRequest(runnable: Runnable) {
    clear
    alarm.addRequest(new Runnable {
      def run {
        if (myRunState && myInitialize && !designer.isProjectClosed) {
          runnable.run
        }
      }
    }, delayMillis, ModalityState.stateForComponent(designer))
  }

  def clear {
    alarm.cancelAllRequests
  }

  protected def updatePsi(event: PsiTreeChangeEvent) {
    if (myRunState && file==event.getFile) {
      addRequest
    }
  }

  override def childAdded(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def childRemoved(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def childReplaced(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def childMoved(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def childrenChanged(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def propertyChanged(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }
}
