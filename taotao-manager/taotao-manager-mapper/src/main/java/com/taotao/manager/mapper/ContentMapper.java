package com.taotao.manager.mapper;

import com.github.abel533.mapper.Mapper;
import com.taotao.manager.pojo.Content;

import java.util.List;

/**
 * @author liu_mw
 * @date 2017/11/22 19:55
 */
public interface ContentMapper extends Mapper<Content> {
    List<Content> queryListBycategoryId(Long categoryId);
}
