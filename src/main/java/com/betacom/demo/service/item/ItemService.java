package com.betacom.demo.service.item;

import com.betacom.demo.dto.item.ItemRequest;
import com.betacom.demo.dto.item.ItemResponse;
import com.betacom.demo.exception.InvalidItemStructureException;
import com.betacom.demo.exception.UserDoesNotExistException;
import com.betacom.demo.model.item.Item;
import com.betacom.demo.model.security.User;
import com.betacom.demo.repository.ItemRepository;
import com.betacom.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ItemResponse> createItem(ItemRequest itemRequest) {
        if (itemRequest.getOwner() == null || itemRequest.getName() == null) {
            throw new InvalidItemStructureException("Invalid format of structure for Item. Body should contain fields: owner, name");
        }
        Optional<User> user = userRepository.findById(itemRequest.getOwner());
        if (user.isEmpty()) {
            throw new UserDoesNotExistException(String.format("Could not find User with id: %s", itemRequest.getOwner()));
        }
        Item item = Item.builder()
                .owner(itemRequest.getOwner())
                .name(itemRequest.getName())
                .build();
        itemRepository.save(item);
        log.info("Item has been saved: " + item.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToItemResponse(item));
    }

    public List<ItemResponse> getAllUserItems() {
        return itemRepository.findAllUserItems(new UUID(11, 1));
    }

    private ItemResponse mapToItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .owner(item.getOwner())
                .name(item.getName())
                .build();
    }
}
