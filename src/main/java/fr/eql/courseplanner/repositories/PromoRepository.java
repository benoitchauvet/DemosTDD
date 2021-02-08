package fr.eql.courseplanner.repositories;

import fr.eql.courseplanner.entities.Promo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoRepository extends CrudRepository<Promo, Long> {
}
