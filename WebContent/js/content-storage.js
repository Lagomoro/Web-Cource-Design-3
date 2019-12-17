function checkStorage(){
    postEvent('./getAllProduct.do', null, function(json){
        if(json.err_code == 0){
            var div = document.getElementById('storageContent');
            if(json.err_msg == ""){
                div.innerHTML = "<div><label>空空如也……</label><label>暂时没有商品哦！</label></div>";
            }else{
                div.innerHTML = json.err_msg;
            }
        }
    }, function(json){});
}

checkStorage();

function addToCart(id){
    var cookies = getCookies();
    if(cookies.username){
        postEvent('./changeCart.do', "id=" + id + "&amount=1", function(json){
            if(json.err_code == 0){
                label = document.getElementById('cart');
                label.children[0].innerText = "有新物品加入 - 我的购物车";
                setTimeout(function(){
                    label = document.getElementById('cart');
                    label.children[0].innerText = "我的购物车";
                },500);
            }
        }, function(json){});
    }else{
        window.location.href = "./login.html";
    }
}