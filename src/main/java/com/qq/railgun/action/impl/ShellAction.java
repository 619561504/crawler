/**
 * @(#)ShellAction.java
 * 2013-11-20-上午11:52:52
 */
package com.qq.railgun.action.impl;

import com.qq.railgun.action.Action;
import com.qq.railgun.action.bean.CheckResult;
import com.qq.railgun.context.Context;
import com.qq.railgun.flow.configs.ActionConfigs;

/**
 * 将数据保存到对应的变量中 rungail获取数据实际就是获取 shell action下子action设置的数据
 * 
 * @author hanjiewu
 * 
 */
public class ShellAction implements Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qq.railgun.action.Action#process(com.qq.railgun.flow.configs.
	 * ActionConfigs, com.qq.railgun.context.Context)
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
		// TODO Auto-generated method stub
		return CheckResult.SUCCESS;
	}

}
