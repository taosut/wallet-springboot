package org.blockchain.wallet.controller;

import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.entity.TxDest;
import org.blockchain.wallet.service.TxDestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/txDest")
public class TxDestController {

    @Autowired
    TxDestService txDestService;

    @GetMapping
    public ResultResponse<List<TxDest>> getTxDest(@RequestParam String hash, @RequestParam String symbol) {

        TxDest txDest = new TxDest();
        txDest.setTxHash(hash);
        txDest.setSymbol(symbol);

        return new ResultResponse<>(txDestService.selectBySelective(txDest));
    }
}
