/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import dto.MessageDTO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Lahiru-PC
 */
public class PublicChatServlet extends HttpServlet {
    ArrayList<MessageDTO> arrMessage=new ArrayList<>();
    ArrayList<String> arrOnlineUsers=new ArrayList<>();
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
        HttpSession session=session=request.getSession();
                
        //Date d=new Date(session.getCreationTime());
//        System.out.println(session.isNew()+"\t"+session.getId()+"\t"+d);
        session.setAttribute("count","");
                
        
        switch(request.getParameter("action")){            
            case "frist-time":
//                System.out.println("in action frist time\t"+session.isNew()+"\t"+session.getId()+"\t"+d); 
                boolean aNew = session.isNew();
                if(aNew){
                    response.getOutputStream().print("frist-time");
                }
                break;
            case "loging":
                session.setAttribute("userName", request.getParameter("name"));
                arrOnlineUsers.add(request.getParameter("name"));
                break;
            case "send":
                MessageDTO dto=new MessageDTO();
                dto.setDate(new Date());
                dto.setName((String) session.getAttribute("userName"));
                dto.setMessage(request.getParameter("message"));
                arrMessage.add(dto);
                
//                System.out.println("json array"+messageToJSON(arrMessage));
                response.getOutputStream().print((String) session.getAttribute("userName"));
                
                break;
            case "see-messages":
                response.getOutputStream().print(messageToJSON(arrMessage, (String) session.getAttribute("userName")));
                break;
            case "online-users":
                JSONArray array=new JSONArray();
                arrOnlineUsers.forEach((n) -> {
                    array.add(n);
                });
                String jsonOnlineUsers=JSONArray.toJSONString(array);
                response.getOutputStream().print(jsonOnlineUsers);
                break;
            case "grab-snap":
                String base64Image = (request.getParameter("imgBase64"));
                base64Image = base64Image.split(",")[1];
                byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
                BufferedImage img;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    File file = new File("/Users\\Lahiru-PC\\Pictures\\img"+session.getAttribute("userName")+""+session.getLastAccessedTime()+".jpg");
                    System.out.println(file.getAbsolutePath());
                    boolean write = ImageIO.write(img, "jpg", file);
                } catch (IOException ex) {
                    Logger.getLogger(PublicChatServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
    }
    private String messageToJSON(ArrayList<MessageDTO> messageDTOs,String name){
        String array="{\"msg\":[";
        array = messageDTOs.stream().map((dTO) -> {
            JSONObject jsono=new JSONObject();    
            String date=String.valueOf(dTO.getDate());
            jsono.put("date",date);
            jsono.put("name", dTO.getName());
            jsono.put("message", dTO.getMessage());
//            System.out.println(jsono);
            return jsono;
        }).map((jsono) -> jsono+",").reduce(array, String::concat);
        
        array=array.substring(0,array.length()-1);
        array+="],\"myName\":\""+name+"\"";
        return array+="}";
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
        processRequest(request, response);
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
