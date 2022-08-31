package Fruit;

import Exception.CustomServiceException;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * Fruit service
 *
 * @param fruitMapper     fruit mapper
 * @param fruitRepository fruit repository
 */
@ApplicationScoped
public record FruitService(FruitMapper fruitMapper, FruitRepository fruitRepository) {

    /**
     * Method service get all fruit
     *
     * @return Multi FruitDto
     * @throws SQLException Database access error
     */
    public Uni<List<FruitDto>> getAllFruit() {
        return fruitRepository.findAll()
                .ifNoItem().after(Duration.ofMillis(500)).fail()
                .onFailure().transform(throwable -> new CustomServiceException(new SQLException(CustomServiceException.ErrorMessage.ERROR_BDD)))
                .onItem().transform(fruitMapper::toDtoList);
    }

    /**
     * Method service get fruit by id
     *
     * @param idFruit id fruit
     * @return Uni FruitDto
     * @throws SQLException      Database access error
     * @throws NotFoundException If fruit doesn't exist
     */
    public Uni<FruitDto> findById(Long idFruit) {
        return fruitRepository.findById(idFruit)
                .ifNoItem().after(Duration.ofMillis(500)).fail()
                .onFailure().transform(throwable -> new CustomServiceException(new SQLException(CustomServiceException.ErrorMessage.ERROR_BDD)))
                .flatMap(fruit -> fruit != null ? Uni.createFrom().item(fruitMapper.toDto(fruit)) :
                        Uni.createFrom().failure(() -> new CustomServiceException(new NotFoundException(CustomServiceException.ErrorMessage.fruitNotExist(idFruit)))));
    }

    /**
     * Method service create a fruit
     *
     * @param dto Fruit dto
     * @return Fruit dto
     * @throws SQLException        Database access error
     * @throws BadRequestException Error post fruit
     */
    public Uni<FruitDto> save(@Valid FruitDto dto) {
        return fruitRepository.save(fruitMapper.toEntity(dto))
                .ifNoItem().after(Duration.ofMillis(500)).fail()
                .onFailure().transform(throwable -> new CustomServiceException(new SQLException(CustomServiceException.ErrorMessage.ERROR_BDD)))
                .flatMap(fruit -> fruit != null ? Uni.createFrom().item(fruitMapper.toDto(fruit)) :
                        Uni.createFrom().failure(() -> new CustomServiceException(new BadRequestException(CustomServiceException.ErrorMessage.ERROR_CREATE_FRUIT + dto.getName()))));
    }

    /**
     * Method service update a fruit
     *
     * @param dto Fruit dto
     * @param id  FruitID
     * @return Uni FruitDto
     */
    public Uni<FruitDto> update(@Valid FruitDto dto, Long id) {
        isBodyValid(dto, id);
        return fruitRepository.update(fruitMapper.toEntity(dto))
                .ifNoItem().after(Duration.ofMillis(500)).fail()
                .onFailure().transform(throwable -> new CustomServiceException(new SQLException(CustomServiceException.ErrorMessage.ERROR_BDD)))
                .flatMap(fruit -> fruit != null ? Uni.createFrom().item(fruitMapper.toDto(fruit)) :
                        Uni.createFrom().failure(() -> new CustomServiceException(new NotFoundException(CustomServiceException.ErrorMessage.ERROR_UPDATE_FRUIT + dto.getId()))));
    }

    /**
     * Method service delete fruit
     *
     * @param id FruitID
     * @return boolean : true when fruit selected is deleted
     */
    public Uni<Boolean> delete(Long id) {
        return fruitRepository.delete(id)
                .ifNoItem().after(Duration.ofMillis(500)).fail()
                .onFailure().transform(throwable -> new CustomServiceException(new SQLException(CustomServiceException.ErrorMessage.ERROR_BDD)))
                .flatMap(deleted -> deleted ? Uni.createFrom().item(true) :
                        Uni.createFrom().failure(() -> new CustomServiceException(new NotFoundException(CustomServiceException.ErrorMessage.DELETE_FRUIT_NOT_FOUND))));
    }


    /**
     * Checks the body sent by the user
     *
     * @param dto FruitDto
     * @param id  iDFruit
     * @throws BadRequestException if id is empty or if id in path variable not equals to the fruit id
     */
    private void isBodyValid(FruitDto dto, Long id) {
        if (Objects.isNull(dto.getId()))
            throw new CustomServiceException(new BadRequestException(CustomServiceException.ErrorMessage.NO_ID_UPDATE_FRUIT));
        if (!Objects.equals(id, dto.getId()))
            throw new CustomServiceException(new BadRequestException(CustomServiceException.ErrorMessage.ERROR_PATH_ID));
    }
}
