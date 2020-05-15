package org.blockchain.wallet.resttemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class JinseRestAPI {

    @Autowired
    RestTemplate restTemplate;

    @Value("${jinse.root.url}")
    String rootUrl;

    public String getLive() {
        String url = rootUrl + "/live/list";

        Map<String,String> map=new HashMap<String,String>();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        return response.getBody();
    }
}
