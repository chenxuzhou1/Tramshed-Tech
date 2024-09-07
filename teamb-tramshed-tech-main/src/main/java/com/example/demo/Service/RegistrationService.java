package com.example.demo.Service;


import com.example.demo.dto.ClientDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistrationService {
    @Value("${notion.api.url}")
    private String notionApiUrl;

    @Value("${notion.api.token}")
    private String notionApiToken;

    @Value("${notion.api.client-database-id}")
    private String clientDatabaseId;

    @Autowired
    private RestTemplate restTemplate;


    public void createClient(ClientDto clientDto) {
        String url = notionApiUrl + "/v1/pages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(notionApiToken);
        headers.set("Notion-Version", "2022-06-28");

        JSONObject payload = buildPayload(clientDto);

        HttpEntity<String> entity = new HttpEntity<>(payload.toString(), headers);

        restTemplate.postForObject(url, entity, String.class);
    }

    private JSONObject buildPayload(ClientDto clientDto) {
        JSONObject properties = new JSONObject();

        properties.put("Full Name", new JSONObject().put("title", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getFullName())))));
        properties.put("Account Name", new JSONObject().put("rich_text", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getAccountName())))));
        properties.put("Gender", new JSONObject().put("select", new JSONObject().put("name", clientDto.getGender())));
        properties.put("Email", new JSONObject().put("email", clientDto.getEmail()));
        properties.put("Password", new JSONObject().put("rich_text", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getPassword())))));
        properties.put("Phone Number", new JSONObject().put("phone_number", clientDto.getPhoneNumber()));
        properties.put("Location", new JSONObject().put("select", new JSONObject().put("name", clientDto.getLocation())));
        properties.put("Date of birth", new JSONObject().put("rich_text", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getDateOfBirth())))));
        properties.put("Company Name", new JSONObject().put("rich_text", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getCompanyName())))));
        properties.put("Billing address", new JSONObject().put("rich_text", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getBillingAddress())))));
        properties.put("Billing country", new JSONObject().put("rich_text", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getBillingCountry())))));
        properties.put("Billing State / Province / Region", new JSONObject().put("select", new JSONObject().put("name", clientDto.getBillingState())));
        properties.put("Billing City", new JSONObject().put("rich_text", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getBillingCity())))));
        properties.put("Zip / Postcode", new JSONObject().put("rich_text", new JSONArray().put(new JSONObject().put("text", new JSONObject().put("content", clientDto.getZipPostcode())))));
        properties.put("Send my invoices to", new JSONObject().put("email", clientDto.getInvoiceEmail()));

        JSONObject payload = new JSONObject();
        payload.put("properties", properties);
        payload.put("parent", new JSONObject().put("database_id", clientDatabaseId));

        return payload;
    }

}
