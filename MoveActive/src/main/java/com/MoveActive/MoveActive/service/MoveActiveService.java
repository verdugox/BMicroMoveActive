package com.MoveActive.MoveActive.service;

import com.MoveActive.MoveActive.entity.MoveActive;
import com.MoveActive.MoveActive.entity.ProductActive;
import com.MoveActive.MoveActive.repository.MoveActiveRepository;
import com.MoveActive.MoveActive.web.mapper.MoveActiveMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MoveActiveService {

    @Autowired
    private MoveActiveRepository moveActiveRepository;

    @Autowired
    private MoveActiveMapper moveActiveMapper;

    private final String BASE_URL = "http://localhost:8085";

    TcpClient tcpClient = TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .doOnConnected(connection ->
                    connection.addHandlerLast(new ReadTimeoutHandler(3))
                            .addHandlerLast(new WriteTimeoutHandler(3)));
    private final WebClient client = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))  // timeout
            .build();

    public Mono<ProductActive> findByIdentityContract(String identityContract){
        return this.client.get().uri("/v1/productActive/findByIdentityContract/{identityContract}",identityContract)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(ProductActive.class);
    }

    public Mono<ProductActive> updateProductActiveById(ProductActive productActive){
        return this.client.put().uri("/v1/productActive/{id}",productActive.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(productActive), ProductActive.class)
                .retrieve()
                .bodyToMono(ProductActive.class);
    }


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

        return findByIdentityContract(moveActive.getIdentityContract())
                .flatMap(productActive1 -> {
                    if(moveActive.getOperationType().equals("PAGO")){
                        productActive1.setAvailableAmount(productActive1.getAvailableAmount() + moveActive.getAmount());
                    }
                    else{
                        if(productActive1.getAvailableAmount()>= moveActive.getAmount() && productActive1.getLimitAmount()<= moveActive.getAmount()){
                            productActive1.setAvailableAmount(productActive1.getAvailableAmount() - moveActive.getAmount());
                        }else{
                            return Mono.error(new Exception("No puede realizar un consumo, ya que el monto del producto activo disponible es menor al monto que desea consumir - " + productActive1.getAvailableAmount() + " y el limite de credito permitido es:  - " +productActive1.getLimitAmount()));
                        }
                    }
                    log.debug("productActive" +productActive1.getId());
                    updateProductActiveById(productActive1).subscribe();
                    return moveActiveRepository.save(moveActive);
                });


        //return moveActiveRepository.save(moveActive);
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
