import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestController.class, TestListManager.class, TestRecipe.class, TestRestaurant.class })
public class AllTests {

}
