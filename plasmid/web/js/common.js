// showTitle() display/hide information.
// a: id of <DIV> tag
// b: html text to display
// x, y: top left position for <DIV>
// Requiremetn: <DIV> need to already inside the page and has style of "visibility: hidden;"

function showTitle(a, b, x, y) {
    od = document.getElementById(a);
    if ((b == null) || (b=="")) {
        try {
            od.innerHTML = "";
            od.width = 0;
            od.height = 0;
            od.style.visibility = "hidden";
        } catch (e) {

        }
    } else {
        try {
            od.innerHTML = b;
            od.style.left = x;
            od.style.top = y;
            od.style.visibility = "visible";
        } catch (e) {

        }
    }
}

function gx(b){
    var c=0;
    if(b.offsetParent){
        while(b.offsetParent){
            c+=b.offsetLeft;
            b=b.offsetParent;
        }
    }else{
        if(b.x){
            c+=b.x;
        }
    }
    return c;
}

function gy(b){
    var c=0;
    if(b.offsetParent){
        while(b.offsetParent){
            c+=b.offsetTop;
            b=b.offsetParent;
        }
    }else{
        if(b.y){
            c+=b.y;
        }
    }
    return c;
}