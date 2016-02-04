/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kafka.producer.servlet;

import org.kafka.producer.dto.UserFileDto;
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
 *
 * @author maryfalcon
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/DownloadServlet"})
public class DownloadServlet extends HttpServlet {

    enum Actions {
        LOGIN,
        SEND_FILE,
        SEND_SIGN,
        UPDATE,
        FILES_LIST
    }

    private UserService userService = new UserService();

    private DataService dataService = new DataService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        InputStream input = request.getInputStream();
        boolean isLogged = isLogged(request);
        if (!isLogged) {
            response.getOutputStream().print(false);
            return;
        }
        if (Actions.LOGIN.toString().equals(action)) {
            response.getOutputStream().print(true);
            return;
        } else if (Actions.SEND_FILE.toString().equals(action)) {
            UserFileDto data = dataService.saveData(request, input);
            response.getOutputStream().print(data.getId());
            return;
        } else if (Actions.SEND_SIGN.toString().equals(action)) {
            try {
                String res = dataService.saveSign(request, input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.getOutputStream().print(true);
            return;
        } else if (Actions.FILES_LIST.toString().equals(action)) {
            String fileNames = dataService.getFilesNames();
            response.getOutputStream().print(fileNames);
            return;
        } else if (Actions.UPDATE.toString().equals(action)) {
            boolean answer = false;
            try {
                answer = dataService.checkSignature(request, input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.getOutputStream().print(answer);
            return;
        }
        response.getOutputStream().print(true);
    }

    private boolean isLogged(HttpServletRequest request) {
        String userLoginHash = request.getHeader("Authorization");
        if (userLoginHash != null) {
            userLoginHash = userLoginHash.substring(6);
        }
        return userService.handleLogin(userLoginHash);
    }

}
