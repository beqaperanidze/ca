package prjnightsky.service;

import prjnightsky.entity.StarMap;
import prjnightsky.entity.User;
import prjnightsky.exception.StarMapNotFoundException;
import prjnightsky.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prjnightsky.repository.StarMapRepository;
import prjnightsky.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StarMapService {

    private final StarMapRepository starMapRepository;
    private final UserRepository userRepository;

    @Autowired
    public StarMapService(StarMapRepository starMapRepository, UserRepository userRepository) {
        this.starMapRepository = starMapRepository;
        this.userRepository = userRepository;
    }


    public StarMap createStarmap(Long userId, StarMap starMapData) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        StarMap newStarMap = new StarMap();
        newStarMap.setUser(user);
        newStarMap.setLocation(starMapData.getLocation());
        newStarMap.setDate(starMapData.getDate());
        newStarMap.setTime(starMapData.getTime());
        newStarMap.setColorScheme(starMapData.getColorScheme());

        return starMapRepository.save(newStarMap);
    }

    public List<StarMap> getAllStarMaps() {
        return starMapRepository.findAll();
    }

    public Optional<StarMap> getStarMapById(Long id) {
        return starMapRepository.findById(id);
    }

    public List<StarMap> getStarMapByUser(Long userId) {
        return starMapRepository.findByUser(userId);
    }

    public StarMap updateStarMap(Long id, StarMap newStarMap) throws StarMapNotFoundException {
        Optional<StarMap> starMapOpt = starMapRepository.findById(id);
        if (starMapOpt.isEmpty()) {
            throw new StarMapNotFoundException();
        }
        StarMap starMap = starMapOpt.get();
        starMap.setLocation(newStarMap.getLocation());
        starMap.setDate(newStarMap.getDate());
        starMap.setTime(newStarMap.getTime());
        starMap.setMapStyle(newStarMap.getMapStyle());
        starMap.setColorScheme(newStarMap.getColorScheme());
        starMap.setSize(newStarMap.getSize());
        starMap.setShowConstellations(newStarMap.isShowConstellations());
        starMap.setShowGrid(newStarMap.isShowGrid());
        starMap.setShowLabels(newStarMap.isShowLabels());
        starMap.setUser(newStarMap.getUser());
        return starMapRepository.save(starMap);
    }

    public void deleteStarMap(Long id) throws StarMapNotFoundException {
        Optional<StarMap> starMap = starMapRepository.findById(id);
        if (starMap.isEmpty()) {
            throw new StarMapNotFoundException();
        }
        starMapRepository.deleteById(id);
    }
}
