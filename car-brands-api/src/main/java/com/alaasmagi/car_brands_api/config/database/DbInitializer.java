package com.alaasmagi.car_brands_api.config.database;

import com.alaasmagi.car_brands_api.contract.data_access.CarJpaRepository;
import com.alaasmagi.car_brands_api.dto.data_access.dto.CarEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DbInitializer {

    private final CarJpaRepository carJpaRepository;

    public DbInitializer(CarJpaRepository carJpaRepository) {
        this.carJpaRepository = carJpaRepository;
    }

    @PostConstruct
    public void init() {
        if (carJpaRepository.count() > 0) return;

        CarEntity mercedes = save("Mercedes-Benz", null);
        CarEntity cKlass = save("C klass", mercedes.getId());
        save("C 160", cKlass.getId());
        save("C 180", cKlass.getId());
        save("C 200", cKlass.getId());
        save("C 220", cKlass.getId());

        CarEntity bmw = save("BMW", null);
        CarEntity s3 = save("3 seeria", bmw.getId());
        save("315", s3.getId());
        save("316", s3.getId());
        save("317", s3.getId());
        save("318", s3.getId());
        save("319", s3.getId());
        save("4 seeria", bmw.getId());
        CarEntity s5 = save("5 seeria", bmw.getId());
        save("518", s5.getId());
        save("520", s5.getId());
        save("523", s5.getId());
        save("524", s5.getId());
        save("525", s5.getId());

        CarEntity audi = save("Audi", null);
        save("A seeria", audi.getId());
        save("e-tron", audi.getId());
        CarEntity qSeeria = save("Q seeria", audi.getId());
        save("Q2", qSeeria.getId());
        save("Q3", qSeeria.getId());
        save("Q4", qSeeria.getId());
        save("Q5", qSeeria.getId());
        save("Q7", qSeeria.getId());
        CarEntity rsSeeria = save("RS seeria", audi.getId());
        save("RS4", rsSeeria.getId());
        save("RS5", rsSeeria.getId());
        save("RS6", rsSeeria.getId());
        save("TT", audi.getId());

        save("Citroën", null);
        save("Muu", null);
    }

    private CarEntity save(String name, UUID parentId) {
        CarEntity entity = new CarEntity();
        entity.setName(name);
        entity.setParentId(parentId);
        return carJpaRepository.save(entity);
    }
}
