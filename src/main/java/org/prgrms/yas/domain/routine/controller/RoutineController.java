package org.prgrms.yas.domain.routine.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineCreateResponse;
import org.prgrms.yas.domain.routine.dto.RoutineDeleteResponse;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateResponse;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.routine.service.RoutineService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/routines")
@Slf4j
@RequiredArgsConstructor
public class RoutineController {

  private final RoutineService routineService;

//  @PostMapping
//  public ResponseEntity<RoutineCreateResponse> create(
//      @Valid @RequestBody RoutineCreateRequest routineCreateRequest,
//      @AuthenticationPrincipal
//          Token token // 요런식으로 들어오겠죠, 저 Token은 원래 있는건 아니고 말들어야해요 JWT~~이런식으로 만들고 그거쓰는거예요
//
//      // 넹 토큰에 해당하는 객체 이름이랑 구조만 알려달라해서 여기서 그거 사용하는걸로 일단 만들고 있다가 토큰 완성돼서 머지되면 그때 그거로 사용하면 돼요
//  ) {
//   RoutineCreateResponse routineCreateResponse = routineService.routineSave(id,routineCreateRequest);
//    return ResponseEntity.ok(routineCreateResponse);
//  }

  @DeleteMapping("/{id}")
  public ResponseEntity<RoutineDeleteResponse> delete(@PathVariable("id") Long id )
      throws NotFoundException {
    RoutineDeleteResponse routineDeleteResponse = routineService.deleteRoutine(id);
    return ResponseEntity.ok(routineDeleteResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RoutineUpdateResponse> update(@PathVariable("id") Long id,
      @Valid @RequestBody RoutineUpdateRequest routineUpdateRequest) throws NotFoundException {
    RoutineUpdateResponse routineUpdateResponse = routineService.updateRoutine(id, routineUpdateRequest);
    return ResponseEntity.ok(routineUpdateResponse);
  }
}
