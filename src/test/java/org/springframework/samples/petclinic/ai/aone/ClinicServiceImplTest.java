package org.springframework.samples.petclinic.ai.aone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.ClinicServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ClinicServiceImplTest {

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Mock
    private PetRepository petRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    // 测试正常保存宠物的情况（#1）
    public void testSavePet_validPet() {
        Pet pet = new Pet(); // 创建一个简单的Pet对象
        assertDoesNotThrow(() -> clinicService.savePet(pet));
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    // 测试抛出DataAccessException异常的情况（#2）
    @Transactional
    public void testSavePet_dataAccessException() {
        Pet pet = new Pet();
        doThrow(new TypeMismatchDataAccessException("故意触发异常")).when(petRepository).save(any(Pet.class));
        assertThrows(DataAccessException.class, () -> clinicService.savePet(pet));
    }
}
