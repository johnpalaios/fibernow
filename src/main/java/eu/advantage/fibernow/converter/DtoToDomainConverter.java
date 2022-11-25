package eu.advantage.fibernow.converter;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.model.Customer;

public class DtoToDomainConverter {
    public static Customer toDomain(CustomerDto dto) {
        if (dto == null) {
            return null;
        }
        return Customer.builder()
                .id(dto.getId())
                .tin(dto.getTin())
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .status(dto.getStatus())
                .build();
    }
}
