package by.tsuprikova.smvservice.worker;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.*;
import by.tsuprikova.smvservice.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Component
@AllArgsConstructor
@Slf4j
public class Worker implements Runnable {

    private NaturalPersonRequestRepository naturalPersonRequestRepository;
    private NaturalPersonInfoRepository infoRepository;
    private NaturalPersonResponseRepository naturalPersonResponseRepository;
    private LegalPersonRequestRepository legalPersonRequestRepository;
    private LegalPersonResponseRepository legalPersonResponseRepository;


    @Override
    public void run() {

        //log.info("Worker is working....");
        while (true) {
            try {
                UUID naturalPersonRequestId = naturalPersonRequestRepository.findRandomId();
                UUID legalPersonRequestId = legalPersonRequestRepository.findRandomId();

                while (naturalPersonRequestId == null && legalPersonRequestId == null) {
                    try {
                        Thread.sleep(300);
                        naturalPersonRequestId = naturalPersonRequestRepository.findRandomId();
                        legalPersonRequestId = legalPersonRequestRepository.findRandomId();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (naturalPersonRequestId != null) {
                    workWithNaturalPersonRepositories(naturalPersonRequestId);
                }
                if (legalPersonRequestId != null) {
                    workWithLegalPersonRepositories(legalPersonRequestId);
                }

            } catch (SmvServiceException e) {
                log.error(e.getMessage());
            }

        }

    }


    private void workWithNaturalPersonRepositories(UUID id) {
        try {
            NaturalPersonRequest naturalPersonRequest = naturalPersonRequestRepository.getById(id);
            String sts = naturalPersonRequest.getSts();

            InfoOfFineNaturalPerson infoOfFineNaturalPerson = infoRepository.findBySts(sts);

            if (infoOfFineNaturalPerson != null) {

                NaturalPersonResponse response = naturalPersonResponseRepository.findBySts(sts);

                if (response == null) {
                    response = NaturalPersonResponse.responseBuilder().
                            amountOfAccrual(infoOfFineNaturalPerson.getAmountOfAccrual()).
                            amountOfPaid(infoOfFineNaturalPerson.getAmountOfPaid()).
                            numberOfResolution(infoOfFineNaturalPerson.getNumberOfResolution()).
                            sts(sts).
                            dateOfResolution(infoOfFineNaturalPerson.getDateOfResolution()).
                            articleOfKoap(infoOfFineNaturalPerson.getArticleOfKoap()).
                            build();

                    naturalPersonResponseRepository.save(response);
                }
            }
            naturalPersonRequestRepository.delete(id);
        } catch (SmvServiceException e) {
            log.error(e.getMessage());
        }


    }


    private void workWithLegalPersonRepositories(UUID id) {
        try {
            LegalPersonRequest legalPersonRequest = legalPersonRequestRepository.getById(id);
            Long INN = legalPersonRequest.getInn();

            LegalPersonResponse response = legalPersonResponseRepository.findByINN(INN);
            if (response == null) {

                response = LegalPersonResponse.LegalResponseBuilder().
                        amountOfAccrual(new BigDecimal(44)).
                        amountOfPaid(new BigDecimal(44)).
                        numberOfResolution(123).
                        inn(INN).
                        dateOfResolution(new Date()).
                        articleOfKoap("21.1").
                        build();

                legalPersonResponseRepository.save(response);
            }
            legalPersonRequestRepository.delete(id);
        } catch (SmvServiceException e) {
            log.error(e.getMessage());
        }


    }


}
