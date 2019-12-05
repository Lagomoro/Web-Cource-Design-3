function showLoginWarning(text){ 
    var warning = document.getElementById('warning');
    warning.innerText = text;
    warning.style.display = "inline-block"; 
}
function hideLoginWarning(){ 
    var warning = document.getElementById('warning');
    warning.style.display = "none"; 
}

function activeLoginButton(){ 
    var button = document.getElementById('confirm');
    button.disabled = null; 
    button.value = "下一步";
}
function deactiveLoginButton(){ 
    var button = document.getElementById('confirm');
    button.disabled = "disabled"; 
    button.value = "正在提交请求……"
}

function checkLoginForm(){ 
    var username = document.getElementById('username');
    var password = document.getElementById('password');
    var md5_password= document.getElementById('md5_password');
    if(username.value == ""){
        showLoginWarning("用户名不能为空");
        return false;
    }
    if(password.value == ""){
        showLoginWarning("密码不能为空");
        return false;
    }
    hideLoginWarning();
    deactiveLoginButton();
    md5_password.value= hex_md5(password.value);          
    return true;
}

$("iframe[name=loginBackContent]").on("load", function() { 
    var responseText = $("iframe")[0].contentDocument.body.getElementsByTagName("pre")[0].innerHTML; 
    var json = JSON.parse(responseText);
    switch(json.err_code){
    case 0:
        showLoginWarning("登录成功！ " + 3 + " 秒后跳转");
        setTimeout(function(){
            showLoginWarning("登录成功！ " + 2 + " 秒后跳转");
        },1000);
        setTimeout(function(){
            showLoginWarning("登录成功！ " + 1 + " 秒后跳转");
        },2000);
        setTimeout(function(){
            showLoginWarning("登录成功！ " + 0 + " 秒后跳转");
            window.location.href = "./index.html";
        },3000);
        break;
    default:
        showLoginWarning(json.err_msg);
        activeLoginButton();
    }
}) 