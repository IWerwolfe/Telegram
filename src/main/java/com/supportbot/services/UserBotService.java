package com.supportbot.services;

import com.supportbot.DTO.api.other.UserResponse;
import com.supportbot.DTO.api.typeObjects.DataResponse;
import com.supportbot.client.ApiClient;
import com.supportbot.model.user.UserBD;
import com.supportbot.model.user.UserStatus;
import com.supportbot.repositories.user.UserRepository;
import com.supportbot.services.converter.TaskDocConverter;
import com.supportbot.services.converter.UserBDConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserBotService {

    public final static String REGEX_FIO = "^(?:[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?\\s+){1,2}[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?$";
    public final static String REGEX_PHONE = "^(7|8|\\+7)9[0-9]{9,10}$";

    private final UserRepository userRepository;
    private final ApiClient api1C;
    private final PartnerService partnerService;
    private final UserBDConverter userConverter;
    private final TaskDocConverter taskDocConverter;

    private UserBD userBD;

    public UserBD getUser(User user) {
        if (this.userBD == null) {
            this.userBD = userRepository
                    .findById(user.getId())
                    .orElse(new UserBD(user));
        }
        return this.userBD;
    }

    public void subUpdateUserByAPI(User user) {
        getUser(user);
        subUpdateUserByAPI(this.userBD.getPhone());
    }

    public void subUpdateUserByAPI(UserBD user) {
        this.userBD = user;
        subUpdateUserByAPI(this.userBD.getPhone());
    }

    public void subUpdateUserByAPI(String phone) {

        UserResponse userData = api1C.getUserData(phone);
        if (!isCompleted(userData)) {
            return;
        }

        if (!userData.getStatusList().isEmpty()) {
            updateUserStatus(userData);
        }

        userConverter.updateEntity(userData, this.userBD);
        userRepository.save(this.userBD);

        if (isCompleted(userData.getPartnerListData())) {
            partnerService.createDataByPartnerDataResponse(userData.getPartnerListData());
        }

        taskDocConverter.convertToEntityList(userData.getTaskList(), true);
    }

    public UserResponse getUserInfoByAPI(String phone) {
        return api1C.getUserData(phone);
    }

    public void updateUserStatus(UserResponse userData) {

        List<UserStatus> statuses = userData.getStatusList()
                .stream()
                .filter(Objects::nonNull)
                .map(statusResponse ->
                        new UserStatus(this.userBD, partnerService.getPartnerByGuid(statusResponse.getGuid()), statusResponse.getPost()))
                .toList();
        this.userBD.setStatuses(statuses);
    }

    private static <R extends DataResponse> boolean isCompleted(R response) {
        return response != null && response.isResult();
    }
}
