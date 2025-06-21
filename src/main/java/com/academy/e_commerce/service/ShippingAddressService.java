package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.BusinessException;
import com.academy.e_commerce.advice.UnauthorizedAccessException;
import com.academy.e_commerce.dto.ShippingAddressResponse;
import com.academy.e_commerce.mapper.ShippingAddressMapper;
import com.academy.e_commerce.model.ShippingAddress;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.ShippingAddressRepository;
import com.academy.e_commerce.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {

    private final UserRepository userRepository;
    private final ShippingAddressRepository shippingAddressRepository;

    public List<ShippingAddressResponse> getAllAddressesForUser(Long userId) {
        return shippingAddressRepository.findByUserId(userId).stream()
                .map(ShippingAddressMapper::toResponse)
                .toList();
    }

    public ShippingAddressResponse getAddressById(Long addressId, Long userId) {
        ShippingAddress address = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("Shipping address not found"));

        if(!Objects.equals(address.getUser().getId(), userId))
            throw new UnauthorizedAccessException("This address doesn't belong to this user");

        return ShippingAddressMapper.toResponse(address);
    }

    public ShippingAddressResponse createAddressForUser(Long userId,@Valid ShippingAddressRequest newAddressRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setStreet(newAddressRequest.getStreet());
        shippingAddress.setCity(newAddressRequest.getCity());
        shippingAddress.setState(newAddressRequest.getState());
        shippingAddress.setCountry(newAddressRequest.getCountry());
        shippingAddress.setUser(user);

        ShippingAddress savedAddress = shippingAddressRepository.save(shippingAddress);
        return ShippingAddressMapper.toResponse(savedAddress);
    }

    public ShippingAddressResponse updateAddress(Long addressId, @Valid ShippingAddressRequest updatedAddressRequest ,Long userId) {
        ShippingAddress address = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("Shipping address not found"));

        if(!Objects.equals(address.getUser().getId(), userId))
            throw new UnauthorizedAccessException("This address doesn't belong to this user");

        address.setStreet(updatedAddressRequest.getStreet());
        address.setCity(updatedAddressRequest.getCity());
        address.setState(updatedAddressRequest.getState());
        address.setCountry(updatedAddressRequest.getCountry());

        ShippingAddress saved = shippingAddressRepository.save(address);
        return ShippingAddressMapper.toResponse(saved);
    }


    public void deleteAddress(Long addressId, Long userId) {

        ShippingAddress address = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("Shipping address not found"));

        if(!Objects.equals(address.getUser().getId(), userId))
            throw new UnauthorizedAccessException("This address doesn't belong to this user");

        shippingAddressRepository.deleteById(addressId);
    }
}

