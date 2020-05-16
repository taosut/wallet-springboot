package org.blockchain.wallet.resttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class DNCRestAPI {

    @Autowired
    RestTemplate restTemplate;

    @Value("${dnc.root.url}")
    String rootUrl;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getHotCoin() {
        String url = rootUrl + "/api/coin/hotcoin_search?page={page}&pagesize={pagesize}&webp={webp}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("page","1");
        map.put("pagesize","20");
        map.put("webp","1");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }

    public String getConceptList() {
        String url = rootUrl + "/api/concept/web-conceptlist?page={page}&webp={webp}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("page", "1");
        map.put("webp", "1");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }

    public String getConceptDetail(String id) {

        String url = rootUrl + "/api/concept/web-conceptdetail?webp={webp}&id={id}&page={page}&pagesize={pagesize}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("page", "1");
        map.put("id", id);
        map.put("webp","1");
        map.put("pagesize","100");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }

    public String getTurnOver() {
        String url = rootUrl + "/api/v2/ranking/turnover?pagesize={pagesize}&webp={webp}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("pagesize", "30");
        map.put("webp", "1");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }

    public String getListingLatest() {
        String url = rootUrl + "/api/coin/web-coinrank?webp={webp}&pagesize={pagesize}&page={page}&type={type}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("page", "1");
        map.put("type", "-1");
        map.put("webp","1");
        map.put("pagesize","100");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }
}
