package com.wildlife.rescue.mapper;

import com.wildlife.rescue.dto.CaseDTO;
import com.wildlife.rescue.model.Case;

public class CaseMapper {

    public static CaseDTO toDTO(Case rescueCase) {
        CaseDTO dto = new CaseDTO();
        dto.setId(rescueCase.getId());
        dto.setAnimalType(rescueCase.getAnimalType());
        dto.setLocation(rescueCase.getLocation());
        dto.setLatitude(rescueCase.getLatitude());
        dto.setLongitude(rescueCase.getLongitude());
        dto.setDescription(rescueCase.getDescription());
        dto.setStatus(rescueCase.getStatus());
        dto.setCreatedAt(rescueCase.getCreatedAt());
        dto.setUpdatedAt(rescueCase.getUpdatedAt());
        return dto;
    }
}
