package org.springframework.samples.petclinic.ai.codefuse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.controller.PetRestController;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.service.ClinicService;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetRestControllerTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetRestController petRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdatePetWithValidPetIdAndDto() {
        // Arrange
        Integer petId = 1;
        PetDto petDto = new PetDto();
        petDto.setBirthDate(LocalDate.parse("2021-01-01"));
        petDto.setName("Fluffy");
        PetTypeDto petType = new PetTypeDto();
        petType.setName("cat");
        petDto.setType(petType);

        Pet currentPet = new Pet();
        currentPet.setId(petId);
        currentPet.setBirthDate(LocalDate.parse("2020-01-01"));
        currentPet.setName("Fido");
        currentPet.setType(new PetType());

        when(clinicService.findPetById(petId)).thenReturn(currentPet);

        // Act
        ResponseEntity<PetDto> response = petRestController.updatePet(petId, petDto);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clinicService, times(1)).savePet(currentPet);
        verify(petMapper, times(1)).toPetDto(currentPet);
    }

    @Test
    public void testUpdatePetWithInvalidPetId() {
        // Arrange
        Integer petId = 1;
        PetDto petDto = new PetDto();
        petDto.setBirthDate(LocalDate.parse("2021-01-01"));
        petDto.setName("Fluffy");
        PetTypeDto petType = new PetTypeDto();
        petType.setName("cat");
        petDto.setType(petType);

        when(clinicService.findPetById(petId)).thenReturn(null);

        // Act
        ResponseEntity<PetDto> response = petRestController.updatePet(petId, petDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clinicService, never()).savePet(any(Pet.class));
        verify(petMapper, never()).toPetDto(any(Pet.class));
    }
}
