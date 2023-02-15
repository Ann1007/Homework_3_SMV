package by.tsuprikova.SMVService.worker;

import by.tsuprikova.SMVService.exceptions.SmvServerException;
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
                try {
                    UUID firstNaturalPersonRequestId = naturalPersonRequestRepository.findFirstId();
                    UUID firstLegalPersonRequestId = legalPersonRequestRepository.findFirstId();

                    while (firstNaturalPersonRequestId == null && firstLegalPersonRequestId == null) {
                        try {
                            Thread.sleep(500);

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

                } catch (SmvServerException e) {
                    log.error(e.getMessage());
                }


            }
        }

    }


    private void workWithNaturalPersonRepository(UUID id) {
        try {
            NaturalPersonRequest naturalPersonRequest = naturalPersonRequestRepository.getById(id);
            String sts = naturalPersonRequest.getSts();

            InfoOfFineNaturalPerson infoOfFineNaturalPerson = infoRepository.findBySts(sts);

            if (infoOfFineNaturalPerson != null) {

                ResponseWithFine response = responseRepository.findBySts(sts);

                if (response == null) {
                    response = ResponseWithFine.builder().
                            amountOfAccrual(infoOfFineNaturalPerson.getAmountOfAccrual()).
                            amountOfPaid(infoOfFineNaturalPerson.getAmountOfPaid()).
                            numberOfResolution(infoOfFineNaturalPerson.getNumberOfResolution()).
                            sts(sts).
                            dateOfResolution(infoOfFineNaturalPerson.getDateOfResolution()).
                            articleOfKoap(infoOfFineNaturalPerson.getArticleOfKoap()).
                            build();

                    responseRepository.save(response);
                }
            }
            naturalPersonRequestRepository.delete(id);
        } catch (SmvServerException e) {
            log.error(e.getMessage());
        }


    }


    private void workWithLegalPersonRepository(UUID id) {
        try {
            LegalPersonRequest legalPersonRequest = legalPersonRequestRepository.getById(id);
            String sts = legalPersonRequest.getSts();

            ResponseWithFine response = responseRepository.findBySts(sts);
            if (response == null) {

                response = response = ResponseWithFine.builder().
                        amountOfAccrual(new BigDecimal(44)).
                        amountOfPaid(new BigDecimal(44)).
                        numberOfResolution(123).
                        sts(sts).
                        dateOfResolution(new Date()).
                        articleOfKoap("21.1").
                        build();

                responseRepository.save(response);
            }
            legalPersonRequestRepository.delete(id);
        } catch (SmvServerException e) {
            log.error(e.getMessage());
        }


    }


}
