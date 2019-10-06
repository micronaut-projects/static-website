var prevScrollpos = window.pageYOffset;
window.onscroll = function() {
    var currentScrollPos = window.pageYOffset;
    var show = false;
    if ( ((prevScrollpos > currentScrollPos) > 25) || (currentScrollPos < 5)) {
        show = true;
    }
    var top = "-201px";
    if (window.innerWidth <= 750) {
        top = "-76px";
    }
    if (show) {
        document.getElementById("navbar").style.top = "0";
    } else {
        document.getElementById("navbar").style.top = top;
    }
    prevScrollpos = currentScrollPos;
}