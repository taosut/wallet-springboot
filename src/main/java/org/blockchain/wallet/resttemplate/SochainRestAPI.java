package org.blockchain.wallet.resttemplate;

import com.alibaba.fastjson.JSONObject;
import org.blockchain.wallet.entity.SochainBroadcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class SochainRestAPI {

    @Autowired
    RestTemplate restTemplate;

    @Value("${sochain.root.url}")
    String rootUrl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getAddressInfo(String address) {
        String url = rootUrl + "/api/v2/address/LTCTEST/" + address;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());

        return response.getBody();
    }

    public String getTxInfo(String txid) {
        String url = rootUrl + "/api/v2/tx/LTCTEST/" + txid;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());

        return response.getBody();
    }

    public String broadcast(SochainBroadcast sochainBroadcast) {
        String url = rootUrl + "/api/v2/send_tx/";

        String requestBody = JSONObject.toJSONString(sochainBroadcast);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody,headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, new HashMap<>());

        return response.getBody();
    }
}
