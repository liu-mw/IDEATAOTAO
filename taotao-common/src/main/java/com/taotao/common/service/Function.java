package com.taotao.common.service;

/**
 * @author liu_mw
 * @date 2017/11/25 12:29
 */
public interface Function<T,E> {
    public T callback(E e);
}
