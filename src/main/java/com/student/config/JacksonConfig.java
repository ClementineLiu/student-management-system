package com.student.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Jackson 配置 - 支持多种日期格式反序列化
 * 解决前端 el-date-picker 发送 yyyy-MM-dd 但 Jackon 默认只接受 yyyy-MM-dd HH:mm:ss 的问题
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        SimpleModule module = new SimpleModule("MultiFormatDateModule");
        module.addDeserializer(Date.class, new MultiFormatDateDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }

    /**
     * 多格式日期反序列化器
     * 依次尝试多种格式解析日期字符串
     */
    static class MultiFormatDateDeserializer extends JsonDeserializer<Date> {

        private static final String[] DATE_FORMATS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss"
        };

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateStr = p.getText().trim();
            if (dateStr.isEmpty()) {
                return null;
            }

            for (String format : DATE_FORMATS) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    sdf.setLenient(false);
                    return sdf.parse(dateStr);
                } catch (Exception ignored) {
                }
            }

            throw new RuntimeException("无法解析日期字符串: " + dateStr
                    + "，支持的格式: yyyy-MM-dd HH:mm:ss / yyyy-MM-dd");
        }
    }
}
