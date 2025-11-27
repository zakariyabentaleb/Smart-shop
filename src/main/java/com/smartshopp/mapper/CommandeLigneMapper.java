package com.smartshopp.mapper;

import com.smartshopp.dto.ClientDTO;
import com.smartshopp.dto.CommandeLigneDTO;
import com.smartshopp.model.Client;
import com.smartshopp.model.CommandeLigne;

public interface CommandeLigneMapper {
    CommandeLigneDTO toDTO(CommandeLigne commandeLigne);
    CommandeLigne toEntity(CommandeLigneDTO commandeLigneDTO);

}
