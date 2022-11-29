package com.MoveActive.MoveActive.web;

import com.MoveActive.MoveActive.entity.MoveActive;
import com.MoveActive.MoveActive.service.MoveActiveService;
import com.MoveActive.MoveActive.web.mapper.MoveActiveMapper;
import com.MoveActive.MoveActive.web.model.MoveActiveModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/moveActive")
public class MoveActiveController {

    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    private MoveActiveService moveActiveService;


    @Autowired
    private MoveActiveMapper moveActiveMapper;


    @GetMapping("/findAll")
    public Mono<ResponseEntity<Flux<MoveActiveModel>>> getAll(){
        log.info("getAll executed");
        return Mono.just(ResponseEntity.ok()
                .body(moveActiveService.findAll()
                        .map(moveActive -> moveActiveMapper.entityToModel(moveActive))));
    }

    @GetMapping("/findById/{id}")
    public Mono<ResponseEntity<MoveActiveModel>> findById(@PathVariable String id){
        log.info("findById executed {}", id);
        Mono<MoveActive> response = moveActiveService.findById(id);
        return response
                .map(moveActive -> moveActiveMapper.entityToModel(moveActive))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<MoveActiveModel>> create(@Valid @RequestBody MoveActiveModel request){
        log.info("create executed {}", request);
        return moveActiveService.create(moveActiveMapper.modelToEntity(request))
                .map(moveActive -> moveActiveMapper.entityToModel(moveActive))
                .flatMap(p -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "moveActive", p.getId())))
                        .body(p)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MoveActiveModel>> updateById(@PathVariable String id, @Valid @RequestBody MoveActiveModel request){
        log.info("updateById executed {}:{}", id, request);
        return moveActiveService.update(id, moveActiveMapper.modelToEntity(request))
                .map(moveActive -> moveActiveMapper.entityToModel(moveActive))
                .flatMap(p -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "moveActive", p.getId())))
                        .body(p)))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id){
        log.info("deleteById executed {}", id);
        return moveActiveService.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
