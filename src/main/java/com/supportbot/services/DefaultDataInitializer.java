package com.supportbot.services;

import com.supportbot.DTO.api.other.DefaultDataResponse;
import com.supportbot.DTO.types.TaskType;
import com.supportbot.client.ApiClient;
import com.supportbot.model.DefaultDocParam;
import com.supportbot.model.reference.*;
import com.supportbot.model.reference.legalentity.Company;
import com.supportbot.model.reference.legalentity.Division;
import com.supportbot.model.types.Entity;
import com.supportbot.repositories.*;
import com.supportbot.repositories.command.CompanyRepository;
import com.supportbot.repositories.reference.DivisionRepository;
import com.supportbot.repositories.reference.PayTerminalRepository;
import com.supportbot.repositories.reference.TaskStatusRepository;
import com.supportbot.repositories.reference.TaskTypeRepository;
import com.supportbot.services.converter.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultDataInitializer implements CommandLineRunner {

    private final ApiClient api1C;

    private final DefaultDocParamRepository defaultDocParamRepository;

    private final TaskTypeRepository typeRepository;
    private final TaskStatusRepository statusRepository;
    private final CompanyRepository companyRepository;
    private final DivisionRepository divisionRepository;
    private final PayTerminalRepository payTerminalRepository;
    private final CashDeskKkmRepository cashDeskKkmRepository;
    private final CashDeskRepository cashDeskRepository;
    private final BankAccountRepository bankAccountRepository;

    private final TaskStatusConverter statusConverter;
    private final TaskTypeConverter typeConverter;
    private final ManagerConverter managerConverter;
    private final CompanyConverter companyConverter;
    private final DivisionConverter divisionConverter;
    private final BankConverter bankConverter;
    private final BankAccountConverter bankAccountConverter;
    private final PayTerminalConverter payTerminalConverter;
    private final CashDeskKkmConverter cashDeskKkmConverter;
    private final CashDeskConverter cashDeskConverter;

    private final DefaultDocParam defaultDocParam = new DefaultDocParam();

    private final String DEFAULT_NAME_CLOSED = "Завершена";
    private final String DEFAULT_NAME_INIT = "Ожидает обработки";
    private final String DEFAULT_NAME_TYPE = "Обращение через чат бот";

    @Override
    public void run(String... args) throws Exception {

        DefaultDataResponse data = null;
        try {
            data = api1C.getDefaultData();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (data == null || !data.isResult()) {
            initDefault();
        } else {
            convertData(data);
        }
    }

    private void convertData(DefaultDataResponse data) {
        statusConverter.convertToEntityList(data.getTaskStatuses(), true);
        typeConverter.convertToEntityList(data.getTaskTypes(), true);
        managerConverter.convertToEntityList(data.getManagers(), true);
        companyConverter.convertToEntityList(data.getCompanies(), true);
        divisionConverter.convertToEntityList(data.getDivisions(), true);
        bankConverter.convertToEntityList(data.getBanks(), true);
        bankAccountConverter.convertToEntityList(data.getBankAccounts(), true);
        payTerminalConverter.convertToEntityList(data.getPayTerminals(), true);
        cashDeskKkmConverter.convertToEntityList(data.getCashDeskKkms(), true);
        cashDeskConverter.convertToEntityList(data.getCashDesks(), true);
        updateDefaultInfo(data);
        log.info("Loading default data success");
    }

    private void initDefault() {
        TaskType taskType = getDefaultType();
        TaskStatus taskInitStatus = getDefaultByName(DEFAULT_NAME_INIT);
        TaskStatus taskClosedStatus = getDefaultByName(DEFAULT_NAME_CLOSED);

        defaultDocParam.setTaskDocType(taskType);
        defaultDocParam.setInitTaskDocStatus(taskInitStatus);
        defaultDocParam.setClosedTaskDocStatus(taskClosedStatus);
        TaskStatus.setInitialStatus(taskInitStatus);
        TaskStatus.setClosedStatus(taskClosedStatus);
        log.info("Creating new default data success");
    }

    private void updateDefaultInfo(DefaultDataResponse data) {

        defaultDocParamRepository.deleteAll();

        Optional<TaskType> typeOptional = typeRepository.findByNameIgnoreCase(data.getNameDefaultType());
        TaskType taskType = typeOptional.orElseGet(this::getDefaultType);

        TaskStatus taskInitStatus = getDefaultEntity(statusRepository, data.getGuidDefaultInitialStatus(), getDefaultByName(DEFAULT_NAME_INIT));
        TaskStatus taskClosedStatus = getDefaultEntity(statusRepository, data.getGuidDefaultClosedStatus(), getDefaultByName(DEFAULT_NAME_CLOSED));
        Company company = getDefaultEntity(companyRepository, data.getGuidDefaultCompany());
        BankAccount bankAccount = getDefaultEntity(bankAccountRepository, data.getGuidDefaultBankAccount());
        CashDesk cashDesk = getDefaultEntity(cashDeskRepository, data.getGuidDefaultCashDesk());
        CashDeskKkm cashDeskKkm = getDefaultEntity(cashDeskKkmRepository, data.getGuidDefaultCashDeskKkm());
        PayTerminal payTerminal = getDefaultEntity(payTerminalRepository, data.getGuidDefaultPaymentTerminal());
        Division division = getDefaultEntity(divisionRepository, data.getGuidDefaultDivisions());

        defaultDocParam.setClosedTaskDocStatus(taskClosedStatus);
        defaultDocParam.setInitTaskDocStatus(taskInitStatus);
        defaultDocParam.setTaskDocType(taskType);
        defaultDocParam.setCompany(company);
        defaultDocParam.setBankAccount(bankAccount);
        defaultDocParam.setCashDesk(cashDesk);
        defaultDocParam.setCashDeskKkm(cashDeskKkm);
        defaultDocParam.setPayTerminal(payTerminal);
        defaultDocParam.setDivision(division);

        TaskStatus.setInitialStatus(taskInitStatus);
        TaskStatus.setClosedStatus(taskClosedStatus);

        defaultDocParamRepository.save(defaultDocParam);
    }

    private <E extends Entity,
            R extends EntityRepository<E>> E getDefaultEntity(R repository, String guid, E defaultEntity) {
        Optional<E> optional = repository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return optional.orElseGet(() -> defaultEntity);
    }

    private <E extends Entity,
            R extends EntityRepository<E>> E getDefaultEntity(R repository, String guid) {
        return getDefaultEntity(repository, guid, null);
    }

    private TaskStatus getDefaultByName(String name) {
        Optional<TaskStatus> optional = statusRepository.findByNameIgnoreCase(name);
        return optional.orElseGet(() -> statusRepository.save(new TaskStatus(null, name)));
    }

    private TaskType getDefaultType() {
        Optional<TaskType> optional = typeRepository.findByNameIgnoreCase(DEFAULT_NAME_TYPE);
        return optional.orElseGet(() -> typeRepository.save(new TaskType(null, DEFAULT_NAME_TYPE)));
    }
}
