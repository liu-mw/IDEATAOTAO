package com.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liu_mw
 * @date 2017/11/25 15:41
 */
public class Item extends com.taotao.manager.pojo.Item {

    public String[] getImages(){
        //该方法可以判断super.getImage()包含了null
        return StringUtils.split(super.getImage(),",");
    }
}
