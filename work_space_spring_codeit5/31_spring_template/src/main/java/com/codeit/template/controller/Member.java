package com.codeit.template.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member{
	private String id;
	private String name;
	private int age;
	private String gender;
	private String address; 
	private List<String> devLang;
	private String nullValue;
}
