package com.cstapin.member.ui;

import com.cstapin.member.service.MemberService;
import com.cstapin.member.service.dto.MembersRequest;
import com.cstapin.member.service.dto.MembersResponse;
import com.cstapin.support.service.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/members")
@RequiredArgsConstructor
public class MemberAdminController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<PageResponse<MembersResponse>> findMembers(@Valid MembersRequest request) {
        Page<MembersResponse> response = memberService.findMembers(request);

        return ResponseEntity.ok().body(new PageResponse<>(response));
    }

}
