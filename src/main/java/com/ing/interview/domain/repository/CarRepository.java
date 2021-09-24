package com.ing.interview.domain.repository;

import com.ing.interview.domain.dto.Car;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface CarRepository extends JpaRepository<Car, Serializable> {
    Car findById(@NonNull Long id);
}