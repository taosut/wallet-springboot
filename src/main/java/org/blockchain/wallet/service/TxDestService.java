package org.blockchain.wallet.service;

import org.blockchain.wallet.entity.TxDest;

import java.util.List;

public interface TxDestService {

    List<TxDest> selectBySelective(TxDest txDest);

    int insertTxDest(TxDest txDest);

    int deleteTxDestBySelective(TxDest txDest);
}
