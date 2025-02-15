package com.betacom.demo.service;

import com.betacom.demo.dto.ItemRequest;
import com.betacom.demo.dto.ItemResponse;
import com.betacom.demo.model.Item;
import com.betacom.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    public ResponseEntity<ItemResponse> createItem(ItemRequest itemRequest) {
        Item item = Item.builder()
                .id(UUID.randomUUID())
                .owner(itemRequest.getOwner())
                .name(itemRequest.getName())
                .build();
        itemRepository.save(item);
        log.info("Item has been saved: " + item.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToItemResponse(item));
    }

    public List<ItemResponse> getAllUserItems() {
        return itemRepository.findAllUserItems(new UUID(11,1));
    }

    private ItemResponse mapToItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .owner(item.getOwner())
                .name(item.getName())
                .build();
    }
}
