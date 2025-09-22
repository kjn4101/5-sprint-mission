package com.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component("common")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonBean {
    private String name = "common bean 입니다";
    {
        System.out.println("common 생성되었습니다.");
    }
}
