import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.webtree.auth.domain.User;

public class MockRepo {
    private static final String USERNAME = "testUser";

    public static void main(String[] args) throws Exception {
        ClientAndServer clientAndServer = null;
        try {
            clientAndServer = ClientAndServer.startClientAndServer(3000);

            mockFindByUsername(clientAndServer);

            while (true);
        } finally {
            if (clientAndServer != null) {
                clientAndServer.stop(true);
            }
        }
    }

    private static void mockFindByUsername(ClientAndServer clientAndServer) throws JsonProcessingException {
        HttpRequest expectedRequest = new HttpRequest()
                .withMethod("GET")
                .withPath("/user/name/" + USERNAME);

        User user = User.builder().withUsername(USERNAME).withPassword("password").build();
        HttpResponse httpResponse = new HttpResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(new ObjectMapper().writeValueAsString(user));

        clientAndServer.when(expectedRequest).respond(httpResponse);
    }
}
