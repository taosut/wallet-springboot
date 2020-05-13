package org.blockchain.wallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Date;

/**
 * @author hxy
 */
@RestController
@RequestMapping(value = "/update")
@RequiredArgsConstructor
public class AppUpdateController {

    @Value("${app.update.xml}")
    String xmlFilePath;

    @Value("${app.update.apk}")
    String apkFilePath;

    @RequestMapping(value = "/xml")
    public ResponseEntity<FileSystemResource> getUpdateXml() {
        File file = new File(xmlFilePath);
        return export(file);
    }

    @RequestMapping(value = "/apk")
    public ResponseEntity<FileSystemResource> getUpdateApk() {
        File file = new File(apkFilePath);
        return export(file);
    }

    public ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
    }
}
