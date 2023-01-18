package by.tsuprikova.SMVService.service.serviceImpl;


import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.SMVService.service.NaturalPersonRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaturalPersonRequestServiceImpl implements NaturalPersonRequestService {

    private final NaturalPersonRequestRepository naturalPersonRequestRepository;


    @Override
    public NaturalPersonRequest saveRequestForFine(NaturalPersonRequest naturalPersonRequest) {
        return naturalPersonRequestRepository.save(naturalPersonRequest);
    }
}
