package org.blockchain.wallet.resttemplate;

import com.alibaba.fastjson.JSONObject;
import org.blockchain.wallet.entity.blockchain.BlockChainSingleAdr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
public class BlockChainRestAPI {

    @Value("${com.blockchain.monitor.http.rootUrl}")
    String rootUrl;

    @Autowired
    RestTemplate restTemplate;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public BlockChainSingleAdr getSingleAddress(String address, Long limit, Long offset) {
        String url =rootUrl + "/rawaddr/" + address + "?limit={limit}&offset={offset}";

        Map<String, Object> map = new HashMap<>();
        map.put("limit", limit);
        map.put("offset", offset);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            BlockChainSingleAdr blockChainSingleAdr = JSONObject.parseObject(result, BlockChainSingleAdr.class);
            return blockChainSingleAdr;
        } else {
            logger.info("getSingleAddress failed");
            return null;
        }
    }
}
