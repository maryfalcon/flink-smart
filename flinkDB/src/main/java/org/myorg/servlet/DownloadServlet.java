/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.servlet;

import org.myorg.model.Datahash;
import org.myorg.persistor.Persistor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 *
 * @author maryfalcon
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/download"})
public class DownloadServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String byteString = request.getParameter("bytes");
        Persistor pers = new Persistor();
        String dateHash = request.getParameter("date");
        String placeHash = request.getParameter("place");

        if ((request.getParameter("param")).equals("datahash")) {
            String fileid = request.getParameter("uuid");
            Datahash dh = pers.getDatahashByUuid(fileid);
            if (dh == null) {
                dh = new Datahash();
                dh.setFlinkdbuuid(fileid);
                dh.setHash(byteString.getBytes());
                dh.setDatehash(dateHash.getBytes());
                dh.setPlacehash(placeHash.getBytes());
                pers.insertDatahash(dh);
            } else {
                dh.setHash(byteString.getBytes());
                dh.setDatehash(dateHash.getBytes());
                dh.setPlacehash(placeHash.getBytes());
                pers.updateDatahash(dh);
                response.getOutputStream().println("update");
            }
        }
        if ((request.getParameter("param")).equals("hashcheck")) {
            String fileid = request.getParameter("fileid");
            Datahash dh = pers.getDatahashByUuid(fileid);
            if(dh!=null && Arrays.equals(byteString.getBytes(), dh.getHash()) && Arrays.equals(dateHash.getBytes(), dh.getDatehash()) && Arrays.equals(placeHash.getBytes(), dh.getPlacehash()))
                response.getOutputStream().println("true");
            else
                response.getOutputStream().println("false");
        }
    }

}
