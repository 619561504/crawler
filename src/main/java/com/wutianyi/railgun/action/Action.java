/**
 * @(#)Action.java
 * 2013-11-20-上午11:08:06
 */
package com.wutianyi.railgun.action;

import com.wutianyi.railgun.action.bean.CheckResult;
import com.wutianyi.railgun.context.Context;
import com.wutianyi.railgun.flow.configs.ActionConfigs;

/**
 * @author hanjiewu
 * 
 */
public interface Action
{

    /**
     * 实际处理
     * @param context TODO
     */
    public void process(ActionConfigs config, Context context);

    /**
     * 验证配置是否正确
     * 
     * @param config
     * @param context TODO
     * @return
     */
    public CheckResult check(ActionConfigs config, Context context);
}
