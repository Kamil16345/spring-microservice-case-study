package com.betacom.demo.repository;

import com.betacom.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

}
