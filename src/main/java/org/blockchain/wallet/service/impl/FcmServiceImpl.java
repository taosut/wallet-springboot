package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.Fcm;
import org.blockchain.wallet.fcm.FcmClient;
import org.blockchain.wallet.mapper.FcmMapper;
import org.blockchain.wallet.service.FcmService;
import org.blockchain.wallet.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FcmServiceImpl implements FcmService {

    private final static Logger logger = LoggerFactory.getLogger(FcmServiceImpl.class);

    @Autowired
    FcmMapper fcmMapper;

    @Autowired
    FcmClient fcmClient;


    @Override
    public Fcm findFcmById(int id) {
        return fcmMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Fcm> findFcmBySelective(Fcm fcm) {
        return fcmMapper.selectBySelective(fcm);
    }

    @Override
    public int insertFcm(Fcm fcm) {
        return fcmMapper.insert(fcm);
    }

    @Override
    public int updateFcm(Fcm fcm) {
        return fcmMapper.updateByPrimaryKeySelective(fcm);
    }

    @Override
    public int register(Fcm fcm) {

        Fcm fcmDto = new Fcm();
        fcmDto.setUserId(fcm.getUserId());
        List<Fcm> fcmList = findFcmBySelective(fcmDto);

        if(fcmList.size() != 0) {
            fcmDto = fcmList.get(0);
            fcm.setId(fcmDto.getId());
            return updateFcm(fcm);
        } else {
            return insertFcm(fcm);
        }
    }

    @Override
    public void sendPersonalNotification(int userId, String title, String body) {
        Fcm fcm = new Fcm();
        fcm.setUserId(userId);
        Map<String, String> message = new HashMap<>();
        message.put("id", userId + String.valueOf(new Date().getTime()));
        message.put("title", title);
        message.put("body", body);
        try {
            fcmClient.sendPersonalMessage(fcmMapper.selectBySelective(fcm).get(0).getFcmToken(), message);
        } catch (Exception e) {
            logger.error("send message", e);
        }
    }

    @Override
    public List<String> selectAllTokens() {
        return fcmMapper.selectAllTokens();
    }

    @Override
    @Async
    public void sendAllNotification(String title, String body) {

        List<String> registerTokens = selectAllTokens();
        Map<String, String> message = new HashMap<>();
        message.put("id", String.valueOf(new Date().getTime()));
        message.put("title", title);
        message.put("body", body);

        try {
            fcmClient.sendAllMessage(registerTokens, message);
        } catch (Exception e) {
            logger.error("send message", e);
        }
    }


}
