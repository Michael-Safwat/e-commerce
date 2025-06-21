package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.ShippingAddressResponse;
import com.academy.e_commerce.service.ShippingAddressRequest;
import com.academy.e_commerce.service.ShippingAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users/{userId}/addresses")
@RequiredArgsConstructor
public class ShippingAddressController {
    private final ShippingAddressService shippingAddressService;

    @GetMapping
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<List<ShippingAddressResponse>> getAllAddresses(@PathVariable Long userId) {
        List<ShippingAddressResponse> addresses = shippingAddressService.getAllAddressesForUser(userId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<ShippingAddressResponse> getAddressById(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        ShippingAddressResponse address = shippingAddressService.getAddressById(addressId,userId);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<ShippingAddressResponse> createAddress(
            @PathVariable Long userId,
            @Valid @RequestBody ShippingAddressRequest addressRequest) {
        ShippingAddressResponse created = shippingAddressService.createAddressForUser(userId, addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{addressId}")
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<ShippingAddressResponse> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @Valid @RequestBody ShippingAddressRequest addressRequest) {
        ShippingAddressResponse address = shippingAddressService.updateAddress(addressId,addressRequest,userId);
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        shippingAddressService.deleteAddress(addressId,userId);
        return ResponseEntity.noContent().build();
    }
}
