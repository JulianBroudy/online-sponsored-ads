package com.mabaya.ads.service.mapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This interface defines the mapping operations between a model entity and its corresponding Data
 * Transfer Object (DTO).
 *
 * <p>It provides methods to convert from a model to a DTO and vice versa, both for individual
 * objects and collections of objects.
 *
 * @param <MODEL> the type parameter representing the model entity
 * @param <DTO> the type parameter representing the Data Transfer Object
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
public interface IMapper<MODEL, DTO> {

  /**
   * Maps a given model entity to its corresponding DTO.
   *
   * @param model the model entity to be mapped
   * @return the mapped DTO
   */
  DTO mapToDTO(MODEL model);

  /**
   * Maps a collection of model entities to a set of corresponding DTOs. This default method
   * iterates over the collection of models, mapping each to its DTO. It ensures that the resulting
   * collection contains unique DTO instances.
   *
   * @param models the collection of model entities to be mapped
   * @return a set of mapped DTOs
   */
  default Collection<DTO> mapToDTO(Collection<MODEL> models) {
    final Set<DTO> dtos = new HashSet<>();
    for (MODEL model : models) {
      dtos.add(mapToDTO(model));
    }
    return dtos;
  }

  /**
   * Maps a given DTO to its corresponding model entity.
   *
   * @param dto the DTO to be mapped
   * @return the mapped model entity
   */
  MODEL mapToModel(DTO dto);

  /**
   * Maps a collection of DTOs to a set of corresponding model entities. This default method
   * iterates over the collection of DTOs, mapping each to its model entity. It ensures that the
   * resulting collection contains unique model entity instances.
   *
   * @param dtos the collection of DTOs to be mapped
   * @return a set of mapped model entities
   */
  default Collection<MODEL> mapToModel(Collection<DTO> dtos) {
    final Set<MODEL> models = new HashSet<>();
    for (DTO dto : dtos) {
      models.add(mapToModel(dto));
    }
    return models;
  }
}
