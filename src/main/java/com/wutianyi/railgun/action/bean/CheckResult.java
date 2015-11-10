/**
 * @(#)CheckResult.java
 * 2013-11-20-下午3:15:37
 */
package com.wutianyi.railgun.action.bean;

/**
 * 检查结果
 * 
 * @author hanjiewu
 * 
 */
public class CheckResult
{

    public static CheckResult SUCCESS = new CheckResult(true, "正常！");

    private boolean result;

    private String info;

    public CheckResult()
    {

    }

    public CheckResult(boolean result, String info)
    {
        this.result = result;
        this.info = info;
    }

    public boolean isResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

}
