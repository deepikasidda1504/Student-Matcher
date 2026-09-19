import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
               "jdbc:mysql://student-matcher-db-deepikasidda1504-511b.k.aivencloud.com:23378/defaultdb?sslMode=REQUIRED",
"avnadmin",
               System.getenv("DB_PASSWORD")
            );

            String sql =
                "SELECT * FROM students WHERE email=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // =========================
                // CREATE SESSION
                // =========================

                HttpSession session = request.getSession();

                // Store student ID
                session.setAttribute(
                    "student_id",
                    rs.getInt("student_id")
                );

                // Store student name
                session.setAttribute(
                    "name",
                    rs.getString("name")
                );

                // Store email
                session.setAttribute(
                    "email",
                    rs.getString("email")
                );

                // Store college
                session.setAttribute(
                    "college_name",
                    rs.getString("college_name")
                );


                // =========================
                // SEND LOGIN EMAIL
                // =========================

                String emailSubject =
                    "Student Matcher - Login Successful";

                String emailMessage =
                    "Hello " + rs.getString("name") + ",\n\n"
                    + "You have successfully logged in to "
                    + "Student Matcher.\n\n"
                    + "If this login was not made by you, "
                    + "please contact the administrator.\n\n"
                    + "Thank you,\n"
                    + "Student Matcher Team";

                try {
    EmailService.sendEmail(
        rs.getString("email"),
        emailSubject,
        emailMessage
    );
} catch (Exception emailError) {
    System.out.println("Login email failed: "
        + emailError.getMessage());
}

                // =========================
                // ONE MAIN PAGE
                // =========================

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");
                out.println("<title>Student Matcher</title>");
                out.println("</head>");

                out.println("<body>");

                out.println("<h1>Student Matcher</h1>");

                out.println(
                    "<h2>Welcome "
                    + rs.getString("name")
                    + "!</h2>"
                );

                out.println("<hr>");


                // =========================
                // PROFILE
                // =========================

                out.println("<h2>My Profile</h2>");

                out.println(
                    "<p><b>Name:</b> "
                    + rs.getString("name")
                    + "</p>"
                );

                out.println(
                    "<p><b>Email:</b> "
                    + rs.getString("email")
                    + "</p>"
                );

                out.println(
                    "<p><b>College:</b> "
                    + rs.getString("college_name")
                    + "</p>"
                );

                out.println("<hr>");


                // =========================
                // SEARCH STUDENTS BY SKILL
                // =========================

                out.println(
                    "<h2>Search Students by Skill</h2>"
                );

                out.println(
                    "<form action='match' method='get'>"
                );

                out.println(
                    "<input type='text' name='skill' "
                    + "placeholder='Enter skill e.g. Java' "
                    + "required>"
                );

                out.println(
                    "<input type='submit' value='Search'>"
                );

                out.println("</form>");

                out.println("<br>");


                // =========================
                // ADD MY SKILLS
                // =========================

                out.println(
                    "<a href='skills.html'>Add My Skills</a>"
                );

                out.println("<br><br>");


                // =========================
                // LOGOUT
                // =========================

                out.println(
                    "<a href='login.html'>Logout</a>"
                );

                out.println("</body>");
                out.println("</html>");

            } else {

                out.println("<h1>Invalid Email or Password</h1>");

                out.println(
                    "<a href='login.html'>Try Again</a>"
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<h1>Error</h1>");

            out.println(
                "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}