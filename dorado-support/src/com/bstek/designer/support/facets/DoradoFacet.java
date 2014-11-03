package com.bstek.designer.support.facets;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;

/**
 * 开始Dorado idea插件开发之旅
 * User: robin
 * Date: 13-9-23
 * Time: 下午3:11
 * To change this template use File | Settings | File Templates.
 */
public class DoradoFacet extends Facet{

    public static final FacetTypeId<DoradoFacet> ID = new FacetTypeId<DoradoFacet>("dorado7");

    public DoradoFacet(@NotNull FacetType facetType, @NotNull Module module, @NotNull String name, @NotNull FacetConfiguration configuration, Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
    }

    public WebFacet getWebFacet(){
        return (WebFacet)getUnderlyingFacet();
    }
}
