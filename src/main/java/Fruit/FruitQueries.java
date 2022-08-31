package Fruit;

/**
 * Class utils queries SQL
 */
public class FruitQueries {
    public static final String SELECT_BY_ID = "SELECT id, name FROM fruit WHERE id = $1";
    public static final String SELECT_ALL_ORDER_BY_NAME_ASC = "SELECT id, name FROM fruit ORDER BY name ASC";
    public static final String INSERT_RETURNING_ALL ="INSERT INTO fruit (name) VALUES ($1) RETURNING *";
    public static final String DELETE_BY_ID = "DELETE FROM fruit WHERE id = $1";
    public static final String UPDATE = "UPDATE fruit SET name = $1 WHERE id = $2 RETURNING *";
}
