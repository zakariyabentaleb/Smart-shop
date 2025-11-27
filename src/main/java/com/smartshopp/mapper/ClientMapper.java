package com.smartshopp.mapper;

import com.smartshopp.dto.ClientDTO;
import com.smartshopp.model.Client;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ClientMapper {
    ClientDTO toDTO(Client client);
    Client toEntity(ClientDTO clientDTO);
}
