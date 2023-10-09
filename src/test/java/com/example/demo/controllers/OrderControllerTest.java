package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class OrderControllerTest {

    private OrderController orderController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);
	
	@Before
	public void setUp() {
		orderController = new OrderController();
		TestUtils.injectObjects(orderController, "userRepository", userRepo);
		TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
	}
	
	@Test
	public void submitSuccess() throws Exception {
		String username = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(0L);
        Item item = new Item(0l, "testItemName", new BigDecimal("1.00"), "testItemDesc");
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("1.00"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserOrder> response =  orderController.submit(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder retrievedUserOrder = response.getBody();
        assertNotNull(retrievedUserOrder);
        assertNotNull(retrievedUserOrder.getItems());
        assertNotNull(retrievedUserOrder.getTotal());
        assertNotNull(retrievedUserOrder.getUser());
	}
	
	@Test
	public void submitError() throws Exception {
        when(userRepo.findByUsername(null)).thenReturn(null);

        ResponseEntity<UserOrder> response =  orderController.submit(null);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	public void getOrdersForUserSuccess() throws Exception {
		String username = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(0L);
        Item item = new Item(0l, "testItemName", new BigDecimal("1.00"), "testItemDesc");
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("1.00"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername(username)).thenReturn(user);

        ResponseEntity<List<UserOrder>> response =  orderController.getOrdersForUser(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrders = response.getBody();
        assertNotNull(userOrders);
        assertEquals(0, userOrders.size());
	}
	
	@Test
	public void getOrdersForUserError() throws Exception {
		String username = null;
        
        when(userRepo.findByUsername(username)).thenReturn(null);

        ResponseEntity<List<UserOrder>> response =  orderController.getOrdersForUser(username);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
	}
}
