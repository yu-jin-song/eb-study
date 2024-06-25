package com.study.board.config;

import com.study.board.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 애플리케이션 설정 클래스
 */
@Configuration
@PropertySource("classpath:application.yml")    // 프로퍼티 파일 경로 지정
public class AppConfig {

    /**
     * 파일 저장 경로(최하위 디렉토리 제외)
     */
    @Value("${file.upload.base-path}")
    private String basePath;

    /**
     * 파일 저장 디렉토리명
     */
    @Value("${file.upload.directory}")
    private String directoryName;

    /**
     * FileUtil 클래스 내 필드값을 초기화하여 반환
     *
     * @return FileUtil 클래스
     */
    @Bean
    public FileUtil fileUtil() {
        return FileUtil.builder()
                .basePath(basePath)
                .directoryName(directoryName)
                .build();
    }

}
