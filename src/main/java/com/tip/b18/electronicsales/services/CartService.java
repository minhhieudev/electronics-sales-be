package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.dto.CartDTO;
import com.tip.b18.electronicsales.dto.CartItemDTO;
import com.tip.b18.electronicsales.dto.CustomList;
import com.tip.b18.electronicsales.dto.OrderDetailDTO;
import com.tip.b18.electronicsales.entities.Cart;
import com.tip.b18.electronicsales.entities.Product;
import java.util.List;
import java.util.UUID;

public interface CartService {
    List<CartItemDTO> viewItemsInCart();
    void addItemToCart(CartItemDTO cartItemDTO);
    int deleteItemsInCart(CustomList<UUID> cartItemIdList);
    void updateItemInCart(CartItemDTO cartItemDTO);
    Product checkExitsColorAndProductAndProductColor(CartItemDTO cartItemDTO);
    int getTotalQuantityItemInCartByAccountId();
    int getTotalQuantityItemInCartByAccountId(UUID accountId);
    void deleteItemsInCartAfterCreateOrder(List<OrderDetailDTO> detailDTOList);
    void updateTotalPriceAndTotalQuantityOfCart(Cart cart);
    CartDTO getTotalQuantityItem();
}
