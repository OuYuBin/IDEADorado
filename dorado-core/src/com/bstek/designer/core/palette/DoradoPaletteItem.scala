package com.bstek.designer.core.palette

import java.io.File
import javax.swing.Icon

import com.intellij.designer.model.MetaModel
import com.intellij.openapi.util.IconLoader
import icons.DoradoIcons

import scala.beans.BeanProperty


/**
 * Created by robin on 14-7-22.
 */
class DoradoPaletteItem(@BeanProperty val title: String, @BeanProperty iconPath: String, @BeanProperty val tooltip: String, @BeanProperty version: String, @BeanProperty val deprecatedVersion: String, @BeanProperty val deprecatedHint: String) {
  //  private final val myTitle: String = null
  //  protected final val myIconPath: String = null
  protected var icon: Icon = _
  //  private final val myTooltip: String = null
  //  private final val myVersion: String = null
  //  private var myEnabled: Boolean = true
  //  private final val myDeprecatedVersion: String = null
  //  private final val myDeprecatedHint: String = null
  //  protected var myMetaModel: MetaModel = null

  //  def this(palette: Element) {
  //    this()
  //    `this`(palette.getAttributeValue("title"), palette.getAttributeValue("icon"), palette.getAttributeValue("tooltip"), palette.getAttributeValue("version"), palette.getAttributeValue("deprecated"), palette.getAttributeValue("deprecatedHint"))
  //  }

  //  def this(title: String, iconPath: String, tooltip: String, version: String, deprecatedVersion: String, deprecatedHint: String) {
  //    this()
  //    myTitle = title
  //    myIconPath = iconPath
  //    myTooltip = tooltip
  //    myVersion = version
  //    myDeprecatedVersion = deprecatedVersion
  //    myDeprecatedHint = deprecatedHint
  //  }

  //  def getTitle: String = {
  //    return myTitle
  //  }

  def getIcon: Icon = {
    if (icon == null) {
      val file: File = new File(iconPath)
      if (file.exists) {
        icon = DoradoIcons.load(file)
      }
    }
    return icon
  }

  //  def getTooltip: String = {
  //    return myTooltip
  //  }

  //  def getVersion: String = {
  //    return myVersion
  //  }
  //
  //  def isEnabled: Boolean = {
  //    return myEnabled
  //  }
  //
  //  def setEnabled(enabled: Boolean) {
  //    myEnabled = enabled
  //  }
  //
  //  @Nullable def getDeprecatedIn: String = {
  //    return myDeprecatedVersion
  //  }
  //
  //  @Nullable def getDeprecatedHint: String = {
  //    return myDeprecatedHint
  //  }
  //
  //  def getCreation: String = {
  //    return myMetaModel.getCreation
  //  }
  //
  //  def getMetaModel: MetaModel = {
  //    return myMetaModel
  //  }
  //
  //  def setMetaModel(metaModel: MetaModel) {
  //    myMetaModel = metaModel
  //  }
}
