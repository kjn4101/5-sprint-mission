package com.codeit.bean.config;


import com.common.bean.CommonBean;
import com.common.bean.SetBean;
import com.common.bean.TestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.util.Optional;

// @ComponentScan : 컴포넌트를 스캔하여 Bean을 생성할 범위를 정하는 어노테이션, 일반적으로는 안써도 된다.
//                  config 할 때 간혹 사용할일 있음. 알고 있자.

//@ComponentScan(basePackages = "com.common") // 범위 지정 방법
//@ComponentScan(basePackageClasses = {CommonBean.class, SetBean.class}) // 클래스로만 지정하는 방법
@ComponentScan(
        basePackages = "com.common",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                            classes = CommonBean.class)
) // 일부만 제거하는 필터
@Configuration
public class ScanConfig {

    @Bean
    // Optional<CommonBean> : bean이 주입이 되지 않아도 사용할수 있도록 Optional 랩핑 가능!
    public Configurer commonConfigurer(Optional<CommonBean> commonBean) {
        Configurer configurer = new Configurer();
        CommonBean commonBean1 = commonBean.orElse(new CommonBean("비었습니다."));
        configurer.setOption("CommonBean : " + commonBean1);
        System.out.println(configurer);
        return configurer;
    }

    @Bean
    public Configurer testConfigurer(TestBean testBean) {
        Configurer configurer = new Configurer();
        configurer.setOption("test name : " + testBean.getName());
        System.out.println(configurer);
        return configurer;
    }
}
