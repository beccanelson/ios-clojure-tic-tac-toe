import application.controller.ComputerGameController;
import com.server.ResponseData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComputerGameControllerTest {
    private final ComputerGameController controller = new ComputerGameController();
    private final String[] initialBoard = new String[] {"", "", "", "", "", "", "", "", ""};

    @Test
    public void toStringArrayReturnsStringArrayFromJSONArray() throws Throwable {
        JSONArray jsonArray = new JSONArray(initialBoard);
        String[] stringArray = controller.toStringArray(jsonArray);
        assertArrayEquals(initialBoard, stringArray);
    }

    @Test
    public void sendResponseDataSetsTheBodyOfTheController() throws Throwable {
        ResponseData data = new ResponseData();
        data.sendRequestBody("test");
        controller.sendResponseData(data);
        assertEquals(controller.body, "test");
    }

    @Test
    public void callingPostAfterSettingBodyReturnsResponse() throws Throwable {
        ResponseData data = new ResponseData();
        data.sendRequestBody("{ \"board\": [\"X\",\"X\",\"X\",\"O\",\"\",\"\",\"\",\"\",\"O\"], \"gameType\": \"humanVsHuman\"}");
        controller.sendResponseData(data);
        String response = new String(controller.post());
        assertTrue(response.contains("status"));
        assertTrue(response.contains("board"));
    }

    @Test
    public void callingPostAfterSettingBodyReturnsInProgressResponse() throws Throwable {
        ResponseData data = new ResponseData();
        data.sendRequestBody("{ \"board\": [\"X\",\"\",\"X\",\"O\",\"\",\"\",\"\",\"\",\"O\"], \"gameType\": \"humanVsHuman\"}");
        controller.sendResponseData(data);
        String response = new String(controller.post());
        assertTrue(response.contains("status"));
        assertTrue(response.contains("board"));
    }

    @Test
    public void callingPostAfterSettingInvalidBodyReturns400Response() throws Throwable {
        ResponseData data = new ResponseData();
        data.sendRequestBody("{\"gameType\": \"humanVsHuman\"}");
        controller.sendResponseData(data);
        String response = new String(controller.post());
        assertTrue(response.contains("400"));
        assertTrue(response.contains("Bad Request"));
    }

    @Test
    public void getRequestReturnsWelcomeMessage() throws Throwable {
        String response = new String(controller.get());
        assertTrue(response.contains("200"));
        assertTrue(response.contains("Welcome"));
    }

    @Test
    public void getJSONPropertyWithDefaultChecksForExistingJSONProperty() throws Throwable {
        JSONObject json = new JSONObject();
        json.put("property", "test");
        String value = controller.getJSONPropertyWithDefault(json, "property", "does not exist");
        assertEquals("test", value);
    }

    @Test
    public void getJSONPropertyWithDefaultReturnsDefaultIfNoProperty() throws Throwable {
        JSONObject json = new JSONObject();
        String value = controller.getJSONPropertyWithDefault(json, "property", "does not exist");
        assertEquals("does not exist", value);
    }

    @Test
    public void createGameJSONReturnsAJSONObjectWithAStatusAndBoard() throws Throwable {
        JSONObject jsonData = controller.createGameJSON("in progress", initialBoard);
        assertEquals("in progress", jsonData.get("status"));
        assertEquals(initialBoard, jsonData.get("board"));
    }

    @Test
    public void setsPropertiesFromBody() throws Throwable {
        JSONObject jsonData = new JSONObject();
        jsonData.put("board", initialBoard);
        jsonData.put("gameType", "humanVsComputer");
        jsonData.put("computerMarker", "O");
        jsonData.put("computerDifficulty", "hard");
        controller.setPropertiesFromRequestBody(jsonData.toString());

        assertArrayEquals(initialBoard, controller.board);
        assertEquals("humanVsComputer", controller.gameType);
        assertEquals("hard", controller.computerDifficulty);
        assertEquals("O", controller.computerMarker);

    }

    @Test
    public void getFileContentsFromFilenameReturnsHTMLFromFile() throws Throwable {
        String content = new String(controller.getFileContentsFromFilename("./Welcome.html"));
        assertTrue(content.contains("Welcome"));
    }
}
