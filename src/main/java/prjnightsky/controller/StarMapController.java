package prjnightsky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prjnightsky.entity.StarMap;
import prjnightsky.exception.StarMapNotFoundException;
import prjnightsky.exception.UserNotFoundException;
import prjnightsky.service.StarMapService;

import java.util.List;

@RestController
@RequestMapping("/star-maps")
public class StarMapController {
    private final StarMapService starMapService;

    @Autowired
    public StarMapController(StarMapService starMapService) {
        this.starMapService = starMapService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<StarMap> createStarmap(@PathVariable Long userId, @RequestBody StarMap starMap) {
        try {
            StarMap newStarMap = starMapService.createStarmap(userId, starMap);
            return new ResponseEntity<>(newStarMap, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<StarMap>> getAllStarMaps() {
        List<StarMap> starMaps = starMapService.getAllStarMaps();
        return new ResponseEntity<>(starMaps, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<StarMap> getStarMapById(@PathVariable Long id) {
        return starMapService.getStarMapById(id)
                .map(starMap -> new ResponseEntity<>(starMap, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StarMap>> getStarMapByUser(@PathVariable Long userId) {
        List<StarMap> starMaps = starMapService.getStarMapByUser(userId);
        return new ResponseEntity<>(starMaps, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StarMap> updateStarMap(@PathVariable Long id, @RequestBody StarMap starMap) {
        try {
            StarMap updatedStarMap = starMapService.updateStarMap(id, starMap);
            return new ResponseEntity<>(updatedStarMap, HttpStatus.OK);
        } catch (StarMapNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStarMap(@PathVariable Long id) {
        try {
            starMapService.deleteStarMap(id);
            return ResponseEntity.noContent().build();
        } catch (StarMapNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
