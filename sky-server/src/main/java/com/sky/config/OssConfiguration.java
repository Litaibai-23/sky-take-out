package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: OssConfiguration
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/2/25 23:47
 */
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean// 创建一个bean
    @ConditionalOnMissingBean// 如果容器中没有这个bean，才创建这个bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传客户端：{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                       aliOssProperties.getAccessKeyId(),
                       aliOssProperties.getAccessKeySecret(),
                       aliOssProperties.getBucketName());
    }
}
