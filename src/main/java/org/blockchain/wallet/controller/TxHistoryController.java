package org.blockchain.wallet.controller;

import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.entity.TxHistory;
import org.blockchain.wallet.service.TxHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/txHistory")
public class TxHistoryController {

    @Autowired
    TxHistoryService txHistoryService;

    @GetMapping
    public ResultResponse<List<TxHistory>> getAllTxHistory(@RequestParam String symbol) {

        TxHistory txHistory = new TxHistory();
        txHistory.setSymbol(symbol);

        return new ResultResponse<>(txHistoryService.selectBySelective(txHistory));
    }
}
