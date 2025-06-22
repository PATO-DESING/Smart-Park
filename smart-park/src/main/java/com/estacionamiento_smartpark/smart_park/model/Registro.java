package com.estacionamiento_smartpark.smart_park.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "registro")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(nullable = false)
    private LocalDateTime horaLlegada;

    @Column(nullable = true)
    private LocalDateTime horaSalida;

    @ManyToOne 
    @JoinColumn(name = "auto_id", nullable = false)
    private Auto auto;

}
