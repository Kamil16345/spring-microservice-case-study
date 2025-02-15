package com.betacom.demo.model;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private UUID owner;
    private String name;
}
