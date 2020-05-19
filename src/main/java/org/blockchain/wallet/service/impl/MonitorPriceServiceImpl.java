package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.constant.Constant;
import org.blockchain.wallet.entity.MonitorPrice;
import org.blockchain.wallet.mapper.MonitorPriceMapper;
import org.blockchain.wallet.service.MonitorPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MonitorPriceServiceImpl implements MonitorPriceService {

    @Autowired
    MonitorPriceMapper monitorPriceMapper;

    @Override
    public List<MonitorPrice> selectBySelective(MonitorPrice monitorPrice) {
        return monitorPriceMapper.selectBySelective(monitorPrice);
    }

    @Override
    public int insert(MonitorPrice monitorPrice) {
        monitorPrice.setNotification(Constant.NOTIFICATION_ON);
        monitorPrice.setCreateTime(new Date());
        return monitorPriceMapper.insert(monitorPrice);
    }

    @Override
    public int updateBySelective(MonitorPrice monitorPrice) {
        return monitorPriceMapper.updateByPrimaryKeySelective(monitorPrice);
    }

    @Override
    public int deleteByPrimaryKey(int id) {
        return monitorPriceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByOneMonth() {
        return monitorPriceMapper.deleteByOneMonth();
    }
}
