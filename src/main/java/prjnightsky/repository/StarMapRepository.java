package prjnightsky.repository;

import prjnightsky.entity.StarMap;
import prjnightsky.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarMapRepository extends JpaRepository<StarMap, Long> {
    List<StarMap> findByUser(User user);
}
