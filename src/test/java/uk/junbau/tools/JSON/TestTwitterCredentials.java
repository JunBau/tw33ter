package uk.junbau.tools.JSON;

import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static org.testng.Assert.*;

public class TestTwitterCredentials {
    @Test
    public void testTwitterCredentialsDeserializesCorrectly() throws IOException {
        //Given a JSON string of credentials.
        String json = "{\"consumerKey\":\"9q28hf4poasji\"," +
                       "\"consumerSecret\":\"q289w4phfof028gh\"," +
                       "\"userToken\":\"8024qvjpij\"," +
                       "\"secretKey\":\"028g43n[pqgio\"}";

        //When we deserialize it
        ITwitterCredentials creds;
        try (InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(json.getBytes("UTF-8")))) {
            creds = TwitterCredentials.deserialize(in);
        }

        //Then it should contain the expected data.
        assertNotNull(creds);
        assertEquals(creds.getConsumerKey(), "9q28hf4poasji");
        assertEquals(creds.getConsumerSecret(), "q289w4phfof028gh");
        assertEquals(creds.getUserToken(), "8024qvjpij");
        assertEquals(creds.getSecretKey(), "028g43n[pqgio");
    }
}