import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/addSkill")
public class AddSkillServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        // Get the logged-in student's session
        HttpSession session = request.getSession(false);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Check whether the student is logged in
        if (session == null ||
            session.getAttribute("student_id") == null) {

            out.println("<h1>Please login first</h1>");
            return;
        }

        // Get student ID from session
        int studentId =
            (Integer) session.getAttribute("student_id");

        // Get skill entered by student
        String skillName =
            request.getParameter("skill_name");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
    "jdbc:mysql://student-matcher-db-deepikasidda1504-511b.k.aivencloud.com:23378/defaultdb?sslMode=REQUIRED",
    "avnadmin",
    System.getenv("DB_PASSWORD")
);
            String sql =
                "INSERT INTO skills (student_id, skill_name) VALUES (?, ?)";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setString(2, skillName);

            ps.executeUpdate();

            out.println("<h1>Skill Added Successfully!</h1>");
            out.println("<p>Your skill: " + skillName + "</p>");

            out.println("<br>");
            out.println("<a href='skills.html'>Add Another Skill</a>");

            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<h1>Failed to Add Skill</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}