package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.MembershipType;
import com.rununit.rununit.infrastructure.repositories.MembershipTypeRepository;
import com.rununit.rununit.web.dto.membershiptype.MembershipTypeCreationRequestDto;
import com.rununit.rununit.web.dto.membershiptype.MembershipTypeResponseDto;
import com.rununit.rununit.web.dto.membershiptype.MembershipTypeUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipTypeService {

    private final MembershipTypeRepository membershipTypeRepository;

    public MembershipTypeService(MembershipTypeRepository membershipTypeRepository) {
        this.membershipTypeRepository = membershipTypeRepository;
    }

    private MembershipTypeResponseDto toResponseDto(MembershipType membershipType) {
        return new MembershipTypeResponseDto(
                membershipType.getId(),
                membershipType.getName(),
                membershipType.getMonthlyPrice(),
                membershipType.getDescription(),
                membershipType.getCreatedAt()
        );
    }

    private MembershipType toEntity(MembershipTypeCreationRequestDto requestDto) {
        MembershipType type = new MembershipType();
        type.setName(requestDto.name());
        type.setMonthlyPrice(requestDto.monthlyPrice());
        type.setDescription(requestDto.description());
        return type;
    }

    @Transactional
    public MembershipTypeResponseDto createMembershipType(MembershipTypeCreationRequestDto requestDto) {
        if (membershipTypeRepository.existsByNameIgnoreCase(requestDto.name())) {
            throw new RuntimeException("Membership type name already exists: " + requestDto.name());
        }

        MembershipType newType = toEntity(requestDto);
        MembershipType savedType = membershipTypeRepository.save(newType);
        return toResponseDto(savedType);
    }

    public List<MembershipTypeResponseDto> findAllMembershipTypes() {
        return membershipTypeRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public MembershipTypeResponseDto findMembershipTypeById(Long id) {
        return membershipTypeRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new RuntimeException("MembershipType not found with ID: " + id));
    }

    @Transactional
    public MembershipTypeResponseDto updateMembershipType(Long id, MembershipTypeUpdateRequestDto requestDto) {
        MembershipType type = membershipTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MembershipType not found with ID: " + id));

        if (requestDto.name() != null) {
            if (membershipTypeRepository.findByNameIgnoreCase(requestDto.name())
                    .filter(existingType -> !existingType.getId().equals(id))
                    .isPresent()) {
                throw new RuntimeException("Membership type name already exists: " + requestDto.name());
            }
            type.setName(requestDto.name());
        }

        if (requestDto.monthlyPrice() != null) {
            type.setMonthlyPrice(requestDto.monthlyPrice());
        }
        if (requestDto.description() != null) {
            type.setDescription(requestDto.description());
        }

        MembershipType updatedType = membershipTypeRepository.save(type);
        return toResponseDto(updatedType);
    }

    @Transactional
    public void deleteMembershipType(Long id) {

        if (!membershipTypeRepository.existsById(id)) {
            throw new RuntimeException("MembershipType not found with ID: " + id);
        }

        membershipTypeRepository.deleteById(id);
    }
}