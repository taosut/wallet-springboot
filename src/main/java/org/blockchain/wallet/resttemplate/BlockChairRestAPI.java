package org.blockchain.wallet.resttemplate;

import com.alibaba.fastjson.JSONObject;
import org.blockchain.wallet.entity.blockchair.BlockchairAddrAbstract;
import org.blockchain.wallet.entity.blockchair.BlockchairBTCAddrObj;
import org.blockchain.wallet.entity.blockchair.BlockchairBTCTxObj;
import org.blockchain.wallet.entity.blockchair.BlockchairTxAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class BlockChairRestAPI {

    @Value("${com.blockchair.monitor.http.rootUrl}")
    String rootUrl;

    @Autowired
    RestTemplate restTemplate;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public BlockchairAddrAbstract getBTCAddress(String address) {
        String url =rootUrl + "/bitcoin/dashboards/address/" + address + "?transaction_details={transaction_details}&limit={limit}";

        Map<String, String> map = new HashMap<>();
        map.put("transaction_details", "true");
        map.put("limit", "50,0");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();

            JSONObject resultMap = JSONObject.parseObject(result);
            String addressJsonStr = resultMap.getJSONObject("data").getJSONObject(address).toString();
            BlockchairAddrAbstract blockchairAddrAbstract = JSONObject.parseObject(addressJsonStr, BlockchairAddrAbstract.class);

            return blockchairAddrAbstract;
        } else {
            logger.error("getBTCAddress failed: " + response.getBody());
            return null;
        }
    }

    public BlockchairBTCTxObj getBTCTx(List<String> txidList) {

        String txidListStr = txidList.toString().replaceAll("[\\[ \\]]", "");
        String url =rootUrl + "/bitcoin/dashboards/transactions/" + txidListStr;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();

            BlockchairBTCTxObj blockchairBTCTxObj = JSONObject.parseObject(result, BlockchairBTCTxObj.class);

            JSONObject resultMap = JSONObject.parseObject(result);

            List<BlockchairTxAbstract> blockchairTxAbstractList = new ArrayList<>();
            for(String txid : txidList) {
                String txJsonStr = resultMap.getJSONObject("data").getJSONObject(txid).toString();
                BlockchairTxAbstract blockchairTxAbstract = JSONObject.parseObject(txJsonStr, BlockchairTxAbstract.class);
                blockchairTxAbstractList.add(blockchairTxAbstract);
            }

            blockchairBTCTxObj.setBlockchairTxAbstractList(blockchairTxAbstractList);

            return blockchairBTCTxObj;
        } else {
            logger.error("getBTCTx failed: " + response.getBody());
            return null;
        }
    }
}
