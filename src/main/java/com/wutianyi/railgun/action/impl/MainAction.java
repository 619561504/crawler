/**
 * @(#)MainAction.java
 * 2013-11-20-上午11:38:42
 */
package com.wutianyi.railgun.action.impl;

import com.wutianyi.railgun.action.Action;
import com.wutianyi.railgun.action.bean.CheckResult;
import com.wutianyi.railgun.context.Context;
import com.wutianyi.railgun.flow.configs.ActionConfigs;

/**
 * @author hanjiewu
 * 
 */
public class MainAction implements Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qq.railgun.action.Action#process(com.qq.railgun.flow.configs.
	 * ActionConfigs)
	 */
	@Override
	public void process(ActionConfigs config, Context context) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.qq.railgun.action.Action#check(com.qq.railgun.flow.configs.ActionConfigs
	 * )
	 */
	@Override
	public CheckResult check(ActionConfigs config, Context context) {

		return CheckResult.SUCCESS;
	}

}
