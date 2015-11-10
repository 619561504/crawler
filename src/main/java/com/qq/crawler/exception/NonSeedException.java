/**
 * 下午2:50:54
 */
package com.qq.crawler.exception;

/**
 * @author hanjiewu
 * 
 */
public class NonSeedException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -182277619251071702L;

    public NonSeedException()
    {
        super("The crawler seed must not null or empty!");
    }
}
