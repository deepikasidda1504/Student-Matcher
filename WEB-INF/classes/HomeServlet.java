import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get the existing login session
        HttpSession session = request.getSession(false);

        // Check whether the student is logged in
        if (session == null ||
            session.getAttribute("student_id") == null) {

            out.println("<h1>Please login first</h1>");
            out.println("<a href='login.html'>Login</a>");
            return;
        }

        // Get student information from session
        int studentId =
            (Integer) session.getAttribute("student_id");

        String name =
            (String) session.getAttribute("name");

        // Home page
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Student Matcher</title>");
        out.println("</head>");

        out.println("<body>");

        // Heading
        out.println("<h1>Student Matcher</h1>");

        out.println("<hr>");

        // Profile
        out.println("<h2>My Profile</h2>");

        out.println("<p><b>Name:</b> "
                + name + "</p>");

        out.println("<p><b>Student ID:</b> "
                + studentId + "</p>");

        out.println("<hr>");

        // Search
        out.println("<h2>Search Students by Skill</h2>");

        out.println("<form action='match' method='get'>");

        out.println("<input type='text' "
                + "name='skill' "
                + "placeholder='Enter skill e.g. Java' "
                + "required>");

        out.println("<input type='submit' "
                + "value='Search'>");

        out.println("</form>");

        out.println("<br>");

        // Add skills
        out.println("<a href='skills.html'>Add My Skills</a>");

        out.println("<br><br>");

        // Logout
        out.println("<a href='login.html'>Logout</a>");

        out.println("</body>");
        out.println("</html>");
    }
}