package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.AddressRequest;
import com.Backend.Payload.Response.AddressResponse;

import java.util.List;

public interface IAddressService {
    void addAddress(AddressRequest addressRequest);
    void updateAddress(Long id, AddressRequest addressRequest);
    void deleteAddress(Long id);
    List<AddressResponse> getAddressesByUser();
}
