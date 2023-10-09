package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

public class CartControllerTest {

	private CartController cartController;
	
	private UserRepository userRepo = mock(UserRepository.class);
	private CartRepository cartRepo = mock(CartRepository.class);
	private ItemRepository itemRepo = mock(ItemRepository.class);
	
	@Before
	public void setUp() {
		cartController = new CartController();
		TestUtils.injectObjects(cartController, "userRepository", userRepo);
		TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
		TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
	}
	@Test
    public void addTocartSuccess() {
        User user = new User();
        user.setUsername("test");

        Item item = new Item(0l, "testItemName", new BigDecimal("1.00"), "testItemDesc");

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("1.00"));
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        Item retrievedItem = items.get(0);
        assertEquals(2, items.size());
        assertNotNull(retrievedItem);
        assertEquals(item, retrievedItem);
        assertEquals(new BigDecimal("2.00"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }

    @Test
    public void addTocartError() {
        
        when(userRepo.findByUsername(null)).thenReturn(null);
        when(itemRepo.findById(0L)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername(null);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromcart() {
        User user = new User();
        user.setUsername("test");

        Item item = new Item(0l, "testItemName", new BigDecimal("1.00"), "testItemDesc");

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("1.00"));
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
        assertEquals(new BigDecimal("0.00"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }
}
