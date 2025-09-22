package com.codeit.bean.controller;

import com.codeit.bean.config.Configurer;
import com.common.bean.SetBean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ConfigController {

    {
        System.out.println("ConfigController 생성되었습니다.");
    }

    private final Configurer myConfigurer;
    private final Configurer dbConfigurer;
    private final Configurer apiConfigurer;
    private final Configurer combiConfigurer;

    @Autowired // 필드 주입, 권장안함
    @Qualifier("mainDB")
    private Configurer maindbConfigurer;
    private Configurer subdbConfigurer;
    private Configurer combiConfigurer2;

    // scan 관련 config
    private final Configurer commonConfigurer;
    private final Configurer testConfigurer;


    @Autowired
    @Qualifier("subDB")
    public void setSubdbConfigurer(Configurer subdbConfigurer) {
        this.subdbConfigurer = subdbConfigurer;
    }

    @Autowired
    @Qualifier("combi")
    public void setCombiConfigurer2(Configurer combiConfigurer2) {
        this.combiConfigurer2 = combiConfigurer2;
    }

    @GetMapping("/config/allBean")
    public Map<String, Object> allBean(){
        return Map.of("myConfigurer",myConfigurer,
                        "dbConfigurer",dbConfigurer,
                        "apiConfigurer",apiConfigurer,
                        "combiConfigurer",combiConfigurer,
                        "maindbConfigurer",maindbConfigurer,
                        "subdbConfigurer",subdbConfigurer,
                        "combiConfigurer2",combiConfigurer2
        );
    }

    // ScanConfig에서 수행한 @ComponentScan(scan + bean 생성)을 통해 setBean을 다른곳에서 사용할수 있다.
    @Autowired
    SetBean setBean;

    @GetMapping("/config/scanBean")
    public Map<String, Object> scanBean(){
        return Map.of("commonConfigurer",commonConfigurer,
                        "testConfigurer", testConfigurer,
                        "setBean", setBean
                        );
    }

}
