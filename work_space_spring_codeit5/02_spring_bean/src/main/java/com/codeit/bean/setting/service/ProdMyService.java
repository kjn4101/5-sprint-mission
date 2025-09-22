package com.codeit.bean.setting.service;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod") // prod 프로필에서만 활성화
// yml에서 myapp.feature.enabled: true 값에 따라서 서비스 선택해보기
@ConditionalOnProperty(name="myapp.feature.enabled", havingValue = "true", matchIfMissing = true)
// name : 속성값 이름, havingValue : 일치할 값, matchIfMissing : 프로퍼티가 일치하지 않으면 생성 안함
public class ProdMyService implements MyService {
    @Override
    public String getString() {
        return "Prod 환경용 서비스 실행";
    }
}
