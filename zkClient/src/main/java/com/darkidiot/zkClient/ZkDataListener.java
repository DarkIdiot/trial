package com.darkidiot.zkClient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;

@Slf4j
class ZkDataListener implements IZkDataListener {

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        log.info("subscribe ok...");
        log.info("dataPath{},data:{}", dataPath, data);
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        log.info("subscribe ok...");
        log.info("dataPath{}", dataPath);
    }
}