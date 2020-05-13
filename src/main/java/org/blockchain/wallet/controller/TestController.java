package org.blockchain.wallet.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping(value = "UTC")
    public String getUTC() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.SSS'Z'");

        Instant instant = Instant.now();

        try {
            String dateStr = instant.toString();

            Date date = dateFormat.parse(dateStr);

            return dateStr + "\n" + date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
