package ca.jrvs.apps.twitter.dao.helper;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TwitterDaoUnitTest {
    @Mock
    HttpHelper mockHelper;

    @InjectMocks
    TwitterDao dao;


}
