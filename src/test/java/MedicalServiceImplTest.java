import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MedicalServiceImplTest {

        PatientInfo patientInfo = new PatientInfo("Андрей", "Осетров",
                LocalDate.of(1980, 6, 16), new HealthInfo(new BigDecimal("36.6"),
                new BloodPressure(125, 78)));
        PatientInfo info = new PatientInfo(UUID.randomUUID().toString(),
                patientInfo.getName(),
                patientInfo.getSurname(),
                patientInfo.getBirthday(),
                patientInfo.getHealthInfo());

        String message = String.format("Warning, patient with id: %s, need help", info.getId());
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService alertServiceMock = Mockito.mock(SendAlertServiceImpl.class);
        private static final Logger logger = LoggerFactory.getLogger(MedicalServiceImplTest.class);

    @Test
    void testPrintMessageCheckBloodPressure() {

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertServiceMock);

        Mockito.when(patientInfoRepository.getById(info.getId()))
            .thenReturn(info);

        medicalService.checkBloodPressure(info.getId(), new BloodPressure(100, 60));

        Mockito.verify(alertServiceMock, Mockito.times(1)).send(message);

        logger.info("Тест пройден успешно! Сообщение об отклонении давления от нормы отправлено.");
    }
    @Test
    void testPrintMessageCheckTemperature() {

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertServiceMock);

        Mockito.when(patientInfoRepository.getById(info.getId()))
                .thenReturn(info);

        medicalService.checkTemperature(info.getId(), new BigDecimal("33.5"));

        Mockito.verify(alertServiceMock, Mockito.times(1)).send(message);

        logger.info("Тест пройден успешно! Сообщение об откронении температуры от нормы отправлено.");
    }
    @Test
    void testNotPrintMessage() {

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertServiceMock);

        Mockito.when(patientInfoRepository.getById(info.getId()))
                .thenReturn(info);

        medicalService.checkTemperature(info.getId(), new BigDecimal("36.6"));
        medicalService.checkBloodPressure(info.getId(), new BloodPressure(125, 78));

        Mockito.verify(alertServiceMock, Mockito.times(0)).send(message);

        logger.info("Тест пройден успешно! При норме показателей сообщение не выводится.");
    }
}
