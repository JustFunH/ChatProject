package com.meguru.chatproject.chat.service.helper;

import com.meguru.chatproject.user.domain.enums.ChatActiveStatusEnum;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.text.StrUtil;

/**
 * Description: 成员列表工具类
 *
 * @author Meguru
 * @since 2025-05-31
 */
public class ChatMemberHelper {
    private static final String SEPARATOR = "_";

    public static Pair<ChatActiveStatusEnum, String> getCursorPair(String cursor) {
        ChatActiveStatusEnum activeStatusEnum = ChatActiveStatusEnum.ONLINE;
        String timeCursor = null;
        if (StrUtil.isNotBlank(cursor)) {
            String activeStr = cursor.split(SEPARATOR)[0];
            String timeStr = cursor.split(SEPARATOR)[1];
            activeStatusEnum = ChatActiveStatusEnum.of(Integer.parseInt(activeStr));
            timeCursor = timeStr;
        }
        return Pair.of(activeStatusEnum, timeCursor);
    }

    public static String generateCursor(ChatActiveStatusEnum activeStatusEnum, String timeCursor) {
        return activeStatusEnum.getStatus() + SEPARATOR + timeCursor;
    }
}
