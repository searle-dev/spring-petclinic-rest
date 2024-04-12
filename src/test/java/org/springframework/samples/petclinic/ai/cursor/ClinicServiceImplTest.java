package org.springframework.samples.petclinic.ai.cursor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.ClinicServiceImpl;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    public void testSavePet() throws DataAccessException {
        Pet pet = new Pet();
        clinicService.savePet(pet);

        verify(petRepository).save(pet);
    }
}
