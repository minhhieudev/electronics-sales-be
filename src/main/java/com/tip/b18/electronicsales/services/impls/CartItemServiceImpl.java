package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.dto.CartItemDTO;
import com.tip.b18.electronicsales.dto.CustomList;
import com.tip.b18.electronicsales.dto.OrderDetailDTO;
import com.tip.b18.electronicsales.entities.CartItem;
import com.tip.b18.electronicsales.entities.Product;
import com.tip.b18.electronicsales.entities.Cart;
import com.tip.b18.electronicsales.exceptions.InsufficientStockException;
import com.tip.b18.electronicsales.exceptions.NotFoundException;
import com.tip.b18.electronicsales.mappers.CartItemMapper;
import com.tip.b18.electronicsales.repositories.CartItemRepository;
import com.tip.b18.electronicsales.services.CartItemService;
import com.tip.b18.electronicsales.services.ProductColorService;
import com.tip.b18.electronicsales.utils.CompareUtil;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductColorService productColorService;

    @Override
    public List<CartItemDTO> findAllByCartId(UUID uuid) {
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(uuid);

        Set<UUID> productIds = cartItems
                .stream()
                .map(cartItem -> cartItem.getProduct().getId())
                .collect(Collectors.toSet());

        Map<UUID, List<String>> cartItemColors = productColorService.getColorsByProductIds(List.copyOf(productIds));

        return cartItemMapper.toCartItemList(cartItemRepository.findAllByCartId(uuid), cartItemColors);
    }

    @Override
    public void addNewItemToCart(Cart cart, Product product, CartItemDTO cartItemDTO) {
        CartItem cartItem = cartItemRepository.findByCartIdAndProductIdAndColor(cart.getId(), product.getId(), cartItemDTO.getColor());
        boolean newCartItem = (cartItem == null);

        if(newCartItem){
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setColor(cartItemDTO.getColor());
            cartItem.setTotalPrice(BigDecimal.ZERO);
            cartItem.setQuantity(0);
        }

        if(CompareUtil.isFirstNumberSmaller(product.getStock(), cartItem.getQuantity() + cartItemDTO.getQuantity())){
            throw new InsufficientStockException(String.format(MessageConstant.ERROR_INSUFFICIENT_STOCK, product.getName()));
        }

        cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());
        cartItem.setTotalPrice(cartItem.getTotalPrice().add(product.getDiscountPrice().multiply(BigDecimal.valueOf(cartItemDTO.getQuantity()))));

        cartItemRepository.save(cartItem);
    }

    @Override
    public boolean existCartItem(Cart cart, Product product, String color) {
        return cartItemRepository.findByCartIdAndProductIdAndColor(cart.getId(), product.getId(), color) != null;
    }

    @Override
    public List<CartItem> deleteItemsInCart(CustomList<UUID> uuidList) {
        List<CartItem> cartItems = cartItemRepository.findAllByIdIn(uuidList.getItems());
        cartItemRepository.deleteAll(cartItems);

        return cartItems;
    }

    @Override
    public void updateQuantityOrColorItemInCart(Cart cart, CartItemDTO cartItemDTO, Product product) {
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), cartItemDTO.getId());
        if (cartItem == null){
            throw new NotFoundException(MessageConstant.ERROR_NOT_FOUND_PRODUCT_IN_CART);
        }

        boolean isUpdate = false;
        if(cartItemDTO.getQuantity() > 0){
            if(CompareUtil.isFirstNumberSmaller(product.getStock(), cartItemDTO.getQuantity())){
                throw new InsufficientStockException(String.format(MessageConstant.ERROR_INSUFFICIENT_STOCK, product.getName()));
            }

            cartItem.setQuantity(cartItemDTO.getQuantity());
            cartItem.setTotalPrice(product.getDiscountPrice().multiply(new BigDecimal(cartItemDTO.getQuantity())));
            isUpdate = true;
        }

        if(!CompareUtil.compare(cartItemDTO.getColor(), cartItem.getColor())){
            cartItem.setColor(cartItemDTO.getColor());
            isUpdate = true;
        }

        if(isUpdate){
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public Tuple calculatorTotalPriceAndTotalQuantityOfCart(UUID cartId) {
        return cartItemRepository.calculatorTotalPriceAndTotalQuantityOfCart(cartId);
    }

    @Override
    public void deleteItemsInCart(Cart cart, List<OrderDetailDTO> detailDTOList) {
        Set<UUID> productIdsSet = detailDTOList
                .stream()
                .map(OrderDetailDTO::getId)
                .collect(Collectors.toSet());

        List<CartItem> cartItems = cartItemRepository.findAllByCartIdAndProductIdIn(cart.getId(), productIdsSet);
        cartItemRepository.deleteAll(cartItems);
    }
}
