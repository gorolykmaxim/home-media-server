package com.gorolykmaxim.thumbnail.view;

import com.gorolykmaxim.thumbnail.api.ThumbnailApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class ThumbnailViewController {
    private ThumbnailApiController apiController;

    @Autowired
    public ThumbnailViewController(ThumbnailApiController apiController) {
        this.apiController = apiController;
    }

    @GetMapping
    public ModelAndView showAllThumbnails() {
        try {
            ModelAndView modelAndView = new ModelAndView("all-thumbnails");
            modelAndView.addObject("thumbnailNames", apiController.getAllThumbnails());
            return modelAndView;
        } catch (Exception e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("thumbnail-removal-confirmation")
    public ModelAndView showThumbnailRemovalConfirmationDialog(@RequestParam(required = false) String thumbnailName) {
        ModelAndView modelAndView = new ModelAndView("removal-confirmation");
        if (thumbnailName != null) {
            modelAndView.addObject("removalUrl", "/remove-thumbnail?thumbnailName=" + thumbnailName);
            modelAndView.addObject("message", "Are you sure, you want to delete " + thumbnailName);
        } else {
            modelAndView.addObject("removalUrl", "/remove-thumbnail");
            modelAndView.addObject("message", "Are you sure, you want to delete ALL existing thumbnails");
        }
        return modelAndView;
    }

    @PostMapping("remove-thumbnail")
    public String removeThumbnail(@RequestParam(required = false) String thumbnailName) {
        try {
            apiController.deleteThumbnails(thumbnailName);
            return "redirect:/";
        } catch (Exception e) {
            throw new ViewError(e);
        }
    }
}
