package com.MoveActive.MoveActive.service;

import com.MoveActive.MoveActive.entity.MoveActive;
import com.MoveActive.MoveActive.repository.MoveActiveRepository;
import com.MoveActive.MoveActive.web.mapper.MoveActiveMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MoveActiveService {

    @Autowired
    private MoveActiveRepository moveActiveRepository;

    @Autowired
    private MoveActiveMapper moveActiveMapper;

    public Flux<MoveActive> findAll(){
        log.debug("findAll executed");
        return moveActiveRepository.findAll();
    }

    public Mono<MoveActive> findById(String moveActiveId){
        log.debug("findById executed {}" , moveActiveId);
        return moveActiveRepository.findById(moveActiveId);
    }

    public Mono<MoveActive> create(MoveActive moveActive){
        log.debug("create executed {}",moveActive);
        return moveActiveRepository.save(moveActive);
    }

    public Mono<MoveActive> update(String moveActiveId, MoveActive moveActive){
        log.debug("update executed {}:{}", moveActiveId, moveActive);
        return moveActiveRepository.findById(moveActiveId)
                .flatMap(dbMoveActive -> {
                    moveActiveMapper.update(dbMoveActive, moveActive);
                    return moveActiveRepository.save(dbMoveActive);
                });
    }

    public Mono<MoveActive>delete(String moveActiveId){
        log.debug("delete executed {}",moveActiveId);
        return moveActiveRepository.findById(moveActiveId)
                .flatMap(existingMoveActive -> moveActiveRepository.delete(existingMoveActive)
                        .then(Mono.just(existingMoveActive)));
    }

}
