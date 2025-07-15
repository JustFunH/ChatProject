package com.meguru.chatproject.chat.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 新建群组
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupAddReq {
    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty("邀请的uid")
    private List<Long> uidList;
}
