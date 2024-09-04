package com.djk.minis.core.env;

public interface Environment extends PropertyResolver {
    String[] getActiveProfiles(); // 当前激活的profile

    String[] getDefaultProfiles(); // 默认的profile

    boolean acceptsProfiles(String... profiles); // 判断是否激活
}
