package com.academy.e_commerce.service.cartService;

import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.CartProductRepository;
import com.academy.e_commerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartHelperService {

    private final CartProductRepository cartProductRepository;
    private final CartRepository cartRepository;

    public void updateCartSubTotal(Cart cart) {
        double subTotal = cartProductRepository.findByCartId(cart.getId())
                .stream().mapToDouble(CartProduct::getSubPrice).sum();
        cart.setTotalPrice(subTotal);
        cartRepository.save(cart);
    }

    public void validateStock(Product product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new RuntimeException("Insufficient stock: Requested " + requestedQuantity + ", Available " + product.getStock());
        }
    }

}
