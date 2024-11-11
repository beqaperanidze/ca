package prjnightsky.repository;

import prjnightsky.entity.StarMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarMapRepository extends JpaRepository<StarMap, Long> {
    List<StarMap> findByUser(Long user);
}
