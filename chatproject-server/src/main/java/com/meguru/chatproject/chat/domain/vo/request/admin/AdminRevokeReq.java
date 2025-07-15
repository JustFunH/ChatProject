package com.meguru.chatproject.chat.domain.vo.request.admin;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Description: 撤销管理
 * @author Meguru
 * @since 2025-05-31
 */
@Data
public class AdminRevokeReq {
    @NotNull
    @ApiModelProperty("房间号")
    private Long roomId;

    @NotNull
    @Size(min = 1, max = 3)
    @ApiModelProperty("需要撤销管理的列表")
    private List<Long> uidList;
}
