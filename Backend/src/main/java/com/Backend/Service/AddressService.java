package com.Backend.Service;

import com.Backend.Entity.Address;
import com.Backend.Entity.User;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.AddressMapper;
import com.Backend.Payload.Request.AddressRequest;
import com.Backend.Payload.Response.AddressResponse;
import com.Backend.Repository.AddressRepository;
import com.Backend.ServiceInterface.IAddressService;
import com.Backend.Util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final AuthUtil authUtil;

    @Override
    public void addAddress(AddressRequest addressRequest) {
        User user = authUtil.loggedInUser();

        boolean exists = addressRepository
                .existsByDistrictAndLandmarkIgnoreCaseAndUserId(
                        addressRequest.getDistrict(),
                        addressRequest.getLandmark().trim(),
                        user.getId()
                );

        if (exists) {
            throw new ResourceAlreadyExistException(
                    "This landmark already exists in this district"
            );
        }

        Address address = addressMapper.toAddress(addressRequest);
        address.setUser(user);
    }

    @Override
    public void updateAddress(Long id, AddressRequest addressRequest) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This address doesn't exist"));

        existingAddress.setDistrict(addressRequest.getDistrict());
        existingAddress.setLandmark(addressRequest.getLandmark());

        addressRepository.save(existingAddress);
    }

    @Override
    public void deleteAddress(Long id) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This address doesn't exist"));

        addressRepository.delete(existingAddress);
    }

    @Override
    public List<AddressResponse> getAddressesByUser() {
        return addressRepository.findByUserId(authUtil.loggedInUser().getId()).stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }
}
