function checkOrder(){
    var cookies = getCookies();
    if(cookies.username){
        postEvent('./getOrder.do', null, function(json){
            if(json.err_code == 0){
                var div = document.getElementById('orderContent');
                if(json.err_msg == ""){
                    div.innerHTML = "<div><label>空空如也……</label><label>添加自己的第一件订单吧！</label></div>";
                }else{
                    div.innerHTML = json.err_msg;
                }
            }
        }, function(json){});
    }else{
        var div = document.getElementById('orderContent');
        div.innerHTML = "<div><label>需要登录</label><label>请先登录，才能查看自己的订单。</label></div>";
    }
}

checkOrder();

function deleteOrder(id){
    postEvent('./deleteOrder.do', "id=" + id, function(json){
        if(json.err_code == 0){
            checkOrder();
        }
    }, function(json){});
}