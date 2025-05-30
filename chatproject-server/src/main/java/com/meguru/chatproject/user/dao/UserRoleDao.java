package com.meguru.chatproject.user.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.user.domain.entity.UserRole;
import com.meguru.chatproject.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> {
    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getId, Objects.requireNonNull(uid))
                .list();
    }
}
