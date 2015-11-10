/**
 * @(#)JsonConfigParser.java
 * 2013-11-20-下午3:02:31
 */
package com.wutianyi.railgun.flow.configs;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * 解析json格式的配置
 * 
 * @author hanjiewu
 * 
 */
public class JsonConfigParser
{
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param path文件路徑
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static ActionConfigs parser(String path) throws JsonParseException, JsonMappingException, IOException
    {
        return parser(new File(path));
    }

    /**
     * @param file
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static ActionConfigs parser(File file) throws JsonParseException, JsonMappingException, IOException
    {
        return objectMapper.readValue(file, ActionConfigs.class);
    }

    public static ActionConfigs parserContents(String railgunConfigs) throws JsonParseException, JsonMappingException, IOException
    {
        return objectMapper.readValue(railgunConfigs, ActionConfigs.class);
    }
}
