package com.bstek.designer.core.palette

import java.awt.{Component, Dimension, Container, LayoutManager}

/**
 * Created by robin on 14-7-22.
 */
class DoradoPaletteContainerLayout extends LayoutManager {

  def layoutContainer(parent: Container) {
    val width: Int = parent.getWidth
    var height: Int = 0
    for (component <- parent.getComponents) {
      if (component.isInstanceOf[DoradoPaletteGroupComponent]) {
        val groupComponent: DoradoPaletteGroupComponent = component.asInstanceOf[DoradoPaletteGroupComponent]
        groupComponent.setLocation(0, height)
        if (groupComponent.isVisible) {
          val groupHeight: Int = groupComponent.getPreferredSize.height
          groupComponent.setSize(width, groupHeight)
          height += groupHeight
        }
        else {
          groupComponent.setSize(0, 0)
        }
        if (groupComponent.isSelected || !groupComponent.isVisible) {
          val itemsComponent: DoradoPaletteItemsComponent = groupComponent.getItemsComponent
          val itemsHeight: Int = itemsComponent.getPreferredSize.height
          itemsComponent.setBounds(0, height, width, itemsHeight)
          height += itemsHeight
        }
      }
    }
  }

  def preferredLayoutSize(parent: Container): Dimension = {
    val width: Int = parent.getWidth
    var height: Int = 0
    for (component <- parent.getComponents) {
      if (component.isInstanceOf[DoradoPaletteGroupComponent]) {
        val groupComponent: DoradoPaletteGroupComponent = component.asInstanceOf[DoradoPaletteGroupComponent]
        height += groupComponent.getHeight
        if (groupComponent.isSelected) {
          height += groupComponent.getItemsComponent.getPreferredHeight(width)
        }
      }
    }
    return new Dimension(10, height)
  }

  def minimumLayoutSize(parent: Container): Dimension = {
    return new Dimension
  }

  def addLayoutComponent(name: String, comp: Component) {
  }

  def removeLayoutComponent(comp: Component) {
  }

}
