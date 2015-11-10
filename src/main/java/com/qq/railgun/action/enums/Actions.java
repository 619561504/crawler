/**
 * @(#)Actions.java
 * 2013-11-20-上午11:37:14
 */
package com.qq.railgun.action.enums;

import java.util.HashMap;
import java.util.Map;

import com.qq.railgun.action.Action;
import com.qq.railgun.action.impl.FetcherAction;
import com.qq.railgun.action.impl.MainAction;
import com.qq.railgun.action.impl.ParserAction;
import com.qq.railgun.action.impl.ShellAction;

/**
 * 定义action 新添加的action需要在这里进行注册
 * 
 * @author hanjiewu
 * 
 */
public enum Actions
{
    FETCHER("fetcher", new FetcherAction()), MAIN("main", new MainAction()), PARSER("parser", new ParserAction()), SHELL(
            "shell", new ShellAction());

    private static Map<String, Action> maps = new HashMap<String, Action>();

    static
    {
        for (Actions a : Actions.values())
        {
            maps.put(a.getName(), a.getAction());
        }
    }

    private String name;

    private Action action;

    private Actions(String name, Action action)
    {
        this.name = name;
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

    public Action getAction()
    {
        return action;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public static Action getAction(String action)
    {
        return maps.get(action);
    }
}
