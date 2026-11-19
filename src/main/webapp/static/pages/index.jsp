<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>职工考勤管理系统 - 首页</title>
    <link rel="stylesheet" href="../css/style.css">
    <style>
        body { font-family: 'Microsoft YaHei', sans-serif; background: #f5f7fa; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
        .welcome { font-size: 24px; }
        .info { opacity: 0.9; margin-top: 10px; }
        .cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }
        .card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .card h3 { color: #667eea; margin-bottom: 10px; }
        .card p { color: #666; }
        .card .value { font-size: 32px; font-weight: bold; color: #333; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>欢迎使用职工考勤管理系统</h1>
            <div class="info">
                <p>当前用户：${sessionScope.currentUser.realName} (${sessionScope.currentUser.role == 3 ? '管理员' : sessionScope.currentUser.role == 2 ? '部门经理' : '普通员工'})</p>
                <p>所属部门：${sessionScope.currentUser.deptName}</p>
                <p>登录时间：<fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            </div>
        </div>
        
        <div class="cards">
            <div class="card">
                <h3>今日打卡</h3>
                <p class="value" id="clockStatus">--</p>
                <p>上班时间：09:00</p>
            </div>
            <div class="card">
                <h3>我的请假</h3>
                <p class="value" id="leaveCount">0</p>
                <p>本月申请</p>
            </div>
            <div class="card">
                <h3>考勤统计</h3>
                <p class="value" id="attendDays">0</p>
                <p>本月出勤天数</p>
            </div>
            <div class="card">
                <h3>系统公告</h3>
                <p class="value" id="annCount">0</p>
                <p>条未读公告</p>
            </div>
        </div>
    </div>
    
    <script>
        // 检查登录状态
        <c:if test="${empty sessionScope.currentUser}">
            window.location.href = '../login.html';
        </c:if>
    </script>
</body>
</html>
