package com.betacom.demo.dto;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ItemResponse {
    @Id
    private UUID id;
    private UUID owner;
    private String name;
}
