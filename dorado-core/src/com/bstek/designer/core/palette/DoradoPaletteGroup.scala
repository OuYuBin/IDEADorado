package com.bstek.designer.core.palette

import scala.beans.BeanProperty


/**
 * Created by robin on 14-7-22.
 */
class DoradoPaletteGroup(@BeanProperty val name:String) {

  protected final val paletteItems: java.util.List[DoradoPaletteItem] = new java.util.ArrayList[DoradoPaletteItem]


  def addItem(item: DoradoPaletteItem) {
    paletteItems.add(item)
  }

  def getItems: java.util.List[DoradoPaletteItem] = {
    return paletteItems
  }

}
