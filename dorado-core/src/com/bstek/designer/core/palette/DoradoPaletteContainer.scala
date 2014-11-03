package com.bstek.designer.core.palette

import java.awt._
import javax.swing.{JPanel, Scrollable}

/**
 * Created by robin on 14-7-22.
 */
class DoradoPaletteContainer extends JPanel(new DoradoPaletteContainerLayout()) with Scrollable {


  def getPreferredScrollableViewportSize: Dimension = {
    return getPreferredSize
  }


  def getScrollableUnitIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int = {
    return 20
  }


  def getScrollableBlockIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int = {
    return 100
  }


  def getScrollableTracksViewportWidth: Boolean = {
    return true
  }


  def getScrollableTracksViewportHeight: Boolean = {
    return false
  }


}
