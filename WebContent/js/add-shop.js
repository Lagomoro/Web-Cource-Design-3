function showAddProduct(){ 
    var cookies = getCookies();
    if(cookies.username){
        document.getElementById('addProductWindow').style.display='block';
        document.getElementById('addProductFade').style.display='block';
        document.getElementById('addProductInfo').innerText = "添加新商品";
    }else{
        window.location.href = "./login.html";
    }
}
function hideAddProduct(){ 
    document.getElementById('addProductWindow').style.display='none';
    document.getElementById('addProductFade').style.display='none';
    document.getElementById('name').value = "";
    document.getElementById('price').value = "";
    document.getElementById('content').value = "";
    document.getElementById('content_data').value = "";
    activeProductButton();
}

function previewProduct(fileDom) {
	if(window.FileReader) {
		var reader = new FileReader();
	} else {
		document.getElementById('addProductInfo').innerText = "您的浏览器不支持图片预览功能";
	}
	var file = fileDom.files[0];
	var imageType = /^image\//;
	if(!imageType.test(file.type)) {
		document.getElementById('addProductInfo').innerText = "请选择一张图片";
		return;
	}
	reader.onload = function(e) {
        var img = document.getElementById("preview");
        document.getElementById('image_data').value = e.target.result;
		img.src = e.target.result;
	};
	reader.readAsDataURL(file);
}

function activeProductButton(){ 
    var button = document.getElementById('confirm');
    button.disabled = null; 
    button.value = "下一步";
}
function changeProductButton(text){ 
    var button = document.getElementById('confirm');
    button.disabled = null; 
    button.value = text;
}
function deactiveProductButton(){ 
    var button = document.getElementById('confirm');
    button.disabled = "disabled"; 
    button.value = "正在提交请求……"
}

function checkProductForm(){ 
    var image_data = document.getElementById('image_data');
    var content = document.getElementById('content');
    var content_data = document.getElementById('content_data');
    if(image_data.value == ""){
        changeProductButton("缓存出现问题，请重选图片");
        return false;
    }
    var text = content.value;
    text = text.replace(/\r\n/g, "<br>");
    text = text.replace(/\n/g, "<br>");
    text = text.replace(/\s/g, "&nbsp;");
    content_data.value = text;
    deactiveProductButton();
    return true;
}

$("iframe[name=addProductBackContent]").on("load", function() { 
    var responseText = $("iframe")[0].contentDocument.body.getElementsByTagName("pre")[0].innerHTML; 
    var json = JSON.parse(responseText);
    switch(json.err_code){
    case 0:
        checkShop();
        hideAddProduct();
        activeProductButton();
        break;
    default:
        changeProductButton(json.err_msg);
        activeProductButton();
    }
}) 