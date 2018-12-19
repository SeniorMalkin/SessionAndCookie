import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet("/ServletCookie")
public class ServletCookie extends Servlet {
    private Cookie findedCookie;
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String title1;
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " +
                        "transitional//en\">\n";
        Cookie[] cookies = null;
        cookies = req.getCookies();
        try {
            Cookie cookie = new Cookie(req.getParameter("login"),req.getParameter("password"));
            if (checkCookie(cookies)) {
                    title1 = "Success";
                    out.println(docType + generateResp(title1,findedCookie.getName(),findedCookie.getValue()));
                    return;
            }
            else {

                cookie.setMaxAge(10000);
                resp.addCookie(cookie);
                String str = checkLogIn(req.getParameter("login"));

                if (str != null) {
                    String[] args = str.split(":");
                    if (args[1].equals((req.getParameter("password")))) {
                        title1 = "Success";
                        out.println(docType + generateResp(title1,req.getParameter("login"),req.getParameter("password")));;

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
                    RequestDispatcher view = req.getRequestDispatcher("create.html");
                    try {
                        view.forward(req, resp);
                    } catch (ServletException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }

        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    boolean checkCookie(Cookie[] arr) throws IOException {
        FileReader fileReader = new FileReader(getServletContext().getRealPath("/database.txt"));
        Scanner scan =  new Scanner(fileReader);
        if(arr == null) {
            fileReader.close();
            return false;
        }
        String res = null;
        for (int i = 0; i < arr.length; i++) {
            scan.reset();
            res = checkLogIn(arr[i].getName());
            if(res != null){
                String[] args = res.split(":");
                if(args[0].equals(arr[i].getName()) && args[1].equals(arr[i].getValue())){
                    findedCookie = arr[i];
                    fileReader.close();
                    return true;
                }
            }
        }
        fileReader.close();
        return false;
    }

}
