package prjnightsky.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/generate-star-map")
public class StarMapPrintController {

//    @PostMapping
//    public String generateStarMap(
//            @RequestParam String location,
//            @RequestParam String date,
//            @RequestParam String time,
//            @RequestParam String mapStyle,
//            @RequestParam String colorScheme,
//            @RequestParam String size,
//            @RequestParam(required = false) boolean showConstellations,
//            @RequestParam(required = false) boolean showGrid,
//            @RequestParam(required = false) boolean showLabels,
//            @RequestParam(required = false) String customMessage,
//            Model model) {
//
//
////        StarMap generatedMap = StarMapGenerator.generateCustomStarMap(location, date, time, mapStyle, colorScheme, size,
////                showConstellations, showGrid, showLabels, customMessage);
////
////        model.addAttribute("starMap", generatedMap);
////        return "starMapResult"; // Thymeleaf page to display the star map
//    }
}
