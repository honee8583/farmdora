package com.farmdora.farmdora.auth.auth.register.service;

import java.io.InputStream;

public interface NCPStorageService {
    void upload(String filePath, InputStream fileIn, long size);



}
