package com.farmdora.farmdora.user.depot.controller;

import com.farmdora.farmdoraauth.common.exception.BaseException;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotModifyRequestDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotRegisterRequestDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotSelectResponseDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.UserAddressDto;
import com.farmdora.farmdoraauth.mypage.user.depot.message.DepotMassage;
import com.farmdora.farmdoraauth.mypage.user.depot.service.DepotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/mypage/user/depot")
public class DepotRestController {

    private final DepotService depotService;

    @GetMapping("/all")
    public ResponseEntity<?> getDepotById(Principal principal) {

        Integer userId = Integer.parseInt(principal.getName());

        List<DepotSelectResponseDto> depotList = depotService.getDepotsByUserId(userId);

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, DepotMassage.DEPOT_GET_ALL_SUCCESS.getMessage(), depotList));
    }

    @GetMapping("/user/address")
    public ResponseEntity<?> getDepotAddressById(Principal principal) {

        Integer userId = Integer.parseInt(principal.getName());
        try {
            UserAddressDto userAddr = depotService.getUserAddr(userId);

            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, DepotMassage.USER_ADDRESS_GET_SUCCESS.getMessage(), userAddr));
        } catch (Exception e) {
            throw new ResourceNotFoundException("유저 정보 조회", userId);
        }
    }

    @GetMapping("/detail/{depotId}")
    public ResponseEntity<?> getDetailDepotById(@PathVariable int depotId) {
        DepotSelectResponseDto depot = depotService.getDepotById(depotId);

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, DepotMassage.DEPOT_GET_SUCCESS.getMessage(), depot));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveDepot(Principal principal, @RequestBody DepotRegisterRequestDto registerRequest) {
        try {

            Integer userId = Integer.parseInt(principal.getName());
            registerRequest.setUserId(userId);
            depotService.registerDepot(registerRequest);

            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, DepotMassage.DEPOT_REGISTER_SUCCESS.getMessage(), true));

        } catch (Exception e) {
            throw new BaseException("배송지 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifyDepot(@RequestBody DepotModifyRequestDto modifyRequest) {

        try {
            depotService.modifyDepot(modifyRequest);

            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, DepotMassage.DEPOT_MODIFY_SUCCESS.getMessage(), true));
        } catch (Exception e) {
            throw new BaseException(DepotMassage.DEPOT_MODIFY_FAILURE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{depotId}")
    public ResponseEntity<?> deleteDepot(@PathVariable int depotId) {
        try {
            depotService.deleteDepot(depotId);

            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, DepotMassage.DEPOT_DELETE_SUCCESS.getMessage(), true));

        } catch (Exception e) {

            throw new BaseException(DepotMassage.DEPOT_DELETE_FAILURE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
