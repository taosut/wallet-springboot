package org.blockchain.wallet.resttemplate;

import com.alibaba.fastjson.JSONObject;
import org.blockchain.wallet.entity.DNCCoinPrice;
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
public class DNCRestAPI {

    @Autowired
    RestTemplate restTemplate;

    @Value("${dnc.root.url}")
    String rootUrl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getHotCoin() {
        String url = rootUrl + "/api/coin/hotcoin_search?page={page}&pagesize={pagesize}&webp={webp}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("page","1");
        map.put("pagesize","30");
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

    public String getGithub() {
        String url = rootUrl + "/api/coin/hotprojectgithub?page={page}&pagesize={pagesize}&sort={sort}&webp={webp}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("page", "1");
        map.put("sort", "devote");
        map.put("webp","1");
        map.put("pagesize","30");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }

    public String searchCoin(String code) {
        String url = rootUrl + "/api/search/websearch?webp={webp}&page={page}&exchange_page={exchange_page}" +
                "&pagesize={pagesize}&code={code}&wallet_page={wallet_page}&token={token}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("page", "1");
        map.put("exchange_page", "1");
        map.put("webp","1");
        map.put("pagesize","100");
        map.put("code", code);
        map.put("wallet_page", "1");
        map.put("token", "");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }

    public String coinDetail(String code) {

        String url = rootUrl + "/api/coin/web-coininfo";

        Map<String,String> map=new HashMap<String,String>();
        map.put("webp","1");
        map.put("code", code);
        map.put("token", "");

        String requestBody = JSONObject.toJSONString(map);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody,headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, new HashMap<>());

        return response.getBody();
    }

    public DNCCoinPrice getCoinPrice(String code) {

        String result = coinDetail(code);

        String dataStr = JSONObject.parseObject(result).get("data").toString();
        DNCCoinPrice dncCoinPrice = JSONObject.parseObject(dataStr, DNCCoinPrice.class);

        return dncCoinPrice;
    }

    public String getExchangeList() {
        String url = rootUrl + "/api/v2/exchange/web-exchange?page={page}&pagesize={pagesize}&sort_type={sort_type}" +
                "&asc={asc}&isinnovation={isinnovation}&type={type}&webp={webp}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("page", "1");
        map.put("isinnovation","1");
        map.put("type", "all");
        map.put("webp","1");
        map.put("pagesize","30");
        map.put("sort_type","exrank");
        map.put("asc","1");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }

    public String getExchangeDetail(String code) {
        String url = rootUrl + "/api/exchange/web-exchangeinfo?code={code}&webp={webp}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("code", code);
        map.put("webp","1");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }
}
