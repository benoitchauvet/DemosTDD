package fr.eql.courseplanner.services;

import fr.eql.courseplanner.entities.Promo;
import fr.eql.courseplanner.repositories.PromoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoService implements IPromoService {

    @Autowired
    private PromoRepository repository;

    @Override
    public List<Promo> findAll() {
        return (List<Promo>)repository.findAll();
    }
}
