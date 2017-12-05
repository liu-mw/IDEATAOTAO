package com.taotao.sso.query.api;

import com.taotao.sso.query.bean.User;

/**
 * @author liu_mw
 * @date 2017/12/5 20:06
 */
public interface UserQueryService {
    /**
     * 根据token查询User对象
     *
     * @return
     */
    public User queryUserByToken(String token);
}
