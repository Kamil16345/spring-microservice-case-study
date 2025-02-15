package com.betacom.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ItemRequest {
    private UUID id;
    private UUID owner;
    private String name;
}
