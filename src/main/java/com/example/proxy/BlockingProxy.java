package com.example.proxy;

import java.net.MalformedURLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/file")
public class BlockingProxy {

    private static final String TEXT_FILE_URL = "http://www.sci.utah.edu/~macleod/docs/txt2html/sample.txt";

    @GetMapping("/bio")
    @ResponseBody
    public Resource bio() throws MalformedURLException {
        log.debug("Received request for download file");
        return new UrlResource(TEXT_FILE_URL);
    }

}
