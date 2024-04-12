package org.springframework.samples.petclinic.ai.codefuse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.ClinicServiceImpl;

import static org.mockito.Mockito.*;

public class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSavePet() throws DataAccessException {
        // Arrange
        Pet pet = new Pet();

        // Act
        clinicService.savePet(pet);

        // Assert
        verify(petRepository, times(1)).save(pet);
    }
}
