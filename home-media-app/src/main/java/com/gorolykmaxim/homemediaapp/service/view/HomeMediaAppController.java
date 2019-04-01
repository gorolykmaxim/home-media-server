package com.gorolykmaxim.homemediaapp.service.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMediaAppController {
    private String homePage;

    @Value("${home-media-app.view.home-page}")
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    @GetMapping
    public String index() {
        return String.format("redirect:%s", homePage);
    }

}
