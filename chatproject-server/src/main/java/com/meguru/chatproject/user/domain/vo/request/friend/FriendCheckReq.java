package com.meguru.chatproject.user.domain.vo.request.friend;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Description: 好友校验
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendCheckReq {

    @NotEmpty
    @Size(max = 50)
    @ApiModelProperty("校验好友的uid")
    private List<Long> uidList;

}
