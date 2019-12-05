function showRegisterWarning(text){ 
    var warning = document.getElementById('warning');
    warning.innerText = text;
    warning.style.display = "inline-block"; 
}
function hideRegisterWarning(){ 
    warning.style.display = "none"; 
}

function activeRegisterButton(){ 
    var button = document.getElementById('confirm');
    button.disabled = null; 
    button.value = "下一步";
}
function deactiveRegisterButton(){ 
    var button = document.getElementById('confirm');
    button.disabled = "disabled"; 
    button.value = "正在提交请求……"
}

function checkRegisterForm(){ 
    var username = document.getElementById('username');
    var nickname = document.getElementById('nickname');
    var password = document.getElementById('password');
    var confirm_password= document.getElementById('confirm_password');
    var md5_password= document.getElementById('md5_password');
    if(username.value == ""){
        showRegisterWarning("用户名不能为空");
        return false;
    }
    if(nickname.value == ""){
        showRegisterWarning("昵称不能为空");
        return false;
    }
    if(password.value == ""){
        showRegisterWarning("密码不能为空");
        return false;
    }
    if(password.value !== confirm_password.value){
        showRegisterWarning("两次输入的密码不一致");
        return false;
    }
    hideRegisterWarning();
    deactiveRegisterButton();
    md5_password.value= hex_md5(password.value);          
    return true;
}

$("iframe[name=registerBackContent]").on("load", function() { 
    var responseText = $("iframe")[0].contentDocument.body.getElementsByTagName("pre")[0].innerHTML;
    var json = JSON.parse(responseText);
    switch(json.err_code){
    case 0:
        showRegisterWarning("注册成功！ " + 3 + " 秒后跳转");
        setTimeout(function(){
            showRegisterWarning("注册成功！ " + 2 + " 秒后跳转");
        },1000);
        setTimeout(function(){
            showRegisterWarning("注册成功！ " + 1 + " 秒后跳转");
        },2000);
        setTimeout(function(){
            showRegisterWarning("注册成功！ " + 0 + " 秒后跳转");
            window.location.href = "./index.html";
        },3000);
        break;
    default:
        showRegisterWarning(json.err_msg);
        activeRegisterButton();
    }
}) 