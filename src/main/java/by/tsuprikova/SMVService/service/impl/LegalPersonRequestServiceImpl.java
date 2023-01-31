package by.tsuprikova.SMVService.service.impl;


import by.tsuprikova.SMVService.model.LegalPersonRequest;
import by.tsuprikova.SMVService.repositories.LegalPersonRequestRepository;
import by.tsuprikova.SMVService.service.LegalPersonRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LegalPersonRequestServiceImpl  implements LegalPersonRequestService {

    private final LegalPersonRequestRepository legalPersonRequestRepository;

    @Override
    public LegalPersonRequest saveRequestForFine(LegalPersonRequest legalPersonRequest) {
        legalPersonRequest.setId(UUID.randomUUID());
        return legalPersonRequestRepository.save(legalPersonRequest);
    }
}
