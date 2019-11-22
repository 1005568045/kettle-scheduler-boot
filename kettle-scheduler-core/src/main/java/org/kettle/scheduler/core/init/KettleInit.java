package org.kettle.scheduler.core.init;

import org.pentaho.di.core.KettleEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * kettle初始化
 *
 * @author lyf
 */
@Component
public class KettleInit implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        // environmentInit();
        KettleEnvironment.init();
    }

    // public static void environmentInit() throws KettleException {
    //     if (Thread.currentThread().getContextClassLoader() == null) {
    //         Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
    //     }
    //
    //     Map<?, ?> prop = Constant.readProperties();
    //     Variables variables = new Variables();
    //     for (Object key : prop.keySet()) {
    //         String variable = (String) key;
    //         String value = variables.environmentSubstitute((String) prop.get(key));
    //         variables.setVariable(variable, value);
    //     }
    //     for (String variable : variables.listVariables()) {
    //         System.setProperty(variable, variables.getVariable(variable));
    //     }
    //
    //     System.getProperties().put("KETTLE_HOME", Constant.KETTLE_HOME);
    //     System.getProperties().put("KETTLE_PLUGIN_BASE_FOLDERS", Constant.KETTLE_PLUGIN);
    //     System.getProperties().put("KETTLE_JS_HOME", Constant.KETTLE_SCRIPT);
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_CLUSTER_SIZE, "1");
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_SLAVE_SERVER_NUMBER, "0");
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_SLAVE_SERVER_NAME, "slave-trans-name");
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_STEP_COPYNR, "0");
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_STEP_NAME, "step-name");
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_STEP_PARTITION_ID, "partition-id");
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_STEP_PARTITION_NR, "0");
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_STEP_UNIQUE_COUNT, "1");
    //     System.getProperties().put(Const.INTERNAL_VARIABLE_STEP_UNIQUE_NUMBER, "0");
    // }
}
