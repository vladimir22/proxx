package com.vladimir22.agmatics;


import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static java.lang.System.out;



public class DeviceRequestHandle<DeviceCurrentData> extends HttpServlet {



//    public DeviceRequestHandle() {
//        super();    //  is not needed here
//    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException { //GET usually used for retrieving resources

        String deviceId = null; // deviceId not related to class, it should be local variable

        JSONObject responseJson = new JSONObject(); // It is better to play with JSON using POJOs instead of raw text

        try {
//            deviceId = request.body.deviceId; // GET request may not have a body, for GET request you should use request parameters instead

            deviceId = request.getParameter("deviceId");  // Use request parameters instead of body for GET request

            if ("POST".equalsIgnoreCase(request.getMethod()))
            {
                String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

                // We have to parse properly the request body into JSON to get the device id
                JSONObject json = new JSONObject(body);
                deviceId = json.getString("deviceId");

            }



            if (DevicesDataDAO.isDeviceIdExist(deviceId) ) {
                // If you want to update data use PUT method
                DeviceCurrentData currentDeviceData = getCurrentDeviceData(deviceId);
                CurrentDeviceDAO.setCurrentDeviceData(currentDeviceData);
                out.print("success"); // Use logging instead of System.out

                // Set response content type
                response.setContentType("application/json");
                // Set status code
                response.setStatus(HttpServletResponse.SC_OK);

                responseJson.put("message","MessagesToDevice.success");




            }

            // If the deviceId is not exist in the database, we have to create a new record using POST method

            out.print("device not exist"); // Use logging instead of System.out

        } catch (JSONException e) {
            out.print("JSON error" + e.toString()); // it is a bad practice to suppress exception use logger and ExceptionAdvice instead

            responseJson.put("message","JSON error" + e.toString());

            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON error" + e.toString());

        }

        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(responseJson); // Write response body in a JSON format


//        out.print("{\"message\":\"" + MessagesToDevice.success + "\"}");

        response.flushBuffer();

//        out.flush();
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) // POST usually used for creating new resources
            throws IOException {
        doGet(request, response);
    }



    private DeviceCurrentData getCurrentDeviceData(String deviceId) {
        return null;
    }
}
