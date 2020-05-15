package org.blockchain.wallet.service;

import org.blockchain.wallet.entity.BlockchainBrowser;

import java.util.List;

public interface BlockchainBrowserService {

    List<BlockchainBrowser> selectBySelective(BlockchainBrowser blockchainBrowser);
}
