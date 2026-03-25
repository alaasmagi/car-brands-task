package com.alaasmagi.car_brands_api.application;

import com.alaasmagi.car_brands_api.contract.data_access.IFormEntryRepository;
import com.alaasmagi.car_brands_api.domain.FormEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FormEntryServiceTest {

    @Mock
    private IFormEntryRepository formEntryRepository;

    @InjectMocks
    private FormEntryService formEntryService;

    @Test
    void createFormEntryDelegatesToRepositorySave() {
        FormEntry formEntry = createFormEntry(null);
        FormEntry savedFormEntry = createFormEntry(UUID.randomUUID());
        when(formEntryRepository.save(formEntry)).thenReturn(savedFormEntry);

        FormEntry result = formEntryService.createFormEntry(formEntry);

        assertSame(savedFormEntry, result);
        verify(formEntryRepository).save(formEntry);
    }

    @Test
    void updateFormEntrySetsIdBeforeDelegatingToRepository() {
        UUID id = UUID.randomUUID();
        FormEntry formEntry = createFormEntry(null);
        FormEntry updatedFormEntry = createFormEntry(id);
        when(formEntryRepository.update(formEntry)).thenReturn(updatedFormEntry);

        FormEntry result = formEntryService.updateFormEntry(id, formEntry);

        assertEquals(id, formEntry.getId());
        assertSame(updatedFormEntry, result);
        verify(formEntryRepository).update(formEntry);
    }

    @Test
    void getFormEntryByIdReturnsRepositoryResult() {
        UUID id = UUID.randomUUID();
        Optional<FormEntry> expected = Optional.of(createFormEntry(id));
        when(formEntryRepository.findById(id)).thenReturn(expected);

        Optional<FormEntry> result = formEntryService.getFormEntryById(id);

        assertSame(expected, result);
        verify(formEntryRepository).findById(id);
    }

    @Test
    void deleteFormEntryDelegatesToRepositoryDelete() {
        UUID id = UUID.randomUUID();

        formEntryService.deleteFormEntry(id);

        verify(formEntryRepository).delete(id);
    }

    private FormEntry createFormEntry(UUID id) {
        FormEntry formEntry = new FormEntry();
        formEntry.setId(id);
        formEntry.setFullName("Mati Maasikas");
        formEntry.setContactPhone("+3725555555");
        formEntry.setValidDrivingLicense(true);
        formEntry.setSelectedCarIds(List.of(UUID.randomUUID()));
        return formEntry;
    }
}
