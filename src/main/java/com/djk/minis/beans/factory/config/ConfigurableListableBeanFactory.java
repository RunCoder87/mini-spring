package com.djk.minis.beans.factory.config;

import com.djk.minis.beans.factory.xml.ListableBeanFactory;

/**
 * 同时集成ListableBeanFactory和ConfigurableBeanFactory
 */
public interface ConfigurableListableBeanFactory extends AutowireCapableBeanFactory, ListableBeanFactory, ConfigurableBeanFactory {


}
