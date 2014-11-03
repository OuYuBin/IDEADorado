package com.bstek.designer.core.config;

import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.Rule;

/**
 * @author Robin
 */
public class DoradoReferenceMeta {
    Object parentChild;
    Child subChild;
    String name;

    public DoradoReferenceMeta(Object parentChild, Child subChild, String name) {
        this.parentChild = parentChild;
        this.subChild = subChild;
        this.name = name;
    }

    public Object getParentChild() {
        return parentChild;
    }

    public String getName() {
        return name;
    }

    public boolean isFixed() {
        return subChild.isFixed();
    }

    public String getNodeName() {
        Rule rule = subChild.getRule();
        return rule.getNodeName();
    }

    public boolean isAggregated() {
        return subChild.isAggregated();
    }

    public Rule getRule() {
        return subChild.getRule();
    }
}