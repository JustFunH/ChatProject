package com.meguru.chatproject.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.user.domain.entity.Role;
import com.meguru.chatproject.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {

}
