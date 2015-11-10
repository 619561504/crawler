/**
 * @(#)ActionConfigs.java
 * 2013-11-20-上午11:09:32
 */
package com.qq.railgun.flow.configs;

import java.util.Map;

/**
 * @author hanjiewu
 * 
 */
public class ActionConfigs
{
    /**
     * 实际动作
     */
    private String action;

    /**
     * 名称
     */
    private String name;

    /**
     * 代理
     */
    private String proxy;

    /**
     * 代理端口
     */
    private int proxyPort;
    /**
     * 请求的url
     */
    private String url;

    /**
     * 请求头部信息
     */
    private Map<String, String> headers;

    /**
     * 提取规则
     */
    private String rule;

    /**
     * 提取属性值
     */
    private String attr;

    /**
     * 对提取之后的数据进行正则的匹配
     */
    private String regex;

    /**
     * 对最后提取的数据进行的动作
     */
    private String setField;

    /**
     * 是否去空格
     */
    private boolean strip;
    
    /**
     * 是否只处理text内容
     */
    private boolean text;

    /**
     * 调用jsoup select 返回的elements中的方法
     */
    private String jsoupSelectAction;

    /**
     * 子动作
     */
    private ActionConfigs[] subActionConfigs;

    /**
     * 父节点
     */
    private ActionConfigs parentActionConfigs;

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getProxy()
    {
        return proxy;
    }

    public void setProxy(String proxy)
    {
        this.proxy = proxy;
    }

    public int getProxyPort()
    {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort)
    {
        this.proxyPort = proxyPort;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    public String getRule()
    {
        return rule;
    }

    public void setRule(String rule)
    {
        this.rule = rule;
    }

    public String getRegex()
    {
        return regex;
    }

    public void setRegex(String regex)
    {
        this.regex = regex;
    }

    public String getSetField()
    {
        return setField;
    }

    public void setSetField(String setField)
    {
        this.setField = setField;
    }

    public boolean isStrip()
    {
        return strip;
    }

    public void setStrip(boolean strip)
    {
        this.strip = strip;
    }

    public String getJsoupSelectAction()
    {
        return jsoupSelectAction;
    }

    public void setJsoupSelectAction(String jsoupSelectAction)
    {
        this.jsoupSelectAction = jsoupSelectAction;
    }

    public ActionConfigs[] getSubActionConfigs()
    {
        return subActionConfigs;
    }

    public void setSubActionConfigs(ActionConfigs[] subActionConfigs)
    {
        this.subActionConfigs = subActionConfigs;
    }

    public ActionConfigs getParentActionConfigs()
    {
        return parentActionConfigs;
    }

    public void setParentActionConfigs(ActionConfigs parentActionConfigs)
    {
        this.parentActionConfigs = parentActionConfigs;
    }

    public String getAttr()
    {
        return attr;
    }

    public void setAttr(String attr)
    {
        this.attr = attr;
    }

    public boolean isText()
    {
        return text;
    }

    public void setText(boolean text)
    {
        this.text = text;
    }

}
