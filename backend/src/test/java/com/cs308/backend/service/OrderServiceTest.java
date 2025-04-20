package com.cs308.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.OrderStatus;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.OrderItemRepository;
import com.cs308.backend.repo.OrderRepository;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private User testUser;
    private Product testProduct;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Smartphone");

        testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setProduct(testProduct);
        testOrderItem.setQuantity(2);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
        testOrder.setStatus(OrderStatus.pending);
        testOrder.setOrderItems(List.of(testOrderItem));
    }

    @Test
    void testAddNewOrder() {
        when(orderRepository.save(testOrder)).thenReturn(testOrder);

        Optional<Order> addedOrder = orderService.addNewOrder(testOrder);

        assertThat(addedOrder).isPresent();
        assertThat(addedOrder.get().getId()).isEqualTo(1L);
        verify(orderRepository, times(1)).save(testOrder);
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderRepository.save(testOrder)).thenReturn(testOrder);

        Optional<Order> updatedOrder = orderService.updateOrderStatus(testOrder, "shipped");

        assertThat(updatedOrder).isPresent();
        assertThat(updatedOrder.get().getStatus()).isEqualTo(OrderStatus.shipped);
        verify(orderRepository, times(1)).save(testOrder);
    }

    @Test
    void testFindAllOrdersByUser() {
        when(orderRepository.findAllByUser(testUser)).thenReturn(List.of(testOrder));

        List<Order> orders = orderService.findAllOrdersByUser(testUser);

        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getUser()).isEqualTo(testUser);
        verify(orderRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    void testFindOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Optional<Order> order = orderService.findOrder(1L);

        assertThat(order).isPresent();
        assertThat(order.get().getId()).isEqualTo(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllOrderItemsByProduct() {
        when(orderItemRepository.findAllByProduct(testProduct)).thenReturn(List.of(testOrderItem));

        List<OrderItem> orderItems = orderService.findAllOrderItemsByProduct(testProduct);

        assertThat(orderItems).isNotEmpty();
        assertThat(orderItems.get(0).getProduct()).isEqualTo(testProduct);
        verify(orderItemRepository, times(1)).findAllByProduct(testProduct);
    }

    @Test
    void testFindAllOrdersIncludingProduct() {
        when(orderItemRepository.findAllByProduct(testProduct)).thenReturn(List.of(testOrderItem));
        when(orderRepository.findByOrderItemsContains(testOrderItem)).thenReturn(Optional.of(testOrder));

        List<Order> orders = orderService.findAllOrdersIncludingProduct(testProduct);

        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getOrderItems()).contains(testOrderItem);
        verify(orderItemRepository, times(1)).findAllByProduct(testProduct);
        verify(orderRepository, times(1)).findByOrderItemsContains(testOrderItem);
    }
}