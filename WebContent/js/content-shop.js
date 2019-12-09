function checkShop(){
    var cookies = getCookies();
    if(cookies.username){
        postEvent('./getShop.do', null, function(json){
            if(json.err_code == 0){
                var div = document.getElementById('shopContent');
                if(json.err_msg == ""){
                    div.innerHTML = "<div><label>空空如也……</label><label>添加自己的第一件商品吧！</label></div>";
                }else{
                    div.innerHTML = json.err_msg;
                }
            }
        }, function(json){});
    }else{
        var div = document.getElementById('shopContent');
        div.innerHTML = "<div><label>需要登录</label><label>请先登录，才能查看自己的店铺。</label></div>";
    }
}

shop_onloginSuccess = onloginSuccess;
onloginSuccess = function(){
    shop_onloginSuccess();
    checkShop();
}

function deleteShop(id){
    postEvent('./deleteProduct.do', "id=" + id, function(json){
        if(json.err_code == 0){
            checkShop();
        }
    }, function(json){});
}