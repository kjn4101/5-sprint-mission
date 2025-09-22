package com.codeit.blog.repository;

import com.codeit.blog.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileRepository extends JpaRepository<BinaryContent, Long> {

}
