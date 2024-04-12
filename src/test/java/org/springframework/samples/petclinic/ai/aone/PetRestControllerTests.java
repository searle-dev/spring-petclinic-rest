package org.springframework.samples.petclinic.ai.aone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.rest.controller.PetRestController;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PetRestControllerTests {

    @InjectMocks
    private PetRestController petRestController;

    @Mock
    private ClinicService clinicService;

    @Mock
    private PetMapper petMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // 测试用例1: 当用户具有OWNER_ADMIN角色时，更新宠物成功
    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    public void testUpdatePetWithOwnerAdminRole() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("OldName");
        pet.setBirthDate(LocalDate.now());

        PetDto petDto = new PetDto();
        petDto.setId(1);
        petDto.setName("NewName");
        petDto.setBirthDate(LocalDate.now());
        petDto.setType(new PetTypeDto());

        when(clinicService.findPetById(any(Integer.class))).thenReturn(pet);
        doNothing().when(clinicService).savePet(any());

        ResponseEntity<PetDto> responseEntity = petRestController.updatePet(1, petDto);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(clinicService, times(1)).findPetById(1);
        verify(clinicService, times(1)).savePet(pet);
        verify(petMapper, times(1)).toPetDto(pet);
    }

    // 测试用例2: 当用户不具有OWNER_ADMIN角色时，返回403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    public void testUpdatePetWithoutOwnerAdminRole() {

        ResponseEntity<PetDto> responseEntity = petRestController.updatePet(1, new PetDto());

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        verify(clinicService, never()).savePet(any(Pet.class));
        verify(petMapper, never()).toPetType(any(PetTypeDto.class));
    }

    // 测试用例3: 当请求的宠物ID不存在时，返回404 Not Found
    @Test
    public void testUpdatePetWithNonExistentPetId() {
        when(clinicService.findPetById(any(Integer.class))).thenReturn(null);

        ResponseEntity<PetDto> responseEntity = petRestController.updatePet(1, new PetDto());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(clinicService, times(1)).findPetById(1);
        verify(petMapper, never()).toPetType(any(PetTypeDto.class));
    }
}
