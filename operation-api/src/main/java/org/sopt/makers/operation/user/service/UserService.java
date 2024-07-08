package org.sopt.makers.operation.user.service;

import java.util.List;

import org.sopt.makers.operation.user.dto.response.UserInfoResponse;
import org.sopt.makers.operation.user.dto.response.UserInfosResponse;

public interface UserService {
    UserInfoResponse getUserInfo(Long userId);
    UserInfosResponse getUserInfos(List<Long> userIds);
}
