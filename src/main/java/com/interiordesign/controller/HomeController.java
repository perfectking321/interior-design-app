package com.interiordesign.controller;

import com.interiordesign.model.Room;
import com.interiordesign.model.RoomLayout;
import com.interiordesign.service.LayoutService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final LayoutService layoutService;

    public HomeController(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("room", new Room());
        return "room-form";
    }

    @PostMapping("/layout")
    public String generateLayout(@Valid @ModelAttribute("room") Room room,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "room-form";
        }

        // create layout
        RoomLayout layout = layoutService.createLayout(room);

        // add warnings/errors if any
        model.addAttribute("layout", layout);
        model.addAttribute("scale", 60); // pixels per meter for rendering
        return "layout-result";
    }
}
