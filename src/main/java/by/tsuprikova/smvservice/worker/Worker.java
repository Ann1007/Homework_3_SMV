package by.tsuprikova.smvservice.worker;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
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
public class Worker extends Thread {

    private NaturalPersonRequestRepository naturalPersonRequestRepository;
    private NaturalPersonInfoRepository infoRepository;
    private NaturalPersonResponseRepository naturalPersonResponseRepository;
    private LegalPersonRequestRepository legalPersonRequestRepository;
    private LegalPersonResponseRepository legalPersonResponseRepository;


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
                            Thread.sleep(300);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        firstNaturalPersonRequestId = naturalPersonRequestRepository.findFirstId();
                        firstLegalPersonRequestId = legalPersonRequestRepository.findFirstId();
                    }

                    if (firstNaturalPersonRequestId != null) {
                        workWithNaturalPersonRepositories(firstNaturalPersonRequestId);
                    }
                    if (firstLegalPersonRequestId != null) {
                        workWithLegalPersonRepositories(firstLegalPersonRequestId);
                    }

                } catch (SmvServerException e) {
                    log.error(e.getMessage());
                }


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
        } catch (SmvServerException e) {
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
        } catch (SmvServerException e) {
            log.error(e.getMessage());
        }


    }


}
