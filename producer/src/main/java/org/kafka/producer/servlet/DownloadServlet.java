/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kafka.producer.servlet;

import org.kafka.producer.remote.RemoteUtil;
import org.kafka.producer.service.DataService;
import org.kafka.producer.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author maryfalcon
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/download"})
public class DownloadServlet extends HttpServlet {

    enum Actions {
        LOGIN,
        SEND_FILE,
    }

    private UserService userService = new UserService();

    private DataService dataService = new DataService();

    @Override
    // TODO Remove redirect to doPost. THis is used only for test
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        InputStream input = request.getInputStream();
        if (Actions.SEND_FILE.toString().equals(action)) {
            try {
                String isLogged = RemoteUtil.sendHttpRequest(request);
                if ("true".equals(isLogged)) {
                    dataService.saveData(request, input);
                    response.getOutputStream().print(true);
                } else {
                    response.getOutputStream().print(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
