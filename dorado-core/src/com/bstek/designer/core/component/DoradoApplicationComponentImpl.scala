package com.bstek.designer.core.component

import com.intellij.openapi.components.ApplicationComponent

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-10-17
 * Time: 上午1:02
 * To change this template use File | Settings | File Templates.
 */
class DoradoApplicationComponentImpl() extends ApplicationComponent with DoradoApplicationComponent{

  println("Application Component");

  def initComponent() = {
    println("Application Component");
  }

  def disposeComponent() = {
  }


  def getComponentName(): String = {
    "DoradoApplicationComponent"
  }

}
