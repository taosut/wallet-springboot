package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.BlockchainBrowser;
import org.blockchain.wallet.mapper.BlockchainBrowserMapper;
import org.blockchain.wallet.service.BlockchainBrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockchainBrowserServiceImpl implements BlockchainBrowserService {

    @Autowired
    BlockchainBrowserMapper blockchainBrowserMapper;

    @Override
    public List<BlockchainBrowser> selectBySelective(BlockchainBrowser blockchainBrowser) {
        return blockchainBrowserMapper.selectBySelective(blockchainBrowser);
    }
}
