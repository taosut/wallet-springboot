package org.blockchain.wallet.resttemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.blockchain.wallet.entity.CryptoBroadcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Component
public class CryptoRestAPI {

    @Autowired
    RestTemplate restTemplate;

    @Value("${crypto.root.url}")
    String rootUrl;

    @Value("${crypto.api.key}")
    String apiKey;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getBCHAddressInfo(String address) {
        String url = rootUrl + "/v1/bc/bch/testnet/address/" + address;

        HttpHeaders headers = new HttpHeaders();

        headers.add("X-API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        return response.getBody();
    }

    public String getBCHTxList(String address) {
        String url = rootUrl + "/v1/bc/bch/testnet/address/" + address + "/unconfirmed-transactions";

        HttpHeaders headers = new HttpHeaders();

        headers.add("X-API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        JSONArray result = JSON.parseObject(response.getBody()).getJSONArray("payload");
        List<String> txList = JSON.parseArray(result.toJSONString(), String.class);

        url = rootUrl + "/v1/bc/bch/testnet/address/" + address + "/transactions";

        response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        result = JSON.parseObject(response.getBody()).getJSONArray("payload");

        txList.addAll(JSON.parseArray(result.toJSONString(), String.class));

        return txList.toString();
    }

    public String getBCHTxInfo(String hash) {
        String url = rootUrl + "/v1/bc/bch/testnet/txs/txid/" + hash;

        HttpHeaders headers = new HttpHeaders();

        headers.add("X-API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        return response.getBody();
    }

    public String getBCHTxFee() {
        String url = rootUrl + "/v1/bc/bch/testnet/txs/fee";

        HttpHeaders headers = new HttpHeaders();

        headers.add("X-API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        return response.getBody();
    }

    public String broadcastBch(CryptoBroadcast cryptoBroadcast) {
        String url = rootUrl + "/v1/bc/bch/testnet/txs/send";

        String requestBody = JSONObject.toJSONString(cryptoBroadcast);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody,headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, new HashMap<>());

        return response.getBody();
    }

    public String getLTCTxFee() {
        String url = rootUrl + "/v1/bc/ltc/testnet/txs/fee";

        HttpHeaders headers = new HttpHeaders();

        headers.add("X-API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        return response.getBody();
    }
}
