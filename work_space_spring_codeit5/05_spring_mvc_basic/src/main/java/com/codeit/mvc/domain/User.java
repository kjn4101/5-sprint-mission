package com.codeit.mvc.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// lombok 사용
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

// xml
@JacksonXmlRootElement(localName = "user")
public class User {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String roles;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "devLang")
    private List<String> devLang;
}
