package com.betacom.demo.repository;

import com.betacom.demo.dto.item.ItemResponse;
import com.betacom.demo.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    @Query("SELECT i FROM Item i WHERE i.owner = :owner")
    List<ItemResponse> findAllUserItems(@Param("owner") UUID owner);
}
