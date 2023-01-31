package by.tsuprikova.SMVService.service.impl;


import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.SMVService.service.NaturalPersonRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaturalPersonRequestServiceImpl implements NaturalPersonRequestService {

    private final NaturalPersonRequestRepository naturalPersonRequestRepository;


    @Override
    public NaturalPersonRequest saveRequestForFine(NaturalPersonRequest naturalPersonRequest) {
        naturalPersonRequest.setId(UUID.randomUUID());
        return naturalPersonRequestRepository.save(naturalPersonRequest);
    }
}
