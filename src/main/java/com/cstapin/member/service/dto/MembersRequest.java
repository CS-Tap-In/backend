package com.cstapin.member.service.dto;

import com.cstapin.support.service.dto.PageRequest;
import lombok.Getter;

@Getter
public class MembersRequest extends PageRequest {

    private String username;
}
