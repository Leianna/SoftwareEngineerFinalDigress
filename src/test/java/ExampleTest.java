import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExampleTest {

    @Test
    void getBalanceTest() {
        Example bankAccount = new Example("a@b.com", 1000);

        assertEquals(1000, bankAccount.getBalance());
    }
}