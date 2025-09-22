package com.codeit.bean.setting.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod") // prod 환경에서 Test용 서비스 활성화 하는 법
// yml에서 myapp.feature.enabled: false 값에 따라서 서비스 선택해보기
@ConditionalOnProperty(name="myapp.feature.enabled", havingValue = "false", matchIfMissing = true)
// name : 속성값 이름, havingValue : 일치할 값, matchIfMissing : 프로퍼티가 일치하지 않으면 생성 안함
public class ProdTestMyService implements MyService {
    @Override
    public String getString() {
        return "Prod 환경 + 필드 테스트 서비스 실행";
    }
}
