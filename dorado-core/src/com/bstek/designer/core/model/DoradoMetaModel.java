package com.bstek.designer.core.model;

import com.bstek.designer.core.config.DoradoConfigRulesModelMeta;
import com.intellij.designer.model.MetaModel;
import com.intellij.designer.model.RadComponent;

/**
 * Created by robin on 14-3-27.
 */
public class DoradoMetaModel extends MetaModel {

    private DoradoConfigRulesModelMeta modelMeta;

    public DoradoMetaModel(Class<RadComponent> model, DoradoConfigRulesModelMeta modelMeta) {
        this(model, modelMeta.getRule().getName(), modelMeta.getRule().getNodeName());
        this.modelMeta=modelMeta;
    }

    public DoradoMetaModel(Class<RadComponent> model, String target, String tag) {
        super(model, target, tag);
    }

    public DoradoConfigRulesModelMeta getModelMeta() {
        return modelMeta;
    }

}
