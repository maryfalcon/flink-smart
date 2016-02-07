/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.myorg.model.Datahash;
import org.myorg.persistor.Persistor;

/**
 *
 * @author maryfalcon
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/DownloadServlet"})
public class DownloadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DownloadServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DownloadServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        processRequest(request, response);
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

        if (((String) request.getParameter("param")).equals("datahash")) {
            int fileid = Integer.valueOf(request.getParameter("fileid")).intValue();
            Datahash dh = pers.getDatahashByFileId(fileid);
            if (dh == null) {
                dh = new Datahash();
                dh.setFlinkdbid(fileid);
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
        if (((String) request.getParameter("param")).equals("hashcheck")) {
            int fileid = Integer.valueOf(request.getParameter("fileid")).intValue();
            Datahash dh = pers.getDatahashByFileId(fileid);
            if(dh!=null && Arrays.equals(byteString.getBytes(), dh.getHash()) && Arrays.equals(dateHash.getBytes(), dh.getDatehash()) && Arrays.equals(placeHash.getBytes(), dh.getPlacehash()))
                response.getOutputStream().println("true");
            else
                response.getOutputStream().println("false");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
