package com.codeit.bean.setting.service;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev") // dev 프로필에서만 활성화
public class DevMyService implements MyService {
    @Override
    public String getString() {
        return "Dev 환경용 서비스 실행";
    }
}
