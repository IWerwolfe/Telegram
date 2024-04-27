package com.supportbot.services;

import com.supportbot.DTO.api.balance.BalanceResponse;
import com.supportbot.DTO.api.other.SyncDataResponse;
import com.supportbot.DTO.api.reference.legal.partner.PartnerDataResponse;
import com.supportbot.DTO.api.reference.legal.partner.PartnerListData;
import com.supportbot.DTO.api.typeObjects.DataResponse;
import com.supportbot.DTO.dadata.DaDataParty;
import com.supportbot.DTO.types.PartnerType;
import com.supportbot.client.ApiClient;
import com.supportbot.client.DaDataClient;
import com.supportbot.model.balance.PartnerBalance;
import com.supportbot.model.documents.docdata.PartnerData;
import com.supportbot.model.reference.legalentity.Contract;
import com.supportbot.model.reference.legalentity.Department;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.model.user.UserBD;
import com.supportbot.model.user.UserStatus;
import com.supportbot.repositories.reference.ContractRepository;
import com.supportbot.repositories.reference.DepartmentRepository;
import com.supportbot.repositories.reference.PartnerRepository;
import com.supportbot.services.converter.ContractConverter;
import com.supportbot.services.converter.Converter;
import com.supportbot.services.converter.DepartmentConverter;
import com.supportbot.services.converter.PartnerConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartnerService {

    public final static String REGEX_INN = "^[0-9]{10}|[0-9]{12}$";
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

    private final PartnerRepository partnerRepository;
    private final ContractRepository contractRepository;
    private final DaDataClient daDataClient;
    private final ApiClient api1C;
    private final PartnerConverter partnerConverter;
    private final DepartmentConverter departmentConverter;
    private final DepartmentRepository departmentRepository;
    private final ContractConverter contractConverter;
    private final BalanceService balanceService;

    public Partner getPartnerByGuid(String guid) {

        Optional<Partner> optional = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        if (optional.isPresent()) {
            return optional.get();
        }

        PartnerDataResponse dataResponse = api1C.getPartnerByGuid(guid);

        if (isCompleted(dataResponse)) {
            PartnerListData data = createDataByPartnerDataResponse(dataResponse);
            return data.getPartners().isEmpty() ? null : data.getPartners().get(0);
        }

        return null;
    }

    public @NonNull PartnerData getPartnerDateByAPI(String inn) {
        if (!inn.matches(REGEX_INN)) {
            return new PartnerData();
        }
        PartnerDataResponse response = api1C.getPartnerData(inn);
        Partner partner = isCompleted(response) ?
                createDataByPartnerDataResponse(response).getPartners().get(0) :
                createLegalByInnFromDaData(inn);
        return new PartnerData(partner);
    }

    public PartnerListData createDataByPartnerDataResponse(PartnerDataResponse dataResponse) {
        List<Partner> partners = Converter.convertToEntityListIsSave(dataResponse.getPartners(), partnerConverter, partnerRepository);
        List<Department> departments = Converter.convertToEntityListIsSave(dataResponse.getDepartments(), departmentConverter, departmentRepository);
        List<Contract> contracts = Converter.convertToEntityListIsSave(dataResponse.getContracts(), contractConverter, contractRepository);
        List<PartnerBalance> balances = updateBalance(dataResponse.getBalance());

        return new PartnerListData(partners, departments, contracts, balances);
    }

    public Partner createLegalByInnFromDaData(String inn) {
        DaDataParty data = daDataClient.getCompanyDataByINN(inn);
        Partner partner = new Partner();
        partner.setInn(inn);
        partner.setComment("created automatically on " + LocalDateTime.now().format(formatter));
        partner.setPartnerType(PartnerType.BUYER);

        if (data != null) {
            partner.setName(data.getName().getShortWithOpf());
            partner.setKpp(data.getKpp());
            partner.setOGRN(data.getOgrn());
            partner.setCommencement(Converter.convertLongToLocalDateTime(data.getState().getRegistrationDate()));
            partner.setDateCertificate(Converter.convertLongToLocalDateTime(data.getOgrnDate()));
            partner.setOKPO(data.getOkpo());
        }

        Contract contract = new Contract(partner);
        partner.setDefaultContract(contract);

        SyncDataResponse createResponse = api1C.createPartner(partnerConverter.convertToResponse(partner));
        partner.setSyncData(createResponse);

        return partnerRepository.save(partner);
    }

    public List<UserStatus> getUserStatusByPartner(Partner partner, UserBD user) {
        return user.getStatuses()
                .stream()
                .filter(p -> p.getLegal() == partner)
                .toList();
    }

    public List<Partner> getPartnerByUserStatus(UserBD user) {
        return user.getStatuses()
                .stream()
                .map(status -> (Partner) status.getLegal())
                .filter(Objects::nonNull)
                .toList();
    }

    private List<PartnerBalance> updateBalance(List<BalanceResponse> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(balanceService::updateLegalBalance)
                .collect(Collectors.toList());
    }

    private static <R extends DataResponse> boolean isCompleted(R response) {
        return response != null && response.isResult();
    }

}
