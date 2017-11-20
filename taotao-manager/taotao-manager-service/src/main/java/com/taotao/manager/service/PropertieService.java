package com.taotao.manager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/*
用于获取父容器中的配置文件信息，间接给controller层
* */
@Service
public class PropertieService {
    @Value("${REPOSITORY_PATH}")
    public String REPOSITORY_PATH;
    @Value("${IMAGE_BASE_URL}")
    public String IMAGE_BASE_URL;
}
