package com.bstek.designer.core.property;


import com.intellij.designer.model.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by robin on 14-5-17.
 */
public abstract class DoradoProperty extends Property<com.bstek.designer.core.model.DoradoRadViewComponent> {


    public DoradoProperty(@Nullable Property parent, @NotNull String name) {
        super(parent, name);
    }

}