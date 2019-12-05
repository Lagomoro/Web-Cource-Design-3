var banner = document.getElementById("banner").children;
var imagebox = document.getElementById("images");
var numberbox = document.getElementById("numbers");
var currentIndex = 0;
var showTime = 0;
var lock = false;

var text = [
    "茶π 自成一派 你心动了吗？心动的你还不赶快入手？点此进入促销 >>",
    "气咖双重 唤醒身心 炭仌®碳酸咖啡 让你的冬天透心凉！点此进入促销 >>",
    "营养多元化 身体更健康 农夫山泉 植物酸奶 让你的生活多点绿！点此进入促销 >>",
    "优质产地种植 天然水源灌溉 阳光足温差大 这样的米 你吃不起。点此进入促销 >>",
    "喝前摇一摇 节操掉一地 妈妈你看那个人在喝节操饮料！点此进入促销 >>"
];
var classList = ["black", "white", "black", "white", "black"];
for(var i = 1; i <= 5; i++){
    addBanner("#", "images/0" + i + ".jpg", text[i-1], classList[i-1]);
}
imagebox.children[currentIndex].step = 1;
numberbox.children[currentIndex].className = "current";

function addBanner(url, image, text, className) {
    var a = document.createElement("a");
    a.href = url;
    a.step = 0;
    a.style.opacity = 0;
    var img = document.createElement("img");
    img.src = image;
    var label = document.createElement("label");
    label.innerText = text;
    label.className = className;
    a.appendChild(img);
    a.appendChild(label);
    imagebox.appendChild(a);

    var span = document.createElement("span");
    span.index = numberbox.children.length;
    span.onmouseover = function(){
        transport(this.index);
        setStep(currentIndex, 8);
        lock = true;
        showTime = 0;
    }
    span.onmouseout = function(){
        lock = false;
    }
    numberbox.appendChild(span);
    numberbox.append(" ");
}

function transport(index){
    var numbers = numberbox.children;
    for(var j = 0; j < numbers.length; j++){
        resetStep(j);
        numbers[j].className = "";
    }
    numbers[index].className = "current";
    currentIndex = index;
}

function setStep(index, step){
    var numbers = numberbox.children;
    //numbers[index].style.width = ((step * 2) * 10) + "px";
    //numbers[index].style.height = ((step * 2) * 10) + "px";
    numbers[index].style.width = "160px";
    numbers[index].style.height = "160px";
    numbers[index].style.border = ((8 - step) * 10) + "px solid orangered";
    numbers[index].style.transform = "scale(0.1)";
    numbers[index].style.margin = "-72px";
    
}
function resetStep(index){
    var numbers = numberbox.children;
    numbers[index].style.width = "12px";
    numbers[index].style.height = "12px"
    numbers[index].style.border = "none"
    numbers[index].style.transform = "none";
    numbers[index].style.margin = "2px";
}

banner.timer = setInterval(function() {
    var images = imagebox.children;
    for(var i = 0; i < images.length; i++){
        if(i === currentIndex){
            if(images[i].step < 1)
                images[i].step += 0.02;
            else
                images[i].step = 1;
        }else{
            if(images[i].step > 0)
                images[i].step -= 0.03;
            else
                images[i].step = 0;
        }
        images[i].style.opacity = images[i].step;
    }
    if(!lock)
        showTime ++;
    setStep(currentIndex, showTime * 8/300);
    if(showTime === 300){
        showTime = 0;
        transport(currentIndex === images.length - 1 ? 0 : currentIndex + 1);
    }
}, 15);