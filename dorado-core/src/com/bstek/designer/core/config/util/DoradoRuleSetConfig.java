package com.bstek.designer.core.config.util;

import com.bstek.dorado.idesupport.StandaloneRuleSetBuilder;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author robin
 */
public class DoradoRuleSetConfig {

    private static String RULES_FILE_NAME = ".rules";


    public static RuleSet createRuleSet(Project project) {
        try {
            File rulesFile = FileUtil.findFirstThatExist(project.getBasePath() + "/.rules");
            if (rulesFile.exists()) {
                return getRuleSet(rulesFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RuleSet getRuleSet(File ruleFile) {
        RuleSet ruleSet = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(ruleFile);
            ruleSet = StandaloneRuleSetBuilder.getRuleSet(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ruleSet;
    }

}