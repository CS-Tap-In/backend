package com.cstapin.member.service.dto;

import com.cstapin.member.persistence.MemberEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MembersResponse {

    private final Long id;
    private final String username;
    private final String nickname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public MembersResponse(Long id, String username, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static MembersResponse from(MemberEntity memberEntity) {
        return new MembersResponse(memberEntity.getId(), memberEntity.getUsername(), memberEntity.getNickname(), memberEntity.getCreatedAt());
    }
}
