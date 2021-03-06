package com.ing.interview.domain.dto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car extends RepresentationModel<Car> {

    @Column(name = "ID", nullable = false)
    @GeneratedValue(generator = "Sequence", strategy = SEQUENCE)
    @SequenceGenerator(name = "Sequence", sequenceName = "SEQUENCE_CAR", allocationSize = 1)
    @Id
    private Long id;

    private String color;

    private String model;

    private LocalDate orderDate;

}
