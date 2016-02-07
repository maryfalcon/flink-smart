/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.servlet;

import org.myorg.model.Data;
import org.myorg.service.DataService;
import org.myorg.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author maryfalcon
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/download"})
public class DownloadServlet extends HttpServlet {

    private static final long serialVersionUID = -7075052875728717573L;

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
        if (Actions.LOGIN.toString().equals(action)) {
            boolean isLogged = userService.loginOrRegister(request);
            response.getOutputStream().print(isLogged);
            return;
        } else {
            boolean isLogged = isLogged(request);
            if (!isLogged) {
                response.getOutputStream().print(false);
                return;
            }
            if (Actions.FILES_LIST.toString().equals(action)) {
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
