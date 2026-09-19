import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/match")
public class MatchServlet extends HttpServlet {

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

        // Get current student's ID
        int currentStudentId =
            (Integer) session.getAttribute("student_id");

        // Get skill entered in search box
        String skill =
            request.getParameter("skill");

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Student Matcher</title>");
        out.println("</head>");

        out.println("<body>");

        out.println("<h1>Student Matcher</h1>");

        out.println("<h2>Students with skill: "
                + skill
                + "</h2>");

        out.println("<hr>");

        try {

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to Aiven MySQL
            Connection con =
                DriverManager.getConnection(
                    "jdbc:mysql://student-matcher-db-deepikasidda1504-511b.k.aivencloud.com:23378/defaultdb?sslMode=REQUIRED",
                    "avnadmin",
                    System.getenv("DB_PASSWORD")
                );

            // Search students having the required skill
            String sql =
                "SELECT students.student_id, "
              + "students.name, "
              + "students.email, "
              + "students.college_name, "
              + "skills.skill_name "
              + "FROM students "
              + "JOIN skills "
              + "ON students.student_id = skills.student_id "
              + "WHERE LOWER(skills.skill_name) = LOWER(?) "
              + "AND students.student_id <> ?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(1, skill);
            ps.setInt(2, currentStudentId);

            ResultSet rs =
                ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                out.println("<h3>"
                        + rs.getString("name")
                        + "</h3>");

                out.println("<p><b>Email:</b> "
                        + rs.getString("email")
                        + "</p>");

                out.println("<p><b>College:</b> "
                        + rs.getString("college_name")
                        + "</p>");

                out.println("<p><b>Skill:</b> "
                        + rs.getString("skill_name")
                        + "</p>");

                out.println("<hr>");
            }

            if (!found) {

                out.println(
                    "<p>No students found with this skill.</p>"
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<h2>Search Failed</h2>");

            out.println("<p>Error: "
                    + e.getMessage()
                    + "</p>");
        }

        out.println("<br>");

        out.println("<a href='home'>Back to Home</a>");

        out.println("</body>");
        out.println("</html>");
    }
}