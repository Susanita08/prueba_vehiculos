package com.ing.interview.domain.repository;

import com.ing.interview.domain.dto.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

}