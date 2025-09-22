package com.codeit.bean.controller;


import com.codeit.bean.setting.AppProperties;
import com.codeit.bean.setting.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SettingController {

    // YAML을 통한 값 주입
    @Value("Hello Java World!") // 문자열 주입하는 방법
    private String hello;

    @Value("${app.name}")
    private String appName;

    @Value("${spring.profiles.active}")
    private String activeMode;

    // SpEL : 간단 수식, 시스템 속성, 다른 Bean을 참고할때
//    @Value("#{true ? 10 : 50}")
    @Value("#{5 * 10}")
    private int number;

    @Value("#{systemProperties['os.name']}")
    private String osName;

    // os별 경로 설정하기
    @Value("#{systemProperties['os.name'].toLowerCase().contains('win') ? 'c:\\dev' : '/user/dev'}")
    private String path;
    
    // 다른 Bean값 참조하기
    @Value("#{someBean.someProperty}")
    private String someProperty;


    // default 값 설정하기
    // 기본값 지정
    @Value("${app.description:Default App Description}")
    private String description;

    @Value("${server.port:8080}")
    private int port;

    @Value("${feature.enabled:false}")
    private boolean featureEnabled;

    // @ConfigurationProperties를 통한 yml 설정 주입 된 객체
    private final AppProperties appProperties;
    // 특정 설정에 따른 삼항연산자 적용 가능!
    @Value("#{appProperties.name == 'MySpringApp' ? 'MySpringApp 입니다.' : 'MySpringApp 아닙니다'}")
    private String value;


    @GetMapping("/settings")
    public String getSettings() {
        return String.format("appName : %s<br>activeMode : %s<br>number : %d<br>osName : %s<br>path : %s<br>description : %s<br>port : %d<br>featureEnabled : %s<br> value : %s"
                ,  appName, activeMode, number, osName, path, description, port, featureEnabled, value);
    }

    @GetMapping("/app-properties")
    public AppProperties getAppProperties() {
        return appProperties;
    }
    
    // @Profile에 따른 serverUrl 변경점 확인하는 방법
    private final String serverUrl;

    @GetMapping("/profile-server-url")
    public String getProfileServerUrl() {
        return serverUrl;
    }
    
    
    // Profile + Conditional 어노테이션 활용
    MyService myService;

    @Autowired
    public void setMyService(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/condition")
    public String condition() {
        if(myService == null){
            return "null 입니다";
        }
        return myService.getString();
    }

}
