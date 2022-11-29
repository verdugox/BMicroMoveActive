package com.MoveActive.MoveActive.repository;

import com.MoveActive.MoveActive.entity.MoveActive;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MoveActiveRepository extends ReactiveMongoRepository<MoveActive , String> {
    Mono<MoveActive> findByIdentityContract(String identityContract);
}
