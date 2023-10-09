package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

public class ItemControllerTest {

	private ItemController itemController;
	
	private ItemRepository itemRepo = mock(ItemRepository.class);
	
	@Before
	public void setUp() {
		itemController = new ItemController();
		TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
	}
	@Test
    public void testGetItems() {
		Item item0 = new Item(0l, "testItemName0", new BigDecimal("1.00"), "testItemDesc0");
		Item item1 = new Item(0l, "testItemName1", new BigDecimal("2.00"), "testItemDesc1");
        List<Item> items = new ArrayList<>(2);
        items.add(item0);
        items.add(item1);
        when(itemRepo.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(2, retrievedItems.size());
        assertEquals(item0, retrievedItems.get(0));
        assertEquals(item1, retrievedItems.get(1));
    }

    @Test
    public void testGetItemById() {
    	Item item = new Item(0l, "testItemName", new BigDecimal("1.00"), "testItemDesc");
    	when(itemRepo.findById(0L)).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item retrievedItem = response.getBody();
        assertEquals(item, retrievedItem);
        assertNotNull(retrievedItem);
        assertEquals(item.getName(), retrievedItem.getName());
        assertEquals(item.getId(), retrievedItem.getId());
        assertEquals(item.getDescription(), retrievedItem.getDescription());
    }

    @Test
    public void testGetItemsByName() {
    	Item item = new Item(0l, "testItemName", new BigDecimal("1.00"), "testItemDesc");
        List<Item> items = new ArrayList<>(2);
        items.add(item);
        when(itemRepo.findByName("testItemName0")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItemName0");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(1, retrievedItems.size());
        assertEquals(item, retrievedItems.get(0));
    }
}
