package com.meguru.chatproject.user.domain.enums;

import com.meguru.chatproject.user.domain.vo.response.ws.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: ws前端请求类型枚举
 *
 * @author Meguru
 * @since 2025-05-28
 */
@AllArgsConstructor
@Getter
public enum WSRespTypeEnum {
    LOGIN_SUCCESS(3, "用户登录成功返回用户信息", WSLoginSuccess.class),
    MESSAGE(4, "新消息", WSMessage.class),
    ONLINE_OFFLINE_NOTIFY(5, "上下线通知", WSOnlineOfflineNotify.class),
    INVALIDATE_TOKEN(6, "使前端的token失效，意味着前端需要重新登录", null),
    BLACK(7, "拉黑用户", WSBlack.class),
    MARK(8, "消息标记", WSMsgMark.class),
    RECALL(9, "消息撤回", WSMsgRecall.class),
    APPLY(10, "好友申请", WSFriendApply.class),
    MEMBER_CHANGE(11, "成员变动", WSMemberChange.class),
    ;

    private final Integer type;
    private final String desc;
    private final Class dataClass;

    private static Map<Integer, WSRespTypeEnum> cache;

    static {
        cache = Arrays.stream(WSRespTypeEnum.values()).collect(Collectors.toMap(WSRespTypeEnum::getType, Function.identity()));
    }

    public static WSRespTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
