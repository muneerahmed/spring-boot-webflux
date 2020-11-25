package com.spring.example.web;

import java.net.MalformedURLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * curl -v http://localhost:8080/bio-file?uri=https://www.w3.org/TR/PNG/iso_8859-1.txt
 * curl -v http://localhost:8080/bio-file?uri=http://www.sci.utah.edu/~macleod/docs/txt2html/sample.txt
 * curl -v http://localhost:8080/bio-file?uri=http://ipv4.download.thinkbroadband.com/5MB.zip > output.zip
 * curl -v http://localhost:8080/bio-file?uri=http://ipv4.download.thinkbroadband.com/512MB.zip > output.zip
 *
 */
@Slf4j
@Controller
@RequestMapping
public class BlockingIOController {

    @GetMapping("bio-file")
    @ResponseBody
    public Resource bio(@RequestParam("uri") String uri) throws MalformedURLException {
        log.debug("Received request for download file");
        return new UrlResource(uri);
    }

}
