package com.meguru.chatproject.user.service;

import com.meguru.chatproject.user.domain.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户角色关系表 服务类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
public interface IUserRoleService extends IService<UserRole> {
    List<UserRole> listByUid(Long uid);
}
