package com.codeit.rest.service;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
    boolean store(String path, InputStream in) throws IOException;
    void delete(String path) throws IOException;
}
