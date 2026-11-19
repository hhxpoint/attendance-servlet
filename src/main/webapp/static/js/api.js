/**
 * API基础配置与工具函数
 * @author 周童
 */

// API基础地址
const API_BASE = '/attendance';

/**
 * 通用GET请求
 */
function apiGet(url) {
    const fullUrl = API_BASE + url;
    console.log('API GET:', fullUrl);
    return fetch(fullUrl, {
        method: 'GET',
        credentials: 'include'
    }).then(res => {
        console.log('API GET Response:', fullUrl, res.status);
        if (!res.ok) {
            throw new Error('HTTP ' + res.status + ': ' + res.statusText);
        }
        return res.json();
    }).catch(err => {
        console.error('API GET Error:', fullUrl, err);
        return { code: 500, msg: '请求失败: ' + err.message, data: null };
    });
}

/**
 * 通用POST请求
 */
function apiPost(url, data) {
    return fetch(API_BASE + url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    }).then(res => {
        if (!res.ok) {
            throw new Error('Network response was not ok');
        }
        return res.json();
    }).catch(err => {
        console.error('API POST Error:', url, err);
        return { code: 500, msg: '请求失败', data: null };
    });
}

/**
 * 通用PUT请求
 */
function apiPut(url, data) {
    return fetch(API_BASE + url, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    }).then(res => {
        if (!res.ok) {
            throw new Error('Network response was not ok');
        }
        return res.json();
    }).catch(err => {
        console.error('API PUT Error:', url, err);
        return { code: 500, msg: '请求失败', data: null };
    });
}

/**
 * 通用DELETE请求
 */
function apiDelete(url) {
    return fetch(API_BASE + url, {
        method: 'DELETE',
        credentials: 'include'
    }).then(res => {
        if (!res.ok) {
            throw new Error('Network response was not ok');
        }
        return res.json();
    }).catch(err => {
        console.error('API DELETE Error:', url, err);
        return { code: 500, msg: '请求失败', data: null };
    });
}

/**
 * 表单POST请求
 */
function apiFormPost(url, formData) {
    return fetch(API_BASE + url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData,
        credentials: 'include'
    }).then(res => {
        if (!res.ok) {
            throw new Error('Network response was not ok');
        }
        return res.json();
    }).catch(err => {
        console.error('API FormPOST Error:', url, err);
        return { code: 500, msg: '请求失败', data: null };
    });
}

/**
 * 格式化日期
 */
function formatDate(dateStr) {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
}

/**
 * 格式化日期时间
 */
function formatDateTime(dateStr) {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    const h = String(date.getHours()).padStart(2, '0');
    const min = String(date.getMinutes()).padStart(2, '0');
    const s = String(date.getSeconds()).padStart(2, '0');
    return `${y}-${m}-${d} ${h}:${min}:${s}`;
}

/**
 * 格式化时间为 HH:mm
 */
function formatTime(dateStr) {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    const h = String(date.getHours()).padStart(2, '0');
    const m = String(date.getMinutes()).padStart(2, '0');
    return `${h}:${m}`;
}

/**
 * 获取考勤状态文本
 */
function getAttendStatusText(status) {
    const map = {
        0: '<span class="status-tag status-default">未打卡</span>',
        1: '<span class="status-tag status-success">正常</span>',
        2: '<span class="status-tag status-warning">迟到</span>',
        3: '<span class="status-tag status-warning">早退</span>',
        4: '<span class="status-tag status-danger">迟到且早退</span>',
        5: '<span class="status-tag status-danger">缺勤</span>'
    };
    return map[status] || '<span class="status-tag status-default">未知</span>';
}

/**
 * 获取请假类型文本
 */
function getLeaveTypeText(type) {
    const map = { 1: '事假', 2: '病假', 3: '年假', 4: '婚假', 5: '产假', 6: '其他' };
    return map[type] || '未知';
}

/**
 * 获取请假状态文本
 */
function getLeaveStatusText(status) {
    const map = {
        0: '<span class="status-tag status-warning">待审批</span>',
        1: '<span class="status-tag status-success">已通过</span>',
        2: '<span class="status-tag status-danger">已驳回</span>',
        3: '<span class="status-tag status-default">已撤销</span>'
    };
    return map[status] || '<span class="status-tag status-default">未知</span>';
}

/**
 * 获取角色文本
 */
function getRoleText(role) {
    const map = { 1: '普通员工', 2: '部门经理', 3: '管理员' };
    return map[role] || '未知';
}

/**
 * 获取公告类型文本
 */
function getAnnounceTypeText(type) {
    const map = { 1: '通知', 2: '公告', 3: '制度' };
    return map[type] || '其他';
}

/**
 * 显示提示消息
 */
function showMessage(msg, type = 'info') {
    const div = document.createElement('div');
    div.className = `message-toast message-${type}`;
    div.textContent = msg;
    document.body.appendChild(div);
    setTimeout(() => {
        div.classList.add('show');
        setTimeout(() => {
            div.classList.remove('show');
            setTimeout(() => div.remove(), 300);
        }, 2000);
    }, 10);
}

/**
 * 确认对话框
 */
function confirmAction(msg) {
    return confirm(msg);
}

/**
 * 获取当前登录用户
 */
function getCurrentUser() {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
}

/**
 * 退出登录
 */
function logout() {
    apiPost('/user/logout').then(() => {
        localStorage.removeItem('currentUser');
        window.location.href = '/attendance/static/login.html';
    }).catch(() => {
        localStorage.removeItem('currentUser');
        window.location.href = '/attendance/static/login.html';
    });
}
