package Exception;

public class CustomServiceException extends RuntimeException{
    public CustomServiceException(Throwable cause) {
        super(cause);
    }
    public static class ErrorMessage {
        public ErrorMessage() {
        }

        public static final String ERROR_BDD = "Database access error";
        public static final String ERROR_CREATE_FRUIT = "Error encountered when creating the fruit ";
        public static final String ERROR_UPDATE_FRUIT = "This fruit does not exist, ID : ";
        public static final String ERROR_DELETE_FRUIT = "Error encountered when deleting the fruit ";
        public static final String NO_ID_UPDATE_FRUIT = "Fruit does not have a FruitID";
        public static final String ERROR_PATH_ID = "Path variable fruitId does not match Fruit.FruitID";
        public static final String DELETE_FRUIT_NOT_FOUND = "This fruit does not exist";
        public static String fruitNotExist(Long idFruit){
            return "Fruit with ID "+ idFruit + " doesn't exist";
        }

    }
}
