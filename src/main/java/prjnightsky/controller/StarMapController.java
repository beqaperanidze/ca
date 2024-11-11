package prjnightsky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prjnightsky.service.StarMapService;

@RestController
@RequestMapping("/starmap")
public class StarMapController {
    private final StarMapService starMapService;

    @Autowired
    public StarMapController(StarMapService starMapService) {
        this.starMapService = starMapService;
    }

    @PostMapping

}
