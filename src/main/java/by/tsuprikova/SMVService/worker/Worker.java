package by.tsuprikova.SMVService.worker;

import by.tsuprikova.SMVService.model.LegalPersonRequest;
import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.model.InfoOfFineNaturalPerson;
import by.tsuprikova.SMVService.repositories.LegalPersonRequestRepository;
import by.tsuprikova.SMVService.repositories.NaturalPersonInfoRepository;
import by.tsuprikova.SMVService.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.SMVService.repositories.ResponseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Component
@AllArgsConstructor
@Slf4j
public class Worker extends Thread {

    private NaturalPersonRequestRepository naturalPersonRequestRepository;
    private NaturalPersonInfoRepository infoRepository;
    private ResponseRepository responseRepository;
    private LegalPersonRequestRepository legalPersonRequestRepository;


    @Override
    public void run() {

        while (true) {

            log.info("Worker is working....");
            while (true) {
                UUID firstNaturalPersonRequestId = naturalPersonRequestRepository.findFirstId();
                UUID firstLegalPersonRequestId = legalPersonRequestRepository.findFirstId();

                while (firstNaturalPersonRequestId == null && firstLegalPersonRequestId == null) {
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    firstNaturalPersonRequestId = naturalPersonRequestRepository.findFirstId();
                    firstLegalPersonRequestId = legalPersonRequestRepository.findFirstId();
                }

                if (firstNaturalPersonRequestId != null) {
                    workWithNaturalPersonRepository(firstNaturalPersonRequestId);
                }
                if (firstLegalPersonRequestId != null) {
                    workWithLegalPersonRepository(firstLegalPersonRequestId);
                }

            }
        }

    }


    private void workWithNaturalPersonRepository(UUID id) {

        NaturalPersonRequest naturalPersonRequest = naturalPersonRequestRepository.getById(id);
        String sts = naturalPersonRequest.getSts();

        InfoOfFineNaturalPerson infoOfFineNaturalPerson = infoRepository.findBySts(sts);

        if (infoOfFineNaturalPerson != null) {

            ResponseWithFine response = responseRepository.findBySts(sts);

            if (response == null) {
                response = new ResponseWithFine(infoOfFineNaturalPerson.getAmountOfAccrual(),
                        infoOfFineNaturalPerson.getAmountOfPaid(), infoOfFineNaturalPerson.getNumberOfResolution(),
                        sts, infoOfFineNaturalPerson.getDateOfResolution(), infoOfFineNaturalPerson.getArticleOfKoap());
                responseRepository.save(response);
            }
        }
        naturalPersonRequestRepository.delete(id);


    }


    private void workWithLegalPersonRepository(UUID id) {

        LegalPersonRequest legalPersonRequest = legalPersonRequestRepository.getById(id);
        String sts = legalPersonRequest.getSts();

        ResponseWithFine response = responseRepository.findBySts(sts);
        if (response == null) {
            response = new ResponseWithFine(new BigDecimal(44), new BigDecimal(44),
                    1212, legalPersonRequest.getSts(), new Date(), "32.1");

            responseRepository.save(response);
        }
        legalPersonRequestRepository.delete(id);


    }


}
