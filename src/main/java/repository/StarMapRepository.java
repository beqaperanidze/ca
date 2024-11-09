package repository;

import entity.StarMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarMapRepository extends JpaRepository<StarMap, Long> {
}
