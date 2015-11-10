/**
 * @(#)Node.java
 * 2013-11-20-下午4:47:41
 */
package com.qq.railgun.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 解析之后的结果
 * 
 * @author hanjiewu
 * 
 */
public class Node
{
    private Map<String, List<String>> datas = new HashMap<String, List<String>>();

    public void put(String key, List<String> value)
    {
        datas.put(key, value);
    }

    public List<String> get(String key)
    {
        return datas.get(key);
    }

    /**
     * 获取每个抓取的第一个值
     * 
     * @return
     */
    public Map<String, String> getFirstValues()
    {
        Map<String, String> data = new HashMap<String, String>();
        for (Entry<String, List<String>> entry : datas.entrySet())
        {
            if (entry.getValue().size() > 0)
            {
                data.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        return data;
    }

    public Map<String, List<String>> getDatas()
    {
        return datas;
    }
}
