package DKSheetsAPI;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;


// *******
// FYI DEVANSH FROM PAST SPEAKING
// OPEN THIS LINK: https://developers.google.com/sheets/api/quickstart/java
// CLICK ON ENABLE GOOGLE SHEETS API
// DOWNLOAD CREDENTIAL FILE AND DRAG INTO RESOURCES FOLDER IN MAIN
// UPDATE INFO BELOW
// FIRST RUN IT WILL ASK YOU TO LOGIN THRU OAUTH DO THIS!
// THANKS - DEVANSH FROM PAST SIGNING OFF
// *******

/**
 * DKSheets API
 * This class is the middleman for communications between
 * Java Applications and Google Sheets / API
 *
 * I learned this code from Google's Developer website, and modified it for the purpose of this application
 *
 * Resource Link https://developers.google.com/sheets/api/quickstart/java
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class DKSheetsAPI {
    private static Sheets sheetsService;
    final private static String APPLICATION_NAME = "DKSheets API";
    final private static String SPREADSHEET_ID = "19qLolwxb1v6WR2pOGeVEEo1lFEvwcvQsVNw3SWSmx20";
    final private static int GID = 0;

    private String sheet;

    /**
     * Authorizes the computer client to access google docs api
     * This is the OAUTH workflow to authenticate a user
     *
     * @return Credentials used to verify
     * @throws IOException File could not be opened
     * @throws GeneralSecurityException Security Erro r
     */
    private static Credential authorize() throws IOException, GeneralSecurityException {
        // Get credentials file
        InputStream in = DKSheetsAPI.class.getResourceAsStream("/credentials.json");

        // Get Client Secrets from file
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(),
                new InputStreamReader(in));

        // Scopes of our file
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        // Authorization Code Flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();

        // Create credentials to return
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("user");

        return credential;

    }


    /**
     * Builds link and opens api link
     *
     * @return instance of sheets builder
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize(); // Grad credentials

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    /**
     * Main function
     * @param args arguments
     * @throws IOException File could not be opened
     * @throws GeneralSecurityException API does not have permission to access sheet (permissions/security)
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService(); // Get data
//        addData();

    }

    /**
     * Constructor
     *
     * @param sheet Sheet / tab name
     * @throws IOException File could not be opened
     * @throws GeneralSecurityException permissions error
     */
    public DKSheetsAPI(String sheet) throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
        this.sheet = sheet;
    }

    /**
     * Add data to sheet
     *
     * @param rowData Row of data being added
     * @throws IOException Could not open file
     */
    public void addData(String[] rowData) throws IOException {

        // Row of data to append
        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(Arrays.asList(rowData)));

        // Response after inserting / while  - unused
        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, this.sheet, appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();

    }

    /**
     * Update a particular cell
     *
     * @param cellLocation Location of cell being updated
     * @param cellValue New value
     * @throws IOException File could not be opened
     */
    public void updateData(String cellLocation, String cellValue) throws IOException {

        // Update
        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList(cellValue) //C543
                ));

        // Resposne
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, cellLocation, body)
                .setValueInputOption("RAW")
                .execute();
    }

    /**
     * Read the data
     * @param range0 Range of data being read
     *
     * @return Data as 2D array
     * @throws IOException File could not be opened
     */
    public List<List<Object>> readData(String range0) throws IOException {

        String range = this.sheet + "!" + range0; // Build range
//        String range = "sheet!A2:F10";

        // Get data
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();

       return response.getValues();

    }

    /**
     * Get the last index by counting all rows
     *
     * @return Index
     * @throws IOException File could not be opened
     * @throws NullPointerException If there is no rows
     */
    public String getLastIndex() throws IOException, NullPointerException{
        List<List<Object>> data = readData("A2:P");
        System.out.println(readData("A2:P"));
        return (String) data.get(data.size()-1).get(0);

    // * -  FIX NULL POINTER ERROR IF NO DATA IS PRESENT IN SHEET...

    }

//    public static void deleteData() throws IOException {
//
//        DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
//                .setRange(
//                        new DimensionRange()
//                                .setSheetId(GID) // #gid
//                                .setDimension("ROWS")
//                                .setStartIndex(542) // 0-index
//                );
//
//        List<Request> requests = new ArrayList<>();
//        requests.add(new Request().setDeleteDimension(deleteRequest));
//
//        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
//        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();
//    }



}