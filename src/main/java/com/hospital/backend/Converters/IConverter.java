package com.hospital.backend.Converters;

import java.util.List;

public interface IConverter<Model,Dto> {
    Model convertDtoToModel(Dto dto);
    Dto convertModelToDto(Model model);

    List<Dto> convertModelListToDtoList(List<Model> modelList);
}