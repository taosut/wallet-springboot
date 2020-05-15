package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.HotWeb;
import org.blockchain.wallet.mapper.HotWebMapper;
import org.blockchain.wallet.service.HotWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotWebServiceImpl implements HotWebService {

    @Autowired
    HotWebMapper hotWebMapper;

    @Override
    public List<HotWeb> selectBySelective(HotWeb hotWeb) {
        return hotWebMapper.selectBySelective(hotWeb);
    }
}
