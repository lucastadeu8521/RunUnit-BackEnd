package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.MembershipTypeService;
import com.rununit.rununit.web.dto.membershiptype.MembershipTypeCreationRequestDto;
import com.rununit.rununit.web.dto.membershiptype.MembershipTypeResponseDto;
import com.rununit.rununit.web.dto.membershiptype.MembershipTypeUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/membership-types")
public class MembershipTypeController {

    private final MembershipTypeService membershipTypeService;

    public MembershipTypeController(MembershipTypeService membershipTypeService) {
        this.membershipTypeService = membershipTypeService;
    }

    @PostMapping
    public ResponseEntity<MembershipTypeResponseDto> createMembershipType(
            @Valid @RequestBody MembershipTypeCreationRequestDto requestDto) {

        MembershipTypeResponseDto responseDto = membershipTypeService.createMembershipType(requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping
    public List<MembershipTypeResponseDto> getAllMembershipTypes() {
        return membershipTypeService.findAllMembershipTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipTypeResponseDto> getMembershipTypeById(@PathVariable Long id) {
        MembershipTypeResponseDto responseDto = membershipTypeService.findMembershipTypeById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipTypeResponseDto> updateMembershipType(
            @PathVariable Long id,
            @Valid @RequestBody MembershipTypeUpdateRequestDto requestDto) {

        MembershipTypeResponseDto responseDto = membershipTypeService.updateMembershipType(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembershipType(@PathVariable Long id) {
        membershipTypeService.deleteMembershipType(id);
        return ResponseEntity.noContent().build();
    }
}