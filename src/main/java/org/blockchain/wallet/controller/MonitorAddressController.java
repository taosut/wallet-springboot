package org.blockchain.wallet.controller;

import org.blockchain.wallet.async.AsyncExcutor;
import org.blockchain.wallet.entity.MonitorAddress;
import org.blockchain.wallet.entity.blockchair.BlockchairBTCAddrObj;
import org.blockchain.wallet.resttemplate.BlockChainRestAPI;
import org.blockchain.wallet.resttemplate.BlockChairRestAPI;
import org.blockchain.wallet.service.MonitorAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/monitorAddress")
public class MonitorAddressController {

    @Autowired
    MonitorAddressService monitorAddressService;

    @Autowired
    BlockChainRestAPI blockChainRestAPI;

    @Autowired
    BlockChairRestAPI blockChairRestAPI;

    @Autowired
    AsyncExcutor asyncExcutor;

    @GetMapping
    public List<BlockchairBTCAddrObj> findSingleAddress(@RequestParam String symbol) {

        MonitorAddress findMonitorAddress = new MonitorAddress();
        findMonitorAddress.setSymbol(symbol);
        List<MonitorAddress> monitorAddressList =  monitorAddressService.selectBySelective(findMonitorAddress);
        List<BlockchairBTCAddrObj> blockchairBTCAddrObjList = new ArrayList<>();

        for(MonitorAddress monitorAddress : monitorAddressList) {
            blockchairBTCAddrObjList.add(blockChairRestAPI.getBTCAddress(monitorAddress.getAddress()));
        }

        return blockchairBTCAddrObjList;
    }

    @GetMapping(value = "/address")
    public MonitorAddress findAddressById(@RequestParam int id) {

        return monitorAddressService.selectByPrimaryKey(id);
    }

    @GetMapping(value = "/test")
    public void Test() {
        asyncExcutor.monitorBTCAddress();
    }
}
