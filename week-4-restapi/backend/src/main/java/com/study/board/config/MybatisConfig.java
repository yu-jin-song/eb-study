package com.study.board.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * MyBatis 설정을 위한 구성 클래스
 */
@Configuration
public class MybatisConfig {
    /**
     * SqlSessionFactory를 생성하는 빈을 정의하는 메서드
     * 데이터 소스와 MyBatis 설정 파일을 기반으로 SqlSessionFactory 객체를 생성
     *
     * @param dataSource 데이터베이스 연결을 위한 데이터 소스 객체
     * @return SqlSessionFactory 객체
     * @throws Exception SqlSessionFactory 생성 과정에서 발생할 수 있는 예외
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    /**
     * SqlSessionTemplate를 생성하는 빈을 정의하는 메서드
     * MyBatis의 SqlSession을 구현, 여러 개의 DAO나 매퍼에서 공유할 수 있음
     *
     * @param sqlSessionFactory SqlSession을 생성하기 위한 SqlSessionFactory 객체
     * @return SqlSessionTemplate 객체
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
