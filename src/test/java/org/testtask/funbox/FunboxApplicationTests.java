package org.testtask.funbox;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testtask.funbox.controller.WebController;
import org.testtask.funbox.entity.Link;
import org.testtask.funbox.entity.Status;
import org.testtask.funbox.repository.LinkRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FunboxApplicationTests {

    @Autowired
    private WebController controller;

    @Autowired
    private LinkRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String urlPost = "http://localhost:8080/visited_links";
    private final String urlGet = "http://localhost:8080/visited_domains";

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(repository).isNotNull();
        assertThat(restTemplate).isNotNull();
    }

    // Testing controller by posting data
    @Test
    public void testControllerWithValidDataPost() {

        String requestBody = "{\n" +
                "  \"links\": [\n" +
                "  \"https://ya.ru\",\n" +
                "  \"https://ya.ru?q=123\",\n" +
                "  \"funbox.ru\",\n" +
                "  \"https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor\"\n" +
                "  ]\n" +
                "}";

        Status responseStatus = restTemplate.postForObject(urlPost, requestBody, Status.class);

        assertThat(responseStatus).isEqualTo(Status.getOkStatus());
    }

    @Test
    public void testControllerWithInvalidDataPost() {

        String requestBody = "{\n" +
                "  \"lnks\": [\n" +
                "  \"https://ya.ru\",\n" +
                "  \"https://ya.ru?q=123\",\n" +
                "  \"funbox.ru\",\n" +
                "  \"https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor\"\n" +
                "  ]\n" +
                "}";

        Status responseStatus = restTemplate.postForObject(urlPost, requestBody, Status.class);

        assertThat(responseStatus).isEqualTo(Status.getErrorStatus());
    }

    @Test
    public void testControllerWithInvalidDataExtraFieldPost() {

        String requestBody = "{" +
                "\"links\":[" +
                "\"https://ya.ru\"," +
                "\"https://ya.ru?q=123\"," +
                "\"funbox.ru\"," +
                "\"https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor\"" +
                "]," +
                "\"test\":\"test\"" +
                "}";

        Status responseStatus = restTemplate.postForObject(urlPost, requestBody, Status.class);

        assertThat(responseStatus).isEqualTo(Status.getErrorStatus());
    }

    // Testing controller by getting data
    @Test
    public void testControllerWithValidDataGet() {

        String[] domains = {"https://ya.ru",
                "https://ya.ru?q=123",
                "funbox.ru",
                "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor"};

        Link link = new Link(100l, domains);

        String[] domains2 = {"https://ya.ru",
                "https://ya.ru?q=123",
                "funbox.ru",
                "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor",
                "vk.com"
        };

        Link link2 = new Link(105l, domains2);

        repository.add(link);
        repository.add(link2);

        String timeFrom = "100";
        String timeTo = "120";
        String url = urlGet + "?from=" + timeFrom + "&to=" + timeTo;

        String expectedResponse = "{" +
                "\"domains\":[" +
                "\"https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor\"," +
                "\"https://ya.ru?q=123\"," +
                "\"https://ya.ru\"," +
                "\"funbox.ru\"," +
                "\"vk.com\"" +
                "]," +
                "\"status\":\"Ok\"" +
                "}";

        String actualResponse = restTemplate.getForObject(url, String.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testControllerWithValidDataWrongTimeGet() {

        String[] url = {"https://ya.ru",
                "https://ya.ru?q=123",
                "funbox.ru",
                "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor"};

        Link link = new Link(100l, url);

        String[] url2 = {"https://ya.ru",
                "https://ya.ru?q=123",
                "funbox.ru",
                "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor",
                "vk.com"
        };

        Link link2 = new Link(105l, url2);

        repository.add(link);
        repository.add(link2);

        String timeFrom = "0";
        String timeTo = "50";

        String expectedResponse = "{" +
                "\"domains\":[]," +
                "\"status\":\"Ok\"" +
                "}";

        String actualResponse = restTemplate.getForObject(urlGet + "?from=" + timeFrom + "&to=" + timeTo, String.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testControllerWithInvalidTimeGet() {

        String[] url = {"https://ya.ru",
                "https://ya.ru?q=123",
                "funbox.ru",
                "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor"};

        Link link = new Link(100l, url);

        String[] url2 = {"https://ya.ru",
                "https://ya.ru?q=123",
                "funbox.ru",
                "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor",
                "vk.com"
        };

        Link link2 = new Link(105l, url2);

        repository.add(link);
        repository.add(link2);

        String timeFrom = "100";
        String timeTo = "50";

        String expectedResponse = "{" +
                "\"status\":\"Error\"" +
                "}";

        String actualResponse = restTemplate.getForObject(urlGet + "?from=" + timeFrom + "&to=" + timeTo, String.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

}
