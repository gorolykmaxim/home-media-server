package com.gorolykmaxim.homemediaapp.service.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMediaAppController {

    @GetMapping
    public String index() {
        return "redirect:/torrent";
    }

}
