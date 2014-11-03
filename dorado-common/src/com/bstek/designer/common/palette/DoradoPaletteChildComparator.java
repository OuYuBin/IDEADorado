package com.bstek.designer.common.palette;

import java.util.Comparator;

import com.bstek.designer.core.config.Dorado7RulesConfigImpl;
import com.bstek.designer.core.config.DoradoConfigRulesModelMeta;
import org.eclipse.emf.ecore.EClass;


/**
 * 
 * @author robin
 * 
 */

public class DoradoPaletteChildComparator implements Comparator {

    Dorado7RulesConfigImpl baseConfig;

	public DoradoPaletteChildComparator(Dorado7RulesConfigImpl baseConfig) {
		this.baseConfig = baseConfig;
	}



    public int compare(Object object1, Object object2) {
        DoradoConfigRulesModelMeta meta1 = baseConfig
				.getMetaOfModel((EClass)object1);
		if (meta1 == null) {
			return 0;
		}
		int sort = 0;
		try {
			int sortFactor1 = meta1.getSortFactor();
			DoradoConfigRulesModelMeta meta2 = baseConfig
					.getMetaOfModel((EClass) object2);
			if (meta2== null) {
				return 0;
			}
			int sortFactor2 = meta2.getSortFactor();
			sort = sortFactor1 - sortFactor2;
			if (sort == 0) {
				String name1 =baseConfig.getMetaOfModel((EClass)object1).getRule().getName();
				String name2 =baseConfig.getMetaOfModel((EClass)object2).getRule().getName();
				sort = name1.compareTo(name2);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} 
		return sort;
	}
}
