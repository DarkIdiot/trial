package com.darkidiot.zkClient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;

@Slf4j
class ZkChildListener implements IZkChildListener {
    @Override
    public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
        log.info("subscribe ok...");
        log.info("parentPath{},currentChildren:{}", parentPath, currentChildren);
    }
}