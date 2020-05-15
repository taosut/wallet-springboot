package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.TxDest;
import org.blockchain.wallet.mapper.TxDestMapper;
import org.blockchain.wallet.service.TxDestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TxDestServiceImpl implements TxDestService {

    @Autowired
    TxDestMapper txDestMapper;

    @Override
    public List<TxDest> selectBySelective(TxDest txDest) {
        return txDestMapper.selectBySelective(txDest);
    }

    @Override
    public int insertTxDest(TxDest txDest) {

        txDest.setCreateTime(new Date());
        return txDestMapper.insert(txDest);
    }

    @Override
    public int deleteTxDestBySelective(TxDest txDest) {
        return txDestMapper.deleteBySelective(txDest);
    }
}
