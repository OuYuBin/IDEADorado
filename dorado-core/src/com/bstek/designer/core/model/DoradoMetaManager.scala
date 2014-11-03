package com.bstek.designer.core.model

import com.bstek.dorado.idesupport.model.Rule
import com.intellij.designer.model.RadComponent
import com.bstek.designer.core.config.DoradoConfigRulesModelMeta

/**
 * Created by robin on 14-3-27.
 */
class DoradoMetaManager {
}

object DoradoMetaManager {
  def createModel(component: Class[RadComponent], modelMeta: DoradoConfigRulesModelMeta): DoradoMetaModel = {
    val metaModel = new DoradoMetaModel(component, modelMeta)
    metaModel.setPresentation(modelMeta.getRule.getNodeName, modelMeta.getRule.getIcon)
    return metaModel
  }
}