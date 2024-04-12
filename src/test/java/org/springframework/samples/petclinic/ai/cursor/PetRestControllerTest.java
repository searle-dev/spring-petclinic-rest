package org.springframework.samples.petclinic.ai.cursor;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.controller.PetRestController;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.service.ClinicService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PetRestControllerTest {

    private final ClinicService     clinicService = mock(ClinicService.class);
    private final PetMapper         petMapper     = mock(PetMapper.class);
    private final PetRestController controller    = new PetRestController(clinicService, petMapper);

    @Test
    public void testUpdatePetExists() {
        PetDto petDto = new PetDto();
        Pet pet = new Pet();
        when(clinicService.findPetById(anyInt())).thenReturn(pet);
        when(petMapper.toPetType(any())).thenReturn(new PetType());
        when(petMapper.toPetDto(any())).thenReturn(petDto);

        ResponseEntity<PetDto> response = controller.updatePet(1, petDto);

        verify(clinicService, times(1)).savePet(pet);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(petDto, response.getBody());
    }

    @Test
    public void testUpdatePetNotExists() {
        PetDto petDto = new PetDto();
        when(clinicService.findPetById(anyInt())).thenReturn(null);

        ResponseEntity<PetDto> response = controller.updatePet(1, petDto);

        verify(clinicService, times(0)).savePet(any());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
