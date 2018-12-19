import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.Scanner;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title1;
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " +
                        "transitional//en\">\n";
        try {
            HttpSession session = request.getSession(true);
            if (!session.isNew()) {
                if (checkSession(session)) {
                    title1 = "Success";
                    out.println(docType +
                            "<html>\n" +
                            "<head><title>" + title1 + "</title></head>\n" +
                            "<body bgcolor = \"#f0f0f0\">\n" +
                            "<h1 align = \"center\">" + title1 + "</h1>\n" +
                            "<p align = \"center\"> Welcome, "
                            + "<b>" + session.getAttribute("login") + "</b>" + " how are you?" + "<br>" +
                            "You know the correct password: "
                            + "<b>" + session.getAttribute("password") + "</b>" + "</p>" +
                            "</body>" +
                            "</html>");
                    return;
                }
            }

            String str = checkLogIn(request.getParameter("login"));

            if (str != null) {
                String[] args = str.split(":");
                if (args[1].equals((request.getParameter("password")))) {
                    title1 = "Success";
                    session.setAttribute("login", request.getParameter("login"));
                    session.setAttribute("password", request.getParameter("password"));
                    out.println(docType + generateResp(title1,request.getParameter("login"),request.getParameter("password")));
                } else {
                    title1 = "Error";
                    out.println(docType +
                            "<html>\n" +
                            "<head><title>" + title1 + "</title></head>\n" +
                            "<body bgcolor = \"#f0f0f0\">\n" +
                            "<h1 align = \"center\">" + title1 + "</h1>\n" +
                            "<p align = \"center\"> Sorry, you entered the wrong password"
                            + "<br>" +
                            "Access closed" + "</b>" + "</p>" +
                            "</body>" +
                            "</html>");
                }

            } else {
                RequestDispatcher view = request.getRequestDispatcher("create.html");
                try {
                    view.forward(request, response);
                } catch (ServletException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    boolean checkSession(HttpSession s) throws IOException {
        String str = checkLogIn((String)s.getAttribute("login"));
        if(str == null)
            return false;
        String[] args = str.split(":");
        if(args[0].equals(s.getAttribute("login")) && args[1].equals(s.getAttribute("password"))){
                return true;
        }
        return false;
    }

    public String generateResp(String title,String login,String password){
        String res = "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor = \"#f0f0f0\">\n" +
                "<h1 align = \"center\">" + title + "</h1>\n" +
                "<p align = \"center\"> Welcome, "
                + "<b>" + login + "</b>" + " how are you?" + "<br>" +
                "You know the correct password: "
                + "<b>" + password + "</b>" + "</p>" +
                "</body>" +
                "</html>";
        return res;

    }


    String checkLogIn(String login) throws IOException {

        FileReader fileReader = new FileReader(getServletContext().getRealPath("/database.txt"));
        Scanner scan =  new Scanner(fileReader);
        String str = null;
        while (scan.hasNextLine()) {
            str = scan.nextLine();
            String[] args = str.split(":");
            if(args[0].equals(login)){
                fileReader.close();
                return str;
            }
        }
        fileReader.close();
        return null;
    }

}
