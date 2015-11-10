/**
 * @(#)RailGunUtils.java
 * 2013-11-21-下午2:55:13
 */
package com.qq.railgun.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.qq.railgun.bean.Node;

/**
 * @author hanjiewu
 * 
 */
public class RailGunUtils
{

    /**
     * 将Node的结果映射到对象中
     * @param clz
     * @param node
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T shellToObject(Class<T> clz, Node node) throws InstantiationException, IllegalAccessException,
            InvocationTargetException
    {
        T obj = clz.newInstance();
        BeanUtils.populate(obj, node.getFirstValues());
        return obj;
    }
}
