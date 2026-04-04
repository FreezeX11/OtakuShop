package com.Backend.Mapper;

import com.Backend.Entity.Address;
import com.Backend.Payload.Request.AddressRequest;
import com.Backend.Payload.Response.AddressResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AddressMapper {

    public Address toAddress(AddressRequest addressRequest) {
        Address address = new Address();

        address.setDistrict(addressRequest.getDistrict());
        address.setLandmark(addressRequest.getLandmark());
        address.setCreatedDate(LocalDateTime.now());

        return address;
    }

    public AddressResponse toAddressResponse(Address address) {
        AddressResponse addressResponse = new AddressResponse();

        addressResponse.setId(address.getId());
        addressResponse.setDistrict(address.getDistrict());
        addressResponse.setLandmark(address.getLandmark());

        return addressResponse;
    }
}
