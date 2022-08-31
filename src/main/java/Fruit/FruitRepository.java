package Fruit;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import lombok.AllArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import static Fruit.FruitQueries.*;

/**
 * FruitRepository
 */
@ApplicationScoped
@AllArgsConstructor
public class FruitRepository {
    private final FruitMapper fruitMapper;
    /**
     * The Reactive Pg Client is derived from Vertx Reactive Pg Client,
     * with the help of Quarkus, you can configure database connection in the application.properties directly,
     * and inject the reactive Postgres Client PgPool bean in your codes.
     */
    @Inject
    PgPool client;

    /**
     * PreparedQuery select all fruits
     * @return Multi Fruit entity
     */
    public Uni<List<Fruit>> findAll() {
        return client.preparedQuery(SELECT_ALL_ORDER_BY_NAME_ASC).execute()
                .map(rows -> {
                    List<Fruit> fruits = new ArrayList<>(rows.size());
                    for (Row row : rows) {
                        fruits.add(fruitMapper.from(row));
                    }
                    return fruits;
                });
    }

    /**
     * PreparedQuery find Fruit by id
     * @param idFruit id
     * @return Uni Fruit Entity
     */
    public Uni<Fruit> findById(Long idFruit) {
        //Get an Iterator for the RowSet result.
        return client.preparedQuery(SELECT_BY_ID).execute(Tuple.of(idFruit))
                //Return true if the iteration has more than one element
                .map(RowSet::iterator)
                //Create a Fruit instance from the Row if an entity was found.
                .map(iterator -> iterator.hasNext() ? fruitMapper.from(iterator.next()) : null);
    }

    /**
     * PreparedQuery create a fruit
     *
     * @param fruit fruit entity
     * @return Uni Fruit entity
     */
    public Uni<Fruit> save(Fruit fruit) {
        return client.preparedQuery(INSERT_RETURNING_ALL).execute(Tuple.of(fruit.getName()))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? fruitMapper.from(iterator.next()) : null);
    }

    /**
     * PreparedQuery update a fruit
     *
     * @return Uni Fruit
     */
    public Uni<Fruit> update(Fruit fruit) {
        return client.preparedQuery(UPDATE).execute(Tuple.of(fruit.getName(), fruit.getId()))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? fruitMapper.from(iterator.next()) : null);
    }

    /**
     * PreparedQuery delete a fruit
     *
     * @param id idFruit
     * @return Uni Boolean
     */
    public Uni<Boolean> delete(Long id) {
        return client.preparedQuery(DELETE_BY_ID).execute(Tuple.of(id))
                //Inspect metadata to determine if a fruit has been actually deleted.
                .map(pgRowSet -> pgRowSet.rowCount() == 1);
    }
}
