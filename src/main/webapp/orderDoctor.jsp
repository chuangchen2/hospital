<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>找医生--在线预约挂号系统</title>
    <jsp:include page="include/headtag.jsp"/>
    <link rel="stylesheet" type="text/css" href="css/orderDoctor.css">
</head>
<body>
<jsp:include page="include/head.jsp"/>
<jsp:include page="include/menu.jsp"/>

<div style="margin-top: 100px;margin-left: 300px">
    <div id="page-inner">
        <div class="search">
            <h1>按医生挂号</h1>
            <form id="doctor" action="searchDoctor" method="post">
                <input  value="${doctor}" name="doctor"  autocomplete="off" class="" placeholder="查找医生">
                <input id="start" hidden value="1" name="start">
                <input id="order" hidden value="doctor" name="order">
                <button type="submit">查找医生</button>
            </form>
        </div>

        <div class="doctors">
            <c:forEach items="${doctors}" var="item">
                <div class="ysjs">
                    <div class="title">
                        <div class="t">
                            <span><a href="showWeek?did=${item.did}">${item.dname}</a></span>
                            <span class="gender">${item.gender}</span>
                            <span class="career">${item.career}</span>
                            <span class="career">${item.age}岁</span>
                        </div>
                        <img src="${item.picpath}">
                    </div>
                    <div class="content">
                        <div >
                            <div>介绍：</div>
                            <div>${item.description}</div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

<%--分页--%>
    <c:if test="${pages.totalPage > 0}">
        <form id="pagination">
            <ul class="pagination">
                <li <c:if test="${pages.currentPage == 1}">class="disabled"</c:if>><a
                        onclick="jump('${pages.prePage }')">&laquo;</a></li>
                <c:forEach begin="${pages.pageNumStart }" end="${pages.pageNumEnd }"
                           varStatus="status">
                    <li>
                        <a <c:if test="${status.index == pages.currentPage }">class="disabled"</c:if>
                            onclick="jump('${status.index }')">
                                ${status.index }
                        </a>
                    </li>
                </c:forEach>
                <li <c:if test="${pages.currentPage == pages.totalPage}">class="disabled"</c:if>>
                    <a onclick="jump('${pages.nextPage }')">&raquo;</a>
                </li>
                <li><input class="text-input" id="index"></li>
                <li><a onclick="jumpInput()">Go</a></li>
                <li><a class="disabled">共${pages.totalPage}页${pages.totalRecord}条记录</a>

                <script>
                    function jump(index) {
                        $("#start").val(index);
                        $("#doctor").submit();
                    }
                    function jumpInput() {
                        var index=$("#index").val();
                        var match = /^[1-9][0-9]*[0-9]*$/;
                        if(match.test(index)){
                            jump(index);
                        }else {
                            alert("请输入数字！")
                        }
                    }
                </script>
            </ul>
        </form>
</c:if>
    </div>
</div>

</body>
</html>
