package by.tsuprikova.SMVService.worker;

import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.model.InfoOfFineNaturalPerson;
import by.tsuprikova.SMVService.repositories.NaturalPersonInfoRepository;
import by.tsuprikova.SMVService.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.SMVService.repositories.NaturalPersonResponseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Slf4j
public class Worker extends Thread {

    private NaturalPersonRequestRepository naturalPersonRequestRepository;
    private NaturalPersonInfoRepository infoRepository;
    private NaturalPersonResponseRepository naturalPersonResponseRepository;


    @Override
    public void run() {

        while (true) {

            log.info("Worker is working....");
            while (true) {
                Integer minRequestId = naturalPersonRequestRepository.findMinId();

                while (minRequestId == null) {
                    try {
                        Thread.sleep(3000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    minRequestId = naturalPersonRequestRepository.findMinId();

                }

                NaturalPersonRequest naturalPersonRequest = naturalPersonRequestRepository.getById(minRequestId);
                String sts = naturalPersonRequest.getSts();

                InfoOfFineNaturalPerson infoOfFineNaturalPerson = infoRepository.findBySts(sts);

                if (infoOfFineNaturalPerson != null) {

                    ResponseWithFine response = naturalPersonResponseRepository.findBySts(sts);

                    if (response == null) {
                        response = new ResponseWithFine(0, infoOfFineNaturalPerson.getAmountOfAccrual(),
                                infoOfFineNaturalPerson.getAmountOfPaid(), infoOfFineNaturalPerson.getNumberOfResolution(),
                                sts, infoOfFineNaturalPerson.getDateOfResolution(), infoOfFineNaturalPerson.getArticleOfKOAP());
                        naturalPersonResponseRepository.save(response);
                    }
                }
                naturalPersonRequestRepository.delete(naturalPersonRequest);

            }
        }

    }
}
