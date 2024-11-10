package repository;

import entity.StarMap;
import entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarMapRepository extends JpaRepository<StarMap, Long> {
    List<StarMap> findByUser(User user);
}
