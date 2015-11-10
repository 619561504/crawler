/**
 * @(#)Action.java
 * 2013-11-20-上午11:08:06
 */
package com.qq.railgun.action;

import com.qq.railgun.action.bean.CheckResult;
import com.qq.railgun.context.Context;
import com.qq.railgun.flow.configs.ActionConfigs;

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
