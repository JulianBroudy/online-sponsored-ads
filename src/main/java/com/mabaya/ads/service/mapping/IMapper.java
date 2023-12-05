package com.mabaya.ads.service.mapping;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Defines mapping operations between model entities and their corresponding Data Transfer Objects
 * (DTOs). Allows for bidirectional conversions between model and DTO, applicable to both individual
 * objects and collections.
 *
 * <p>Implementing classes will provide specific mapping logic for different entity-DTO pairs.
 *
 * @param <MODEL> Type parameter representing the model entity.
 * @param <DTO> Type parameter representing the Data Transfer Object.
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@SuppressWarnings("unused")
public interface IMapper<MODEL, DTO> {

  /**
   * Converts a model entity to its corresponding DTO.
   *
   * @param model The model entity to be mapped.
   * @return The mapped DTO.
   */
  DTO mapToDTO(MODEL model);

  /**
   * Converts a collection of model entities to their corresponding DTOs. Ensures uniqueness within
   * the resulting DTO set.
   *
   * @param models The collection of model entities to be mapped.
   * @return A set of mapped DTOs.
   */
  default Collection<DTO> mapToDTO(Collection<MODEL> models) {
    return models.stream().map(this::mapToDTO).collect(Collectors.toSet());
  }

  /**
   * Converts a DTO to its corresponding model entity.
   *
   * @param dto The DTO to be mapped.
   * @return The mapped model entity.
   */
  MODEL mapToModel(DTO dto);

  /**
   * Converts a collection of DTOs to their corresponding model entities. Ensures uniqueness within
   * the resulting model entity set.
   *
   * @param dtos The collection of DTOs to be mapped.
   * @return A set of mapped model entities.
   */
  default Collection<MODEL> mapToModel(Collection<DTO> dtos) {
    return dtos.stream().map(this::mapToModel).collect(Collectors.toSet());
  }
}
