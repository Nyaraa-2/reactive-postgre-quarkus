package Fruit;

import io.vertx.mutiny.sqlclient.Row;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface FruitMapper {
     static final String ID = "id";
     static final String NAME = "name";
    FruitDto toDto(Fruit fruit);
    @InheritInverseConfiguration(name = "toDto")
    Fruit toEntity(FruitDto dto);
    List<FruitDto> toDtoList(List<Fruit> entities);
    void updateEntityFromDto(FruitDto dto, @MappingTarget Fruit entity);
    void updateDtoFromEntity(Fruit entity, @MappingTarget FruitDto dto);

    /**
     * Mapper
     *
     * @param resultSet result db
     * @return Fruit
     */
    default Fruit from(Row resultSet) {
        return new Fruit(resultSet.getLong(ID), resultSet.getString(NAME));
    }
}

