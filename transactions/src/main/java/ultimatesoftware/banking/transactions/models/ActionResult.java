package ultimatesoftware.banking.transactions.models;

public class ActionResult {
    private final String message;

    public ActionResult(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public String toString() {
        return message;
    }
}