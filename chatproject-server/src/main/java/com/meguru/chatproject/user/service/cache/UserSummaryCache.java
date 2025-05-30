package com.meguru.chatproject.user.service.cache;

import com.meguru.chatproject.common.constant.RedisKey;
import com.meguru.chatproject.common.service.cache.AbstractRedisStringCache;
import com.meguru.chatproject.user.domain.dto.SummaryInfoDTO;
import com.meguru.chatproject.user.domain.entity.IpDetail;
import com.meguru.chatproject.user.domain.entity.IpInfo;
import com.meguru.chatproject.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 用户所有信息的缓存
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Component
public class UserSummaryCache extends AbstractRedisStringCache<Long, SummaryInfoDTO> {
    @Autowired
    private UserInfoCache userInfoCache;

    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_SUMMARY_STRING, uid);
    }

    @Override
    protected Long getExpireSeconds() {
        return 10 * 60L;
    }

    @Override
    protected Map<Long, SummaryInfoDTO> load(List<Long> uidList) {
        Map<Long, User> userMap = userInfoCache.getBatch(uidList);
        return uidList.stream().map(uid -> {
            SummaryInfoDTO summaryInfoDTO = new SummaryInfoDTO();
            User user = userMap.get(uid);
            if (Objects.isNull(user)) {
                return null;
            }
            summaryInfoDTO.setUid(user.getId());
            summaryInfoDTO.setName(user.getName());
            summaryInfoDTO.setAvatar(user.getAvatar());
            summaryInfoDTO.setLocPlace(Optional.ofNullable(user.getIpInfo()).map(IpInfo::getUpdateIpDetail)
                    .map(IpDetail::getCity).orElse(null));
            return summaryInfoDTO;
        }).filter(Objects::nonNull).collect(Collectors.toMap(SummaryInfoDTO::getUid, Function.identity()));
    }
}
