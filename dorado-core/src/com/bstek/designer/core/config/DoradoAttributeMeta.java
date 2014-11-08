package com.bstek.designer.core.config;

import com.bstek.dorado.idesupport.model.Property;
import com.bstek.dorado.idesupport.model.Reference;

/**
 * @author Robin
 */
public class DoradoAttributeMeta {
    private Property prop;

    // --构造属性附加信息
    public DoradoAttributeMeta(Property prop) {
        this.prop = prop;
    }

    public Object getDefaultValue() {
        if (prop == null)
            return "";
        Object defaultValue = prop.getDefaultValue();
        if (getType() != null && getType().equals("boolean")) {
            return defaultValue == null ? "" : defaultValue;
        }
        return prop == null ? "" : defaultValue;
    }

    // --type 属性的Class类型.如果为null则表示此属性的类型是java.lang.String
    public String getType() {
        return prop.getType();
    }

    // --name 属性名
    public String getName() {
        return prop.getName();
    }

    // --fixed 是否固定属性,如果是则用户不能再IDE中修改该属性的值,也不能删除该属性
    public boolean getFixed() {
        return prop.isFixed();
    }

    // --enumValues 枚举值数组.表示该属性的取值应当来自于此枚举值数组中
    public String[] getEnumValues() {
        return (String[]) prop.getEnumValues();
    }

    public boolean isDeprecated() {
        return prop.isDeprecated();
    }

    public Reference getReference() {
        return prop.getReference();
    }

    public Property getProp() {
        return prop;
    }

    public int getHighlight() {
        return prop.getHighlight();
    }

    public boolean isVisible() {
        return prop.isVisible();
    }

    public String getEditor() {
        return prop.getEditor();
    }
}
