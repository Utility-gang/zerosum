package com.utilitygang.zerosum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class IndexController {
    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("/portfolio");
    }
}
