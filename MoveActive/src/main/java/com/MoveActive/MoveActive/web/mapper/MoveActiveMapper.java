package com.MoveActive.MoveActive.web.mapper;

import com.MoveActive.MoveActive.entity.MoveActive;
import com.MoveActive.MoveActive.web.model.MoveActiveModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MoveActiveMapper {

    MoveActive modelToEntity(MoveActiveModel model);

    MoveActiveModel entityToModel(MoveActive event);

    @Mapping(target  = "id", ignore = true)
    void update(@MappingTarget MoveActive entity, MoveActive updateEntity);

}
