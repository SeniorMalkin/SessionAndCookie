import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/NewUser")
public class addNewUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileWriter fileWriter = new FileWriter(getServletContext().getRealPath("/database.txt"),true);
        fileWriter.write(req.getParameter("login") + ":" + req.getParameter("password"));
        fileWriter.append('\n');
        fileWriter.close();
        String title1 = "Success";
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " +
                        "transitional//en\">\n";

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title1 + "</title></head>\n" +
                    "<body bgcolor = \"#f0f0f0\">\n" +
                    "<h1 align = \"center\">" + title1 + "</h1>\n" +
                    "<p align = \"center\">"
                    + "<b>" + "User successfully added" + "</p>" +
                    "</body>" +
                     "</html>");
        }

}
