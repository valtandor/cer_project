<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
  <head>
    <meta charset="utf-8">
    <script src="../js/jquery-1.8.3.min.js"></script>
    <script src="../js/jquery.tablesorter.min.js"></script>
    <link rel="stylesheet" href="../style/common.css" type="text/css" />
    <link rel="stylesheet" href="../style/tablesorter/theme.default.css" type="text/css" />
    <script>
      $(document).ready(function() {
        $("#projectTable").tablesorter({theme: "default", sortList: [[3,1],[4,1]]});
      });
  </script>
  </head>

  <body>
    <c:choose>
      <c:when test="${not empty error_message}">
        <div class="errorblock">
          ${error_message}
        </div>
      </c:when>
      <c:otherwise>
        <h3>List of projects you are associated with</h3>
        <c:choose>
          <c:when test="${f:length(projects) gt 0}">
            <table id="projectTable" class="tablesorter">
              <thead>
                <tr>
                  <th>Code</th>
                  <th>Title</th>
                  <th>Your role</th>
                  <th>Status</th>
                  <th>First Day</th>
                  <th>Last Day</th>
                </tr>
              </thead>
              <tbody>
              <c:forEach items="${projects}" var="project">
                <tr>
                  <td>${project.projectCode}&nbsp;</td>
                  <td><a href="view_project?id=${project.id}">${project.name}&nbsp;</a></td>
                  <td>${roles[project.id]}</td>
                  <td>${project.statusName}&nbsp;</td>
                  <td>${project.startDate}&nbsp;</td>
                  <td>${project.endDate}&nbsp;</td>
                </tr>
              </c:forEach> 
              </tbody>
            </table>
          </c:when>
          <c:otherwise>
            <div class="infoblock">
              You don't have any projects registered.
            </div>
          </c:otherwise>
        </c:choose>
      </c:otherwise>
    </c:choose>
  </body>

</html>
