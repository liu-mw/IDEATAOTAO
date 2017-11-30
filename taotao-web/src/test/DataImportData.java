import com.taotao.web.service.DataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.reflect.annotation.ExceptionProxy;

/**
 * @author liu_mw
 * @date 2017/11/30 11:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ="classpath:spring/*.xml")
public class DataImportData {
    @Autowired
    private DataService dataService;
    @Test
    public void dataImportTest() throws Exception{
        this.dataService.importData();
    }
}
